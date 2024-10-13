package com.iamnirvan.restaurant.core.repositories;

import com.iamnirvan.restaurant.core.models.entities.Address;
import com.iamnirvan.restaurant.core.models.responses.address.AddressGetResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAddressByCustomerId(Long customerId);
    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.isMain = true")
    Address getMainAddress(Long customerId);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.address.AddressGetResponse(a.id, a.province, a.city, a.street, a.buildingNumber, a.isMain) FROM Address a WHERE a.customer.id = ?1")
    List<AddressGetResponse> findAllAddressesByCustomerId(Long customerId);
    @Query("SELECT new com.iamnirvan.restaurant.core.models.responses.address.AddressGetResponse(a.id, a.province, a.city, a.street, a.buildingNumber, a.isMain) FROM Address a WHERE a.id = ?1")
    Optional<AddressGetResponse> findAddressById(Long id);
}
