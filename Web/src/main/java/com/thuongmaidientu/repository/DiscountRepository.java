package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.DiscountEntity;
import com.thuongmaidientu.entity.XuatXuEntity;

import jakarta.transaction.Transactional;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer>{


	Page<DiscountEntity> findByTrash(String trash, Pageable pageable);
	
	List<DiscountEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE DiscountEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE DiscountEntity p SET p.trash = :trash WHERE p.id = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	boolean existsByCodeAllIgnoreCase(String code);
	
	@Modifying
	@Transactional
	@Query("UPDATE DiscountEntity p SET p.numberUsed = p.numberUsed - 1 WHERE p.id = :id")
	void decrese(@Param("id") int id);
}
