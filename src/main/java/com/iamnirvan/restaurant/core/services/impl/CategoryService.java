package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Category;
import com.iamnirvan.restaurant.core.models.requests.category.CategoryCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.category.CategoryUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryGetResponse;
import com.iamnirvan.restaurant.core.models.responses.category.CategoryUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.CategoryRepository;
import com.iamnirvan.restaurant.core.services.ICategoryService;
import com.iamnirvan.restaurant.core.util.Converter;
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
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final Converter converter;

    /**
     * Creates multiple categories using a list of create requests
     * @param requests list of create requests
     * @return list of create responses
     * @throws BadRequestException if category with name already exists
     * */
    @Override
    @Transactional
    public List<CategoryCreateResponse> createCategory(List<CategoryCreateRequest> requests) {
        final List<CategoryCreateResponse> result = new ArrayList<>();

        for (CategoryCreateRequest request : requests) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new BadRequestException(String.format("Category with name %s already exists", request.getName()));
            }

            Category category = Category.builder()
                    .name(request.getName())
                    .created(OffsetDateTime.now())
                    .build();

            categoryRepository.save(category);
            log.debug(String.format("Category created: %s", category));

            result.add(CategoryCreateResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .created(OffsetDateTime.now())
                    .createdBy(category.getCreatedBy())
                    .updated(category.getUpdated())
                    .updatedBy(category.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Updates multiple categories using a list of update requests
     * @param requests list of update requests
     * @return list of update responses
     * @throws BadRequestException if category with name already exists
     * @throws NotFoundException if category with id does not exist
     * */
    @Override
    @Transactional
    public List<CategoryUpdateResponse> updateCategory(List<CategoryUpdateRequest> requests) {
        final List<CategoryUpdateResponse> result = new ArrayList<>();

        for (CategoryUpdateRequest request : requests) {
            Category category = categoryRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist", request.getId())));

            if (request.getName() != null) {
                if (request.getName().isEmpty()) {
                    throw new BadRequestException("Category name cannot be empty");
                }

                if (categoryRepository.existsByNameExcludingId(request.getName(), request.getId())) {
                    throw new BadRequestException(String.format("Category with name %s already exists", request.getName()));
                }
                category.setName(request.getName());
            }

            category.setUpdated(OffsetDateTime.now());
            categoryRepository.save(category);
            log.debug(String.format("Category updated: %s", category));

            result.add(CategoryUpdateResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .created(category.getCreated())
                    .createdBy(category.getCreatedBy())
                    .updated(category.getUpdated())
                    .updatedBy(category.getUpdatedBy())
                    .build());

        }
        return result;
    }

    /**
     * Delete multiple categories using a list of ids
     * @param ids list of ids
     * @return list of delete responses
     * @throws NotFoundException if category with id does not exist
     * */
    @Override
    @Transactional
    public List<CategoryDeleteResponse> deleteCategory(List<Long> ids) {
        final List<CategoryDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist", id)));

            categoryRepository.delete(category);
            log.debug(String.format("Category deleted: %s", category));

            result.add(CategoryDeleteResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .created(category.getCreated())
                    .createdBy(category.getCreatedBy())
                    .updated(category.getUpdated())
                    .updatedBy(category.getUpdatedBy())
                    .build());
        }

        return result;
    }

    /**
     * Get all categories of a single category using an id
     * @param id id of category
     * @return list of get responses
     * @throws NotFoundException if category with id does not exist
     * */
    @Override
    public List<CategoryGetResponse> getCategories(Long id) {
        final List<CategoryGetResponse> result = new ArrayList<>();

        if (id != null) {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist", id)));

            result.add(CategoryGetResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .dishes(converter.toDishSimplifiedGetResponse(category.getDishes()))
                    .created(category.getCreated())
                    .createdBy(category.getCreatedBy())
                    .updated(category.getUpdated())
                    .updatedBy(category.getUpdatedBy())
                    .build());
        } else {
            for (Category category : categoryRepository.findAll()) {
                result.add(CategoryGetResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .dishes(converter.toDishSimplifiedGetResponse(category.getDishes()))
                        .created(category.getCreated())
                        .createdBy(category.getCreatedBy())
                        .updated(category.getUpdated())
                        .updatedBy(category.getUpdatedBy())
                        .build());
            }
        }
        return result;
    }
}
