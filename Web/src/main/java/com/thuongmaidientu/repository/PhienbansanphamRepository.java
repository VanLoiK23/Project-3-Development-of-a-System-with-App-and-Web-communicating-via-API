package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface PhienbansanphamRepository extends JpaRepository<PhienBanSanPhamEntity, Long> {

//	@Query("SELECT pb FROM PhienBanSanPhamEntity pb "
//			+ "LEFT JOIN RamEntity ram ON pb.ram.maDungLuongRam = ram.maDungLuongRam "
//			+ "LEFT JOIN RomEntity rom ON pb.rom.maDungLuongRom = rom.maDungLuongRom "
//			+ "LEFT JOIN MauSacEntity mausac ON pb.mauSac.maMauSac = mausac.maMauSac " + "WHERE pb.sanPham.id = :masp "
//			+ "AND (ram.status = 1 OR ram.status IS NULL) " + "AND (rom.status = 1 OR rom.status IS NULL) "
//			+ "AND (mausac.status = 1 OR mausac.status IS NULL) " + "AND (ram.trash = 'active' OR ram.trash IS NULL) "
//			+ "AND (rom.trash = 'active' OR rom.trash IS NULL) "
//			+ "AND (mausac.trash = 'active' OR mausac.trash IS NULL)")
//	List<PhienBanSanPhamEntity> selectAllPhienBanSP(@Param("masp") int id);
	
	@Query("SELECT pb FROM PhienBanSanPhamEntity pb "
			+ "LEFT JOIN RamEntity ram ON pb.ram.maDungLuongRam = ram.maDungLuongRam "
			+ "LEFT JOIN RomEntity rom ON pb.rom.maDungLuongRom = rom.maDungLuongRom "
			+ "LEFT JOIN MauSacEntity mausac ON pb.mauSac.maMauSac = mausac.maMauSac " + "WHERE pb.sanPham.id = :masp "
			+ "AND (ram.status = 1 OR ram.status IS NULL) " + "AND (rom.status = 1 OR rom.status IS NULL) "
			+ "AND (mausac.status = 1 OR mausac.status IS NULL) " + "AND (ram.trash = 'active' OR ram.trash IS NULL) "
			+ "AND (rom.trash = 'active' OR rom.trash IS NULL) "
			+ "AND (mausac.trash = 'active' OR mausac.trash IS NULL)")
	List<PhienBanSanPhamEntity> selectAllPhienBanSP(@Param("masp") int id);

	@Query(value = "SELECT pb.maphienbansp FROM phienbansanpham pb WHERE pb.masp=:masp", nativeQuery = true)
	List<Integer> selectAllMaphienbanSPByMaSP(@Param("masp") int masp);

	@Query(value = "SELECT version.maphienbansp " + " FROM phienbansanpham AS version "
			+ " LEFT JOIN dungluongram AS ram ON version.ram = ram.madungluongram "
			+ " LEFT JOIN dungluongrom AS rom ON version.rom = rom.madungluongrom "
			+ " LEFT JOIN mausac AS color ON version.mausac = color.mamausac "
			+ " WHERE (ram.madungluongram = :ramId OR ram.madungluongram IS NULL) "
			+ " AND (rom.madungluongrom = :romId OR rom.madungluongrom IS NULL) "
			+ " AND (color.mamausac = :colorId OR color.mamausac IS NULL) "
			+ " AND version.masp = :productId", nativeQuery = true)
	Long findVariantId(@Param("productId") Long productId, @Param("romId") Integer romId, @Param("ramId") Integer ramId,
			@Param("colorId") Integer colorId);

	@Query(value = "SELECT version.masp " + " FROM phienbansanpham AS version "
			+ " WHERE version.maphienbansp=:productId ", nativeQuery = true)
	Integer findMaSP(@Param("productId") Integer productId);

	@Query(value = "SELECT SUM(soluongton) FROM phienbansanpham WHERE masp = :masp", nativeQuery = true)
	Integer countNumberByMaPhienBanSP(@Param("masp") Integer masp);

	@Modifying
	@Transactional
	@Query(value = "UPDATE phienbansanpham SET " + "rom = COALESCE(:romId, rom), " + "ram = COALESCE(:ramId, ram), "
			+ "mausac = COALESCE(:colorId, mausac), " + "gianhap = COALESCE(:gianhap, gianhap), "
			+ "giaxuat = COALESCE(:giaxuat, giaxuat), " + "sale = COALESCE(:sale, sale), "
			+ "price_sale = COALESCE(:priceSale, price_sale) "
			+ "WHERE maphienbansp = :maphienbansp", nativeQuery = true)
	void updatePBSP(@Param("maphienbansp") Long maphienbansp, @Param("romId") Integer romId,
			@Param("ramId") Integer ramId, @Param("colorId") Integer colorId, @Param("gianhap") Integer gianhap,
			@Param("giaxuat") Integer giaxuat, @Param("sale") Float sale, @Param("priceSale") Integer priceSale);

	@Modifying
	@Transactional
	@Query(value = "UPDATE phienbansanpham SET " + "soluongton = soluongton + :soluong "
			+ "WHERE maphienbansp = :maphienbansp", nativeQuery = true)
	void updateSLPBSP(@Param("maphienbansp") Long maphienbansp, @Param("soluong") Integer soluong);

	@Modifying
	@Transactional
	@Query(value = "UPDATE phienbansanpham SET " + "soluongton = :soluong "
			+ "WHERE maphienbansp = :maphienbansp", nativeQuery = true)
	void setUpSLPBSP(@Param("maphienbansp") Long maphienbansp, @Param("soluong") Integer soluong);

	
	@Modifying
	@Transactional
	void deleteBySanPham(ProductEntity sanPham);

//	@Query(value = "SELECT pb.* FROM phienbansanpham pb " + "LEFT JOIN dungluongram ram ON ram.madungluongram = pb.ram "
//			+ "LEFT JOIN dungluongrom rom ON rom.madungluongrom = pb.rom "
//			+ "LEFT JOIN mausac color ON color.mamausac = pb.mausac " + "WHERE pb.masp = :masp "
//			+ "AND (:ram IS NULL OR ram.kichthuocram = :ram) " + "AND (:rom IS NULL OR rom.kichthuocrom = :rom) "
//			+ "AND (:color IS NULL OR color.tenmausac LIKE LOWER(CONCAT('%', :color, '%')))", nativeQuery = true)
//	List<PhienBanSanPhamEntity> findByConfig(@Param("masp") int id, @Param("ram") Integer ram,
//			@Param("rom") Integer rom, @Param("color") String color);

}
