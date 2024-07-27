package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.enums.EStatus;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Cart;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class FoodOrderService implements IFoodOrderService {
    private final CartRepository cartRepository;
    private final FoodOrderRepository foodOrderRepository;

    @Override
    @Transactional
    public List<FoodOrderCreateResponse> placeOrder(List<FoodOrderCreateRequest> requests) {
        final List<FoodOrderCreateResponse> result = new ArrayList<>();
        for (FoodOrderCreateRequest request : requests) {
            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new NotFoundException(String.format("Cart with id %d does not exist", request.getCartId())));

            // Get the items (dish portion ids) from the cart
            // Access the dish, its portion and quantity
            double total = 0;
            for (DishPortionCart dishPortionCart : cart.getDishPortionCarts()) {
                total += (dishPortionCart.getDishPortion().getPrice() * dishPortionCart.getQuantity());
            }

            FoodOrder foodOrder = FoodOrder.builder()
                    .notes(request.getNotes())
                    .status(EStatus.IN_PROGRESS)
                    .total(total)
                    .customer(cart.getCustomer())
                    .date(OffsetDateTime.now())
                    .cart(cart)
                    .build();

            foodOrderRepository.save(foodOrder);
            cart.setFoodOrder(foodOrder);
            cartRepository.save(cart);
            log.debug(String.format("Created order: %s", foodOrder));

            result.add(FoodOrderCreateResponse.builder()
                    .notes(request.getNotes())
                    .cartId(cart.getId())
                    .date(foodOrder.getDate())
                    .status(foodOrder.getStatus())
                    .total(foodOrder.getTotal())
                    .build());
        }
        return result;
    }
}
