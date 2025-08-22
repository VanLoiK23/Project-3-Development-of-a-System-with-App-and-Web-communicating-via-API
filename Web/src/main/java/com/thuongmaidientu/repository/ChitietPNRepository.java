package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.thuongmaidientu.entity.ChiTietPhieuNhapEntity;
import com.thuongmaidientu.entity.ChiTietPhieuNhapKey;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.PhieuNhapEntity;

public interface ChitietPNRepository extends JpaRepository<ChiTietPhieuNhapEntity, ChiTietPhieuNhapKey> {

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM chitietphieunhap  WHERE maphienbansp = :maPhienBanSP", nativeQuery = true)
	void deleteByMaPhienBanSP(@Param("maPhienBanSP") Integer maPhienBanSP);

	@Query(value = "SELECT COALESCE(SUM(soluong), 0) FROM chitietphieunhap WHERE maphienbansp = :ma", nativeQuery = true)
	Integer countSLNhap(@Param("ma") Integer maPB);

	@Query(value = "SELECT COALESCE(SUM(soluong), 0) FROM chitietphieunhap WHERE maphienbansp = :ma AND maphieunhap=:pn", nativeQuery = true)
	Integer countSLNhapByMaPBANDMaPN(@Param("ma") Integer maPB, @Param("pn") Integer maPN);
	
	@Query(value = "SELECT * FROM chitietphieunhap WHERE maphieunhap = :id", nativeQuery = true)
	List<ChiTietPhieuNhapEntity> getListCTPN(@Param("id") Integer id);
	
	@Modifying
	@Transactional
	void deleteByPhienBanSanPhamNhap(PhienBanSanPhamEntity phienBanSanPhamNhap);
	
	long countByPhieunhap(PhieuNhapEntity phieunhap);
	
	List<ChiTietPhieuNhapEntity> getByPhienBanSanPhamNhap(PhienBanSanPhamEntity phienBanSanPhamNhap);
}
