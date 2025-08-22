package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.XuatXuEntity;

import jakarta.transaction.Transactional;

public interface XuatxuRepository extends JpaRepository<XuatXuEntity, Long> {

	Page<XuatXuEntity> findByTrash(String trash, Pageable pageable);
	
	List<XuatXuEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE XuatXuEntity p SET p.status = :status WHERE p.maXuatXu = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE XuatXuEntity p SET p.trash = :trash WHERE p.maXuatXu = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	boolean existsByTenXuatXuAllIgnoreCase(String ten);

}
