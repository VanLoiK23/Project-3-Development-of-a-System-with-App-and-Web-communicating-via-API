package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.RomEntity;

import jakarta.transaction.Transactional;

public interface RomRepository extends JpaRepository<RomEntity, Long> {

	Page<RomEntity> findByTrash(String trash, Pageable pageable);
	
	List<RomEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Query("SELECT p.maDungLuongRom FROM RomEntity p WHERE p.kichThuocRom = :r")
	Integer selectIdByValue(@Param("r") int r);

	@Modifying
	@Transactional
	@Query("UPDATE RomEntity p SET p.status = :status WHERE p.maDungLuongRom = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE RomEntity p SET p.trash = :trash WHERE p.maDungLuongRom = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);

	boolean existsByKichThuocRomAllIgnoreCase(Integer rom);

}
