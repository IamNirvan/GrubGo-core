package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.ConflictException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
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
import com.iamnirvan.restaurant.core.models.responses.dish_portion.DishPortionGetResponseWithoutDishName;
import com.iamnirvan.restaurant.core.models.responses.metrics.DishMetrics;
import com.iamnirvan.restaurant.core.models.responses.metrics.MonthlySales;
import com.iamnirvan.restaurant.core.models.responses.metrics.UnitsSoldPerMonthProjection;
import com.iamnirvan.restaurant.core.models.responses.review.ReviewGetResponseWithoutDishId;
import com.iamnirvan.restaurant.core.repositories.DishRepository;
import com.iamnirvan.restaurant.core.repositories.PortionRepository;
import com.iamnirvan.restaurant.core.services.IDishService;
import com.iamnirvan.restaurant.core.util.Image.ImageUtil;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DishService implements IDishService {
    private final DishRepository dishRepository;
    private final PortionRepository portionRepository;
    private static ImageUtil imageUtil;

    @Autowired
    public DishService(DishRepository dishRepository, PortionRepository portionRepository, ImageUtil imgUtil) {
        this.dishRepository = dishRepository;
        this.portionRepository = portionRepository;
        imageUtil = imgUtil;
    }

    /**
     * Creates new dishes based on the provided requests.
     *
     * @param requests the list of dish creation requests
     * @return a list of responses containing the details of the created dishes
     * @throws ConflictException if a dish with the given name already exists
     * @throws NotFoundException if a portion with the given ID does not exist
     */
    @Transactional
    @Override
    public List<DishCreateResponse> createDish(List<DishCreateRequest> requests) {
        final List<DishCreateResponse> result = new ArrayList<>();

        for (DishCreateRequest request : requests) {
            if (dishRepository.existsByName(request.getName())) {
                throw new ConflictException(String.format("Dish with name %s already exists", request.getName()));
            }

            MultipartFile image = request.getImage();
            byte[] compressedImage = null;
            if (image != null) {
                compressedImage = Parser.toCompressedImage(image);
            }

            Dish dish = Dish.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .image(compressedImage)
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
                    throw new ConflictException(String.format("Portion with id %s is already assigned to dish", portion.getId()));
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

            dish.setDishPortions(dishPortions);
            dishRepository.save(dish);
            log.debug(String.format("Dish created: %s", dish));

            result.add(Parser.toDishCreateResponse(dish));
        }
        return result;
    }

    /**
     * Updates existing dishes based on the provided requests.
     *
     * @param requests the list of dish update requests
     * @return a list of responses containing the updated details of the dishes
     * @throws NotFoundException   if a dish with the given ID does not exist
     * @throws BadRequestException if the name or description is empty or if a dish with the new name already exists
     */
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

            if (request.getImage() != null) {
                byte[] compressedImage = Parser.toCompressedImage(request.getImage());
                dish.setImage(compressedImage);
            }

            dish.setUpdated(OffsetDateTime.now());
            dishRepository.save(dish);
            log.debug(String.format("Dish updated: %s", dish));

            result.add(Parser.toDishUpdateResponse(dish));
        }
        return result;
    }

    /**
     * Deletes dishes based on the provided IDs.
     *
     * @param ids the list of dish IDs to delete
     * @return a list of responses containing the details of the deleted dishes
     * @throws NotFoundException if a dish with the given ID does not exist
     */
    @Transactional
    @Override
    public List<DishDeleteResponse> deleteDish(List<Long> ids) {
        final List<DishDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", id)));

            dishRepository.delete(dish);
            log.debug(String.format("Dish deleted: %s", dish));

            result.add(Parser.toDishDeleteResponse(dish));
        }
        return result;
    }

    /**
     * Retrieves dishes based on the provided ID.
     *
     * @param id the ID of the dish to retrieve (optional)
     * @return a list of responses containing the details of the retrieved dishes
     * @throws NotFoundException if a dish with the given ID does not exist
     */
    @Override
    public List<DishGetResponse> getDishes(Long id, boolean includeImg) {
        if (id != null) {
            Dish dish = dishRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Dish with id %s does not exist", id)));
            return List.of(Parser.toDishGetResponse(dish, includeImg));
        }
        return dishRepository.findAll().stream().map(dish -> Parser.toDishGetResponse(dish, includeImg)).collect(Collectors.toList());
    }

    @Override
    public DishMetrics getDishMetrics(Long id) {
        dishRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Dish with id %s does not exist", id))
        );

        final LocalDate today = LocalDate.now();
        final LocalDateTime startDate = today.atTime(LocalTime.MIN);
        final LocalDateTime endDate = today.atTime(LocalTime.MAX);

        return Parser.toDishStats(
                dishRepository.getRevenueAccountedFor(id),
                dishRepository.getUnitsSoldToday(id, startDate.toString(), endDate.toString()),
                dishRepository.getUnitsSoldThisQuarter(id),
                dishRepository.getMonthlySalesForDish(id)
        );
    }

    public static class Parser {
        private static byte[] toCompressedImage(@NotNull MultipartFile file) {
            try {
                return imageUtil.compressImage(file.getBytes());
            } catch (IOException e) {
                log.error("Failed to compress image", e);
                throw new RuntimeException(e);
            }
        }

        private static DishCreateResponse toDishCreateResponse(Dish dish) {
            final Set<DishPortion> dishPortions = dish.getDishPortions();

            return DishCreateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .portions(dishPortions.stream().map(portion -> DishPortionGetResponseWithoutDishName.builder()
                            .id(portion.getId())
                            .portionName(portion.getPortion().getName())
                            .price(portion.getPrice())
                            .build()).collect(Collectors.toList()))
                    .created(dish.getCreated())
                    .updated(dish.getUpdated())
                    .build();
        }

        private static DishUpdateResponse toDishUpdateResponse(Dish dish) {
            return DishUpdateResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
                    .updated(dish.getUpdated())
                    .build();
        }

        private static DishDeleteResponse toDishDeleteResponse(Dish dish) {
            return DishDeleteResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .created(dish.getCreated())
                    .updated(dish.getUpdated())
                    .build();
        }

        private static DishGetResponse toDishGetResponse(Dish dish, boolean includeImg) {
            final byte[] decompressedImage = (dish.getImage() == null || !includeImg) ? null : imageUtil.decompressImage(dish.getImage());

            return DishGetResponse.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .reviews(dish.getReviews().stream().map(review -> ReviewGetResponseWithoutDishId.builder()
                            .id(review.getId())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .rating(review.getRating())
                            .customerId(review.getCustomer().getId())
                            .created(review.getCreated())
                            .updated(review.getUpdated())
                            .build()).collect(Collectors.toSet()))
                    .dishPortions(dish.getDishPortions().stream().map(portion -> DishPortionGetResponseWithoutDishName.builder()
                            .id(portion.getId())
                            .portionName(portion.getPortion().getName())
                            .price(portion.getPrice())
                            .build()).collect(Collectors.toSet()))
                    .image(decompressedImage)
                    .created(dish.getCreated())
                    .updated(dish.getUpdated())
                    .build();
        }

        private static DishMetrics toDishStats(float revenueAccountedFor, long unitsToday, long unitsQuarter, List<UnitsSoldPerMonthProjection> unitsSoldPerMonthProjections) {
            List<MonthlySales> monthlySales = new ArrayList<>();
            for (UnitsSoldPerMonthProjection unitsSoldPerMonthProjection : unitsSoldPerMonthProjections) {
                monthlySales.add(MonthlySales.builder()
                        .month(unitsSoldPerMonthProjection.getMonth().trim())
                        .unitsSold(unitsSoldPerMonthProjection.getUnitsSold())
                        .build());
            }

            return DishMetrics.builder()
                    .unitsSoldToday(unitsToday)
                    .unitsSoldThisQuarter(unitsQuarter)
                    .revenueAccountedFor(revenueAccountedFor)
                    .monthlySales(monthlySales)
                    .build();
        }
    }
}
