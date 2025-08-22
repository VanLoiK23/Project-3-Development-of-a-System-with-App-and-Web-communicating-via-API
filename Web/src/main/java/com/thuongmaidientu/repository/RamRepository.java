package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.RamEntity;

import jakarta.transaction.Transactional;

public interface RamRepository extends JpaRepository<RamEntity, Long> {

	Page<RamEntity> findByTrash(String trash, Pageable pageable);

	List<RamEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Query("SELECT p.maDungLuongRam FROM RamEntity p WHERE p.kichThuocRam = :r")
	Integer selectIdByValue(@Param("r") int r);
	
	@Modifying
	@Transactional
	@Query("UPDATE RamEntity p SET p.status = :status WHERE p.maDungLuongRam = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE RamEntity p SET p.trash = :trash WHERE p.maDungLuongRam = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	boolean existsByKichThuocRamAllIgnoreCase(Integer ram);

}
