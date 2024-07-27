package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Cart;
import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.entities.DishPortion;
import com.iamnirvan.restaurant.core.models.entities.DishPortionCart;
import com.iamnirvan.restaurant.core.models.requests.cart.AddDishIntoCartRequest;
import com.iamnirvan.restaurant.core.models.requests.cart.RemoveDishFromCartRequest;
import com.iamnirvan.restaurant.core.models.responses.cart.AddDishIntoCartResponse;
import com.iamnirvan.restaurant.core.models.responses.cart.GetCartResponse;
import com.iamnirvan.restaurant.core.models.responses.cart.RemoveDishFromCartResponse;
import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponse;
import com.iamnirvan.restaurant.core.repositories.CartRepository;
import com.iamnirvan.restaurant.core.repositories.DishPortionCartRepository;
import com.iamnirvan.restaurant.core.repositories.DishPortionRepository;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.services.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final DishRepository dishRepository;
    private final DishPortionRepository dishPortionRepository;
    private final DishPortionCartRepository dishPortionCartRepository;

    /**
     * Adds multiple items into the cart
     * @param id cart id
     * @param requests list of requests containing the dish portion ids and quantities to be added
     * @return response containing the dish portions added
     * @throws NotFoundException if the cart or dish portion does not exist
     * */
    @Override
    @Transactional
    public List<AddDishIntoCartResponse> addDishPortionIntoCart(Long id, List<AddDishIntoCartRequest> requests) {
        final Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %d does not exist", id)));

        List<AddDishIntoCartResponse> result = new ArrayList<>();
        for (AddDishIntoCartRequest request : requests) {
            DishPortion dishPortion = dishPortionRepository.findById(request.getDishPortionId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish portion with id %d does not exist", request.getDishPortionId())));

            Dish dish = dishRepository.findById(dishPortion.getDish().getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %d does not exist", dishPortion.getDish().getId())));

            // Save the associative entity record...
            DishPortionCart dishPortionCart = DishPortionCart.builder()
                    .cart(cart)
                    .dishPortion(dishPortion)
                    .quantity(request.getQuantity())
                    .build();
            dishPortionCartRepository.save(dishPortionCart);

            result.add(AddDishIntoCartResponse.builder()
                    .cartId(cart.getId())
                    .dishPortion(DishPortionGetResponse.builder()
                            .dishName(dish.getName())
                            .portionName(dishPortion.getPortion().getName())
                            .price(dishPortion.getPrice())
                            .quantity(request.getQuantity())
                            .build())
                    .build());

        }
        log.debug("Dishes added to the cart");
        return result;
    }

    /**
     * Removes multiple dishes from the cart
     * @param id cart id
     * @param requests requests containing the dish portion cart ids to be removed
     * @return response containing the dish portions removed
     * @throws NotFoundException if the cart or dish portion cart does not exist
     * */
    @Override
    @Transactional
    public List<RemoveDishFromCartResponse> removeDishPortionFromCart(Long id, List<RemoveDishFromCartRequest> requests) {
        final Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %d does not exist", id)));

        List<RemoveDishFromCartResponse> result = new ArrayList<>();
        for (RemoveDishFromCartRequest request : requests) {
            DishPortionCart dishPortionCart = dishPortionCartRepository.findById(request.getDishPortionCartId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish portion cart with id %d does not exist", request.getDishPortionCartId())));

            result.add(RemoveDishFromCartResponse.builder()
                    .dishPortionCartId(dishPortionCart.getId())
                    .dishPortion(DishPortionGetResponse.builder()
                            .dishName(dishPortionCart.getDishPortion().getDish().getName())
                            .portionName(dishPortionCart.getDishPortion().getPortion().getName())
                            .price(dishPortionCart.getDishPortion().getPrice())
                            .quantity(dishPortionCart.getQuantity())
                            .build())
                    .build());

            dishPortionCartRepository.delete(dishPortionCart);
        }
        return null;
    }

    /**
     * Gets all the carts or the cart corresponding to the id
     * @param id cart id
     * @return response containing the cart and its items
     * @throws NotFoundException if the cart does not exist
     * */
    @Override
    public GetCartResponse getCart(Long id) {
        List<DishPortionGetResponse> dishes = new ArrayList<>();
        if (id != null) {
            Cart cart = cartRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Cart with id %s does not exist", id)));

            for (DishPortionCart dishPortionCart : cart.getDishPortionCarts()) {
                dishes.add(DishPortionGetResponse.builder()
                        .dishName(dishPortionCart.getDishPortion().getDish().getName())
                        .portionName(dishPortionCart.getDishPortion().getPortion().getName())
                        .price(dishPortionCart.getDishPortion().getPrice())
                        .quantity(dishPortionCart.getQuantity())
                        .build());
            }
           return GetCartResponse.builder()
                    .id(cart.getId())
                    .dishes(dishes)
                    .build();
        } else {
            for (Cart cart : cartRepository.findAll()) {
                for (DishPortionCart dishPortionCart : cart.getDishPortionCarts()) {
                    dishes.add(DishPortionGetResponse.builder()
                            .dishName(dishPortionCart.getDishPortion().getDish().getName())
                            .portionName(dishPortionCart.getDishPortion().getPortion().getName())
                            .price(dishPortionCart.getDishPortion().getPrice())
                            .quantity(dishPortionCart.getQuantity())
                            .build());
                }
                return GetCartResponse.builder()
                        .id(cart.getId())
                        .dishes(dishes)
                        .build();
            }
        }
        return null;
    }
}
