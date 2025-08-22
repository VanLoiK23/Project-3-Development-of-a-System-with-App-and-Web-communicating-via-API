package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.MauSacEntity;

import jakarta.transaction.Transactional;

public interface ColorRepository extends JpaRepository<MauSacEntity, Long> {

	Page<MauSacEntity> findByTrash(String trash, Pageable pageable);
	
	List<MauSacEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Query("SELECT p.maMauSac FROM MauSacEntity p WHERE p.tenMauSac = :color")
	Integer selectIdByValue(@Param("color") String color);

	@Modifying
	@Transactional
	@Query("UPDATE MauSacEntity p SET p.status = :status WHERE p.maMauSac = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE MauSacEntity p SET p.trash = :trash WHERE p.maMauSac = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	Integer findMaMauSacByTenMauSac(String color);
	
	List<MauSacEntity> findByTenMauSacContaining(String keyword);
	
	boolean existsByTenMauSac(String color);
	
	Long countByTenMauSac(String color);

	List<MauSacEntity> findByTenMauSacOrderByMaMauSacDesc(String color);

	MauSacEntity findTopByOrderByMaMauSacDesc();



}
