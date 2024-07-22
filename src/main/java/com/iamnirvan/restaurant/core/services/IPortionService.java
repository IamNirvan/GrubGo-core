package com.iamnirvan.restaurant.core.services;

import com.iamnirvan.restaurant.core.models.requests.portion.PortionCreateRequest;
import com.iamnirvan.restaurant.core.models.requests.portion.PortionUpdateRequest;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionCreateResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionDeleteResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionGetResponse;
import com.iamnirvan.restaurant.core.models.responses.portion.PortionUpdateResponse;

import java.util.List;

public interface IPortionService {
    /**
     * Create multiple portions using a list of create requests
     * @param requests list of create requests
     * @return list of create responses
     * */
    List<PortionCreateResponse> createPortion(List<PortionCreateRequest> requests);

    /**
     * Update multiple portions using a list of update requests
     * @param requests list of update requests
     * @return list of update responses
     * */
    List<PortionUpdateResponse> updatePortion(List<PortionUpdateRequest> requests);

    /**
     * Delete multiple portions using a list of ids
     * @param ids list of ids
     * @return list of delete responses
     * */
    List<PortionDeleteResponse> deletePortion(List<Long> ids);

    /**
     * Get portions
     * @param id id
     * @return list of get responses
     * */
    List<PortionGetResponse> getPortions(Long id);
}
