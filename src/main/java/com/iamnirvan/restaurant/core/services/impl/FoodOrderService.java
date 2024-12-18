package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.enums.EActiveStatus;
import com.iamnirvan.restaurant.core.enums.EFoodOrderStatus;
import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Cart;
import com.iamnirvan.restaurant.core.models.entities.Customer;
import com.iamnirvan.restaurant.core.models.entities.FoodOrder;
import com.iamnirvan.restaurant.core.models.requests.food_order.FoodOrderCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderGetResponse;
import com.iamnirvan.restaurant.core.models.responses.food_order.FoodOrderUpdateStatusResponse;
import com.iamnirvan.restaurant.core.repositories.CartRepository;
import com.iamnirvan.restaurant.core.repositories.FoodOrderRepository;
import com.iamnirvan.restaurant.core.services.IFoodOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

            FoodOrder foodOrder = FoodOrder.builder()
                    .notes(request.getNotes())
                    .status(EFoodOrderStatus.IN_PROGRESS)
                    .total(cart.getTotalValue())
                    .date(OffsetDateTime.now())
                    .cart(cart)
                    .build();

            foodOrderRepository.save(foodOrder);
            cart.setFoodOrder(foodOrder);
            cartRepository.save(cart);
            log.debug("Created order: {}", foodOrder);

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

    /**
     * Retrieves a list of food orders based on the provided order ID or customer ID.
     *
     * @param id the ID of the food order to retrieve (optional)
     * @param customerId the ID of the customer whose orders to retrieve (optional)
     * @return a list of FoodOrderGetResponse objects containing the details of the retrieved orders
     * @throws NotFoundException if the order with the specified ID does not exist
     */
    @Override
    public List<FoodOrderGetResponse> getOrders(Long id, Long customerId) {

        if (id != null) {
            FoodOrder foodOrder = foodOrderRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Order with id %d does not exist", id)));
            return List.of(Parser.toFoodOrderGetResponse(foodOrder));
        }

        if (customerId != null) {
            return foodOrderRepository.findAllByCustomerIdOrderByDateAsc(customerId).stream().map(Parser::toFoodOrderGetResponse).collect(Collectors.toList());
        }

        return foodOrderRepository.findAllOrderByDateAsc().stream().map(Parser::toFoodOrderGetResponse).collect(Collectors.toList());
    }


    /**
     * Updates the status of a food order based on the provided order ID and status.
     *
     * @param id the ID of the food order to update
     * @param status the new status to set for the food order
     * @return a FoodOrderUpdateStatusResponse object containing the details of the updated order
     * @throws NotFoundException if the order with the specified ID does not exist
     * @throws ConflictException if the status transition is not allowed
     */
    @Override
    public FoodOrderUpdateStatusResponse updateOrderStatus(Long id, EFoodOrderStatus status) {
        final FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id %d does not exist", id)));

        if (status == EFoodOrderStatus.IN_PROGRESS) {
            order.setStatus(status);
        } else if (status == EFoodOrderStatus.COMPLETED) {
            if (order.getStatus() != EFoodOrderStatus.IN_PROGRESS) {
                throw new ConflictException("Order must be in progress to be completed");
            }
            order.setStatus(status);
        } else if (status == EFoodOrderStatus.CANCELLED) {
            if (order.getStatus() != EFoodOrderStatus.IN_PROGRESS) {
                throw new ConflictException("Order must be in progress to be cancelled");
            }
            order.setStatus(status);
        } else if (status == EFoodOrderStatus.PAID) {
            if (order.getStatus() != EFoodOrderStatus.COMPLETED) {
                throw new ConflictException("Order must be completed to be paid");
            }
            order.setStatus(status);
        }
        foodOrderRepository.save(order);
        log.debug("Updated order status: {}", order);

        return Parser.toFoodOrderUpdateStatusResponse(order);
    }

    public static class Parser {
        public static FoodOrderCreateResponse toFoodOrderCreateResponse(@NotNull FoodOrder foodOrder) {
            return FoodOrderCreateResponse.builder()
                    .id(foodOrder.getId())
                    .notes(foodOrder.getNotes())
                    .cartId(foodOrder.getCart().getId())
                    .status(foodOrder.getStatus())
                    .total(foodOrder.getTotal())
                    .date(foodOrder.getDate())
                    .build();
        }

        public static FoodOrderGetResponse toFoodOrderGetResponse(@NotNull FoodOrder foodOrder) {
            return FoodOrderGetResponse.builder()
                    .id(foodOrder.getId())
                    .notes(foodOrder.getNotes())
                    .status(foodOrder.getStatus())
                    .total(foodOrder.getTotal())
                    .date(foodOrder.getDate())
                    .build();
        }

        public static FoodOrderUpdateStatusResponse toFoodOrderUpdateStatusResponse(@NotNull FoodOrder foodOrder) {
            return FoodOrderUpdateStatusResponse.builder()
                    .id(foodOrder.getId())
                    .status(foodOrder.getStatus())
                    .build();
        }
    }
}
