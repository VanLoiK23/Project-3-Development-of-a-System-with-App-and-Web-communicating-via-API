package com.thuongmaidientu.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.KhachHangEntity;

import jakarta.transaction.Transactional;

public interface KhachHangRepository extends JpaRepository<KhachHangEntity, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE KhachHangEntity p SET p.trangThai = :status WHERE p.maKhachHang = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	boolean existsByHoTenAllIgnoreCase(String th);

	@Modifying
	@Transactional
	@Query(value = "UPDATE khachhang p SET p.hoten = ?1,p.gioitinh=?2,p.ngaysinh=?3,p.sdt=?4,p.email=?5 WHERE p.makh = ?6", nativeQuery = true)
	int update(String name, String gioitinh, Date ngaySinh, String sdt, String email, int id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE khachhang p SET p.hoten = ?1,p.ngaysinh=?2,p.sdt=?3,p.email=?4 WHERE p.makh = ?5", nativeQuery = true)
	int updateFromAndroid(String name, Date ngaySinh, String sdt, String email, int id);

//Page<NhaCungCapEntity> findByTrash(String trash, Pageable pageable);
//	
//	List<NhaCungCapEntity> findByTrash(String trash);
//
//	long countByTrash(String trash);
//
//	@Modifying
//	@Transactional
//	@Query("UPDATE NhaCungCapEntity p SET p.status = :status WHERE p.id = :id")
//	int updateStatusById(@Param("id") int id, @Param("status") int status);
//
//	@Modifying
//	@Transactional
//	@Query("UPDATE NhaCungCapEntity p SET p.trash = :trash WHERE p.id = :id")
//	int updateTrashById(@Param("id") int id, @Param("trash") String status);
//	
//	boolean existsByTenNhaCungCapAllIgnoreCase(String th);

}
