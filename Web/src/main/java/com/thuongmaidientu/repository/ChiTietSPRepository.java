package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.thuongmaidientu.entity.CTSanPhamEntity;

public interface ChiTietSPRepository extends JpaRepository<CTSanPhamEntity, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ctsanpham  WHERE maphienbansp = :maPhienBanSP", nativeQuery = true)
	void deleteByMaPhienBanSP(@Param("maPhienBanSP") Integer maPhienBanSP);

	@Query(value = "SELECT maimei FROM ctsanpham "
			+ "WHERE maphienbansp = :mapb AND tinhtrang = 0 AND maphieuxuat IS NULL "
			+ "ORDER BY maimei ASC", nativeQuery = true)
	List<Long> findImeisByPhienBan(@Param("mapb") Integer maPhienBanSP, Pageable pageable);
	
	@Query(value = "SELECT maimei FROM ctsanpham "
			+ "WHERE maphienbansp = :mapb AND tinhtrang = 1 AND maphieuxuat =:mapx "
			+ "ORDER BY maimei ASC", nativeQuery = true)
	List<Long> findImeisByPhienBanForCancle(@Param("mapx") Integer orderId,@Param("mapb") Integer maPhienBanSP);

	@Modifying
	@Transactional
	@Query(value = "UPDATE ctsanpham SET maphieuxuat=?1, tinhtrang=1 WHERE maphienbansp=?2 AND maimei=?3", nativeQuery = true)
	void updateCTSP(int mapx, int mapb, Long imei);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE ctsanpham SET maphieuxuat = NULL, tinhtrang=0 WHERE maphienbansp=?2 AND maimei=?3 AND maphieuxuat=?1 AND tinhtrang=1", nativeQuery = true)
	void updateCTSPForCancle(int mapx, int mapb, Long imei);

	@Query(value = """
						SELECT COUNT(*) FROM ctsanpham as ct LEFT JOIN phienbansanpham as version ON version.maphienbansp=ct.maphienbansp
			LEFT JOIN sanpham as sp ON sp.masp=version.masp
			WHERE sp.masp=?1 AND ct.tinhtrang=0 AND version.maphienbansp=?2
						""", nativeQuery = true)
	Integer getQuantityVersionProductInStock(Integer masp, Integer maPBSP);

	@Query(value = """
						SELECT COUNT(*) FROM ctsanpham as ct LEFT JOIN phienbansanpham as version ON version.maphienbansp=ct.maphienbansp
			LEFT JOIN sanpham as sp ON sp.masp=version.masp
			WHERE sp.masp=?1 AND ct.tinhtrang=?2
						""", nativeQuery = true)
	Integer getQuantityProductByStatus(Integer masp, Integer status);
}
