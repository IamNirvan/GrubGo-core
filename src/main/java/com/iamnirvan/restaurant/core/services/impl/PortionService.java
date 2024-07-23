package com.iamnirvan.restaurant.core.services.impl;

import com.iamnirvan.restaurant.core.exceptions.BadRequestException;
import com.iamnirvan.restaurant.core.exceptions.NotFoundException;
import com.iamnirvan.restaurant.core.models.entities.Portion;
import com.iamnirvan.restaurant.core.models.requests.portion.PortionCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.portion.PortionUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionGetResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionUpdateResponse;
import com.iamnirvan.restaurant.core.repositories.DishPortionRepository;
import com.iamnirvan.restaurant.core.repositories.PortionRepository;
import com.iamnirvan.restaurant.core.services.IPortionService;
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
public class PortionService implements IPortionService {
    private final PortionRepository portionRepository;
    private final DishPortionRepository dishPortionRepository;

    /**
     * Create multiple portions using a list of create requests
     *
     * @param requests list of create requests
     * @return list of create responses
     * @throws BadRequestException if portion with name already exists
     */
    @Override
    public List<PortionCreateResponse> createPortion(List<PortionCreateRequest> requests) throws BadRequestException {
        final List<PortionCreateResponse> result = new ArrayList<>();

        for (PortionCreateRequest request : requests) {
            if (portionRepository.existsByName(request.getName())) {
                throw new BadRequestException(String.format("Portion with name %s already exists", request.getName()));
            }

            Portion portion = Portion.builder()
                    .name(request.getName())
                    .created(OffsetDateTime.now())
                    .build();

            portionRepository.save(portion);
            log.debug(String.format("Portion created: %s", portion));

            result.add(PortionCreateResponse.builder()
                    .id(portion.getId())
                    .name(portion.getName())
                    .createdBy(portion.getCreatedBy())
                    .created(portion.getCreated())
                    .updatedBy(portion.getUpdatedBy())
                    .updated(portion.getUpdated())
                    .build());
        }
        return result;
    }

    /**
     * Update multiple portions using a list of update requests
     *
     * @param requests list of update requests
     * @return list of update responses
     * @throws NotFoundException if portion with id does not exist
     * @throws BadRequestException if portion name is empty or already exists
     */
    @Override
    public List<PortionUpdateResponse> updatePortion(List<PortionUpdateRequest> requests) throws NotFoundException, BadRequestException {
        final List<PortionUpdateResponse> result = new ArrayList<>();

        for (PortionUpdateRequest request : requests) {
            Portion portion = portionRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException(String.format("Portion with id %s does not exist", request.getId())));

            if (request.getName() != null) {
                if (request.getName().isEmpty()) {
                    throw new BadRequestException("Portion name cannot be empty");
                }

                if (portionRepository.existsByName(request.getName(), request.getId())) {
                    throw new BadRequestException(String.format("Portion with name %s already exists", request.getName()));
                }

                portion.setName(request.getName());
            }
            portion.setUpdated(OffsetDateTime.now());
            portionRepository.save(portion);
            log.debug(String.format("Portion updated: %s", portion));

            result.add(PortionUpdateResponse.builder()
                    .id(portion.getId())
                    .name(portion.getName())
                    .created(portion.getCreated())
                    .createdBy(portion.getCreatedBy())
                    .updated(portion.getUpdated())
                    .updatedBy(portion.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Delete multiple portions using a list of ids
     *
     * @param ids list of ids
     * @return list of delete responses
     * @throws NotFoundException if portion with id does not exist
     */
    @Override
    @Transactional
    public List<PortionDeleteResponse> deletePortion(List<Long> ids) throws NotFoundException {
        final List<PortionDeleteResponse> result = new ArrayList<>();

        for (Long id : ids) {
            Portion portion = portionRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(String.format("Portion with id %s does not exist", id)));

            portionRepository.delete(portion);
            log.debug(String.format("Portion deleted: %s", portion));

            if (dishPortionRepository.existsByPortionId(id)) {
                throw new BadRequestException(String.format("Cannot delete portion with id %s because it is used by dishes", id));
            }

            result.add(PortionDeleteResponse.builder()
                    .id(portion.getId())
                    .name(portion.getName())
                    .created(portion.getCreated())
                    .createdBy(portion.getCreatedBy())
                    .updated(portion.getUpdated())
                    .updatedBy(portion.getUpdatedBy())
                    .build());
        }
        return result;
    }

    /**
     * Get portions
     *
     * @param id id
     * @return list of get responses
     * @throws NotFoundException if portion with id does not exist
     */
    @Override
    public List<PortionGetResponse> getPortions(Long id) throws NotFoundException {
        final List<PortionGetResponse> result = new ArrayList<>();
        if (id != null) {
            Portion address = portionRepository.findById(id).orElseThrow(() ->
                    new NotFoundException((String.format("Portion with id %s does not exist", id))));
            result.add(PortionGetResponse.builder()
                    .id(address.getId())
                    .name(address.getName())
                    .createdBy(address.getCreatedBy())
                    .created(address.getCreated())
                    .updatedBy(address.getUpdatedBy())
                    .updated(address.getUpdated())
                    .build());
        } else {
            for (Portion portion : portionRepository.findAll()) {
                result.add(PortionGetResponse.builder()
                        .id(portion.getId())
                        .name(portion.getName())
                        .createdBy(portion.getCreatedBy())
                        .created(portion.getCreated())
                        .updatedBy(portion.getUpdatedBy())
                        .updated(portion.getUpdated())
                        .build());
            }
        }
        return result;
    }
}
