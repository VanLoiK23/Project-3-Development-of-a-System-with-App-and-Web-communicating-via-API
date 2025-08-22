package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.PhieuNhapEntity;

import jakarta.transaction.Transactional;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhapEntity, Long> {
	List<PhieuNhapEntity> findBySave(String save);

	Page<PhieuNhapEntity> findBySave(String trash, Pageable pageable);

	long countBySave(String save);

	@Modifying
	@Transactional
	@Query("UPDATE PhieuNhapEntity p SET p.save = :trash WHERE p.id = :id")
	int updateSaveById(@Param("id") int id, @Param("trash") String status);

	@Query("SELECT p FROM PhieuNhapEntity p WHERE DATE(p.thoiGian) >= :fromDate  AND p.save='active' ")
	List<PhieuNhapEntity> findAllByDate(@Param("fromDate") Date fromDate, Pageable pageable);

	@Query("SELECT COUNT(p) FROM PhieuNhapEntity p WHERE DATE(p.thoiGian) >= :fromDate  AND p.save='active'")
	int countByDate(@Param("fromDate") Date fromDate);

	@Query(value = "SELECT MONTH(p.thoigian), SUM(p.tongtien) FROM phieunhap p WHERE YEAR(p.thoigian) = YEAR(CURRENT_DATE) GROUP BY MONTH(p.thoigian)", nativeQuery = true)
	List<Object[]> getTongPhieuNhapTheoThang();

	@Query(value = "SELECT QUARTER(thoigian) AS quarter, SUM(tongtien) AS total " + "FROM phieunhap "
			+ "WHERE YEAR(thoigian) = YEAR(CURDATE()) " + "GROUP BY QUARTER(thoigian)", nativeQuery = true)
	List<Object[]> getPhieuNhapTheoQuy();

}
