package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.SlideEntity;

import jakarta.transaction.Transactional;

public interface SlideRepository extends JpaRepository<SlideEntity, Long> {

	Page<SlideEntity> findByTrash(String trash, Pageable pageable);
	
	List<SlideEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE SlideEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE SlideEntity p SET p.trash = :trash WHERE p.id = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
}
