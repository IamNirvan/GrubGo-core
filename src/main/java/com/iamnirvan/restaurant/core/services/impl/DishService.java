package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Category;
import com.iamnirvan.restaurant.core.models.entities.Dish;
import com.iamnirvan.restaurant.core.models.entities.DishPortion;
import com.iamnirvan.restaurant.core.models.entities.Portion;
import com.iamnirvan.restaurant.core.models.requests.dish.DishCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.dish.DishUpdateRequest;
import com.iamnirvan.restaurant.core.models.requests.dish_portion.DishPortionCreateRequest;
import com.iamnirvan.restaurant.core.models.responses.dish.DishCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishGetResponse;
import com.iamnirvan.restaurant.core.models.responses.dish.DishUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CategoryRepository;
import com.iamnirvan.restaurant.core.repositories.DishPortionRepository;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.repositories.PortionRepository;
import com.iamnirvan.restaurant.core.services.IDishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class DishService implements IDishService {
    private final DishRepository dishRepository;
    private final PortionRepository portionRepository;
    private final DishPortionRepository dishPortionRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Create multiple dish instances using a list of create requests. If contained in the request,
     * dish portions can also be assigned to the dish along with their prices.
     * @param requests list of create requests
     * @return list of create responses
     * @throws NotFoundException if portion with id does not exist
     * */
    @Transactional
    @Override
    public List<DishCreateResponse> createDish(List<DishCreateRequest> requests) {
        final List<DishCreateResponse> result = new ArrayList<>();

        for (DishCreateRequest request : requests) {
            if(dishRepository.existsByName(request.getName())) {
                throw new BadRequestException(String.format("Dish with name %s already exists", request.getName()));
            }

            Dish dish = Dish.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .created(OffsetDateTime.now())
                    .build();

            Set<DishPortion> dishPortions = new HashSet<>();
            Map<Long, Boolean> portionPersentMap = new HashMap<>();
            for (DishPortionCreateRequest dishPortionCreateRequest : request.getDishPortion()) {
                Portion portion = portionRepository.findById(dishPortionCreateRequest.getPortionId())
                        .orElseThrow(() -> new NotFoundException(String.format("Portion with id %s does not exist",
                                dishPortionCreateRequest.getPortionId())));

                // When assigning portions, ensure it is not repeated for each dish...
                if (portionPersentMap.get(portion.getId()) != null && portionPersentMap.get(portion.getId())) {
                    throw new BadRequestException(String.format("Portion with id %s is already assigned to dish", portion.getId()));
                } else {
                    portionPersentMap.put(portion.getId(), true);
                }

                dishPortions.add(DishPortion.builder()
                        .price(dishPortionCreateRequest.getPrice())
                        .dish(dish)
                        .portion(portion)
                        .build());
            }
            log.debug("assigned dish portions to dish");

            final Set<Category> categories = new HashSet<>();
            Map<Long, Boolean> categoryPersentMap = new HashMap<>();
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist", categoryId)));

                // When assigning categories, ensure it is not repeated for each dish...
                if (categoryPersentMap.get(categoryId) != null && categoryPersentMap.get(categoryId)) {
                    throw new BadRequestException(String.format("Category with id %s is already assigned to dish", categoryId));
                } else {
                    categoryPersentMap.put(categoryId, true);
                }

                categories.add(category);
            }

            dish.setDishPortions(dishPortions);
//            dish.setCategories(categories);
            dishRepository.save(dish);
            log.debug(String.format("Dish created: %s", dish));

            // Save the dish on the category entity...
            Set<Dish> categoryDishes;
            for (Category category : categories) {
                categoryDishes = category.getDishes();
                categoryDishes.add(dish);
                category.setDishes(categoryDishes);
                categoryRepository.save(category);
            }

            result.add(DishCreateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
//                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
//                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Update multiple dish instances using a list of update requests
     * @param requests list of update requests
     * @return list of update responses
     * @throws BadRequestException if name is empty
     * @throws BadRequestException if description is empty
     * @throws NotFoundException if dish with id does not exist
     * */
    @Transactional
    @Override
    public List<DishUpdateResponse> updateDish(List<DishUpdateRequest> requests) {
        final List<DishUpdateResponse> result = new ArrayList<>();

        for (DishUpdateRequest request : requests) {
            Dish dish = dishRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", request.getId())));

            if (request.getName() != null) {
                if (request.getName().isEmpty()) {
                    throw new BadRequestException("Name cannot be empty");
                }

                if (dishRepository.existsByNameExcludingId(request.getName(), request.getId())) {
                    throw new BadRequestException(String.format("Dish with name %s already exists", request.getName()));
                }

                dish.setName(request.getName());
            }

            if (request.getDescription() != null) {
                if (request.getDescription().isEmpty()) {
                    throw new BadRequestException("Description cannot be empty");
                }
                dish.setDescription(request.getDescription());
            }

            dish.setUpdated(OffsetDateTime.now());
            dishRepository.save(dish);
            log.debug(String.format("Dish updated: %s", dish));

            result.add(DishUpdateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
//                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
//                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Delete multiple dish instances using a list of ids
     * @param ids list of ids
     * @return list of delete responses
     * @throws NotFoundException if dish with id does not exist
     * */
    @Transactional
    @Override
    public List<DishDeleteResponse> deleteDish(List<Long> ids) {
        final List<DishDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", id)));

            dishRepository.delete(dish);
            log.debug(String.format("Dish deleted: %s", dish));

            result.add(DishDeleteResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
//                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
//                    .updatedBy(dish.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Gets all dishes or dish using the id
     * @param id id of the dish
     * @return list of get responses
     * @throws NotFoundException if dish with id does not exist
     * */
    @Override
    public List<DishGetResponse> getDishes(Long id) {
        final List<DishGetResponse> result = new ArrayList<>();
        if (id != null) {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", id)));
            result.add(DishGetResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
//                    .images(dish.getImages())
                    .reviews(dish.getReviews())
                    .dishPortions(dish.getDishPortions())
                    .created(dish.getCreated())
//                    .createdBy(dish.getCreatedBy())
                    .updated(dish.getUpdated())
//                    .updatedBy(dish.getUpdatedBy())
                    .build());
        } else {
            for (Dish dish : dishRepository.findAll()) {
                result.add(DishGetResponse.builder()
                        .id(dish.getId())
                        .name(dish.getName())
                        .description(dish.getDescription())
//                        .images(dish.getImages())
                        .reviews(dish.getReviews())
                        .dishPortions(dish.getDishPortions())
                        .created(dish.getCreated())
//                        .createdBy(dish.getCreatedBy())
                        .updated(dish.getUpdated())
//                        .updatedBy(dish.getUpdatedBy())
                        .build());
            }
        }
        return result;
    }
}
