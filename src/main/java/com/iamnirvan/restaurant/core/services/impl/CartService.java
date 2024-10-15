package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Cart;
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
import com.iamnirvan.restaurant.core.services.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final DishPortionRepository dishPortionRepository;
    private static DishPortionCartRepository dishPortionCartRepository;

    @Autowired
    public CartService(CartRepository cartRepository, DishPortionRepository dishPortionRepository, DishPortionCartRepository dpcRepository) {
        this.cartRepository = cartRepository;
        this.dishPortionRepository = dishPortionRepository;
        dishPortionCartRepository = dpcRepository;
    }

    /**
     * Manipulates the content of the cart by adding or updating dish portions.
     *
     * @param id the ID of the cart
     * @param requests a list of requests containing dish portion IDs and quantities to be added or updated
     * @return a list of responses containing the updated cart content
     * @throws NotFoundException if the cart or dish portion does not exist
     * @throws ConflictException if the quantity is zero when adding a new dish portion
     */
    @Override
    @Transactional
    public List<AddDishIntoCartResponse> manipulateCartContent(Long id, List<AddDishIntoCartRequest> requests) {
        // TODO: Get the customer's active cart (get the customer id from the token)
        final List<AddDishIntoCartResponse> result = new ArrayList<>();

        final Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %d does not exist", id)));

        for (AddDishIntoCartRequest request : requests) {
            boolean removeItemFromCart = false;

            DishPortionCart dishPortionCart = dishPortionCartRepository.findByCartIdAndDishPortionId(
                    cart.getId(),
                    request.getDishPortionId()
            );

            // Get the dish portion (small pizza) from the database
            final DishPortion dishPortion = dishPortionRepository.findById(request.getDishPortionId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish portion with id %d does not exist", request.getDishPortionId())));

            if (dishPortionCart != null) {
                // This means the dishPortion is already in then cart... Therefore, update the quantity.
                if (request.getQuantity() == 0) {
                    removeItemFromCart = true;
                } else {
                    dishPortionCart.setQuantity(request.getQuantity());
                }
            } else {
                // Check if the quantity is 0
                if (request.getQuantity() == 0) {
                    throw new ConflictException("Quantity must be greater than 0");
                }

                dishPortionCart = DishPortionCart.builder()
                        .quantity(request.getQuantity())
                        .cart(cart)
                        .dishPortion(dishPortion)
                        .build();
            }

            // Save the changes that were made to the cart
            if (removeItemFromCart) {
                dishPortionCartRepository.delete(dishPortionCart);
            } else {
                dishPortionCartRepository.save(dishPortionCart);
            }

            double totalValue = Util.calculateCartTotalValue(cart);
            cart.setTotalValue(totalValue);
            cartRepository.save(cart);
            log.debug("Updated contents of the cart");

            result.add(Parser.toAddDishIntoCartResponse(cart, dishPortion, request.getQuantity()));
        }

        return result;
    }

    /**
     * Removes multiple dishes from the cart
     *
     * @param id       cart id
     * @param requests requests containing the dish portion cart ids to be removed
     * @return response containing the dish portions removed
     * @throws NotFoundException if the cart or dish portion cart does not exist
     */
    @Override
    @Transactional
    @Deprecated
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
     * Retrieves the content of the cart.
     *
     * @param id the ID of the cart
     * @return a response containing the cart content
     * @throws NotFoundException if the cart does not exist
     */
    @Override
    public GetCartResponse getCartContent(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Cart with id %s does not exist", id)));
        return Parser.toGetCartResponse(cart);
    }

    public static class Parser {
        public static AddDishIntoCartResponse toAddDishIntoCartResponse(Cart cart, DishPortion dishPortion, int quantity) {
            return AddDishIntoCartResponse.builder()
                    .cartId(cart.getId())
                    .totalValue(cart.getTotalValue())
                    .dishPortion(DishPortionGetResponse.builder()
                            .dishName(dishPortion.getDish().getName())
                            .portionName(dishPortion.getPortion().getName())
                            .price(dishPortion.getPrice())
                            .quantity(quantity)
                            .build())
                    .build();
        }

        public static GetCartResponse toGetCartResponse(Cart cart) {
            return GetCartResponse.builder()
                    .id(cart.getId())
                    .dishes(cart.getDishPortionCarts().stream().map(cartContents -> Parser.toDishPortionGetResponse(cartContents.getDishPortion(), cartContents.getQuantity())).collect(Collectors.toList()))
                    .build();
        }

        public static DishPortionGetResponse toDishPortionGetResponse(DishPortion dishPortion, int quantity) {
            return DishPortionGetResponse.builder()
                    .dishName(dishPortion.getDish().getName())
                    .portionName(dishPortion.getPortion().getName())
                    .price(dishPortion.getPrice())
                    .quantity(quantity)
                    .build();
        }
    }

    public static class Util {
        public static double calculateCartTotalValue(Cart cart) {
            List<DishPortionCart> cartContents = dishPortionCartRepository.findAllByCartId(cart.getId());
            return cartContents.stream().mapToDouble(cartContent -> cartContent.getDishPortion().getPrice() * cartContent.getQuantity()).sum();
        }
    }
}
