package com.example.app.ws.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.app.ws.io.entity.AddressEntity;
import com.example.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {
	Iterable<AddressEntity> findAllByUserDetails(UserEntity userEntity);
}
