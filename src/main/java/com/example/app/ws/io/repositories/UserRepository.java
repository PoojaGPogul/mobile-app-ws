package com.example.app.ws.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.ws.io.entity.UserEntity;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findByEmailVerificationToken(String token);
	
	//Native SQL queries
	@Query(value="Select * from users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			countQuery = "Select count(*) from users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
	
	//Positional parameters
	@Query(value = "Select * from users u where u.first_name = ?1", nativeQuery = true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	//Named parameters
	@Query(value = "Select * from users u where u.last_name = :lastName", nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);
	
	//LIKE expression
	@Query(value = "Select * from users where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);
		
	//Select specific columns
	@Query(value = "Select u.first_name, u.last_name from users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
	List<Object[]> findUserFirstnameAndLastnameByKeyword(@Param("keyword") String keyword);
	
	//Update SQL query
	@Transactional
	@Modifying
	@Query(value = "update users u set u.email_verification_status = :emailVerificationStatus where u.user_id= :userId",
			nativeQuery = true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
											@Param("userId") String userId);
	
	
	//JPQL(Java persistence query language) select SQL query
	@Query("select user from UserEntity user where user.userId = :userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	//JPQL to select specific fields only
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
	List<Object[]> findUserEntityFullNameByUserId(@Param("userId") String userId);
		
	//JPQL update sqL query
	@Transactional
	@Modifying
	@Query("UPDATE UserEntity u set u.emailVerificationStatus = :emailVerificationStatus where u.userId = :userId")
	void updateUserEntityEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId") String userId);
	
	
}
