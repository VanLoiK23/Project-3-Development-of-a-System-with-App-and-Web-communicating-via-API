package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.thuongmaidientu.entity.ChiTietPhieuXuatEntity;
import com.thuongmaidientu.entity.ChiTietPhieuXuatKey;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;

public interface ChitietPXRepository extends JpaRepository<ChiTietPhieuXuatEntity,ChiTietPhieuXuatKey> {
	@Modifying
	@Transactional
	@Query(value="DELETE FROM chitietphieuxuat  WHERE maphienbansp = :maPhienBanSP",nativeQuery = true)
	void deleteByMaPhienBanSP(@Param("maPhienBanSP") Integer maPhienBanSP);
	
	
	@Query(value="SELECT COALESCE(SUM(soluong), 0) FROM chitietphieuxuat WHERE maphienbansp = :ma",nativeQuery = true)
	Integer countSLSOLD(@Param("ma") Integer maPB);
	
	@Query(value = "SELECT COALESCE(SUM(soluong), 0) FROM chitietphieuxuat WHERE maphienbansp = :ma AND maphieuxuat=:pn", nativeQuery = true)
	Integer countSLXuatByMaPBANDMaPN(@Param("ma") Integer maPB, @Param("pn") Integer maPN);
	
	@Query(value = "SELECT * FROM chitietphieuxuat WHERE maphieuxuat = :id", nativeQuery = true)
	List<ChiTietPhieuXuatEntity> getListCTPX(@Param("id") Integer id);
	
	@Modifying
	@Transactional
	void deleteByPhienBanSanPhamXuat(PhienBanSanPhamEntity phienBanSanPhamXuat);
	
	long countByPhieuXuat(PhieuXuatEntity phieuXuat);
	
	List<ChiTietPhieuXuatEntity> getByPhienBanSanPhamXuat(PhienBanSanPhamEntity phienBanSanPhamXuat);
}
