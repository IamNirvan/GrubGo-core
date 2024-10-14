package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.enums.EActiveStatus;
import com.iamnirvan.restaurant.core.enums.EStatus;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Cart;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.DishPortionCart;
import com.iamnirvan.restaurant.core.models.entities.FoodOrder;
import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderCreateResponse;
import com.iamnirvan.restaurant.core.repositories.CartRepository;
import com.iamnirvan.restaurant.core.repositories.FoodOrderRepository;
import com.iamnirvan.restaurant.core.services.IFoodOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class FoodOrderService implements IFoodOrderService {
    private final CartRepository cartRepository;
    private final FoodOrderRepository foodOrderRepository;

    /**
     * Places an order based on the provided list of food order requests.
     *
     * @param requests a list of FoodOrderCreateRequest objects containing the details of the orders to be placed
     * @return a list of FoodOrderCreateResponse objects containing the details of the placed orders
     * @throws NotFoundException if the cart with the specified ID does not exist
     */
    @Override
    @Transactional
    public List<FoodOrderCreateResponse> placeOrder(List<FoodOrderCreateRequest> requests) {
        final List<FoodOrderCreateResponse> result = new ArrayList<>();

        for (FoodOrderCreateRequest request : requests) {
            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new NotFoundException(String.format("Cart with id %d does not exist", request.getCartId())));

            // TODO: get the total from the cart's total value field (needs to be added. represents the cost of all items in the cart + taxes, etc..)
            double total = 0;
            for (DishPortionCart dishPortionCart : cart.getDishPortionCarts()) {
                total += (dishPortionCart.getDishPortion().getPrice() * dishPortionCart.getQuantity());
            }

            FoodOrder foodOrder = FoodOrder.builder()
                    .notes(request.getNotes())
                    .status(EStatus.IN_PROGRESS)
                    .total(total)
                    .date(OffsetDateTime.now())
                    .cart(cart)
                    .build();

            foodOrderRepository.save(foodOrder);
            cart.setFoodOrder(foodOrder);
            cartRepository.save(cart);
            log.debug(String.format("Created order: %s", foodOrder));

            // Deactivate the old cart
            cart.setStatus(EActiveStatus.INACTIVE);
            cartRepository.save(cart);
            log.debug("deactivated the old cart...");

            // Make a new active cart for the customer
            Customer customer = cart.getCustomer();
            Set<Cart> customerCarts = customer.getCart();

            Cart newCart = Cart.builder()
                    .customer(customer)
                    .status(EActiveStatus.ACTIVE)
                    .build();
            cartRepository.save(newCart);
            log.debug("created new active cart for customer");

            customerCarts.add(newCart);
            customer.setCart(customerCarts);
            log.debug("updated customer's carts");

            result.add(Parser.toFoodOrderCreateResponse(foodOrder));
        }
        return result;
    }

    public static class Parser {
        public static FoodOrderCreateResponse toFoodOrderCreateResponse(FoodOrder foodOrder) {
            return FoodOrderCreateResponse.builder()
                    .id(foodOrder.getId())
                    .notes(foodOrder.getNotes())
                    .cartId(foodOrder.getCart().getId())
                    .status(foodOrder.getStatus())
                    .total(foodOrder.getTotal())
                    .date(foodOrder.getDate())
                    .build();
        }
    }
}
