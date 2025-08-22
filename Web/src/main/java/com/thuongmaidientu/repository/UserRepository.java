package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.UserEntity;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Page<UserEntity> findByStatusNotLikeAndRole(String status, String role, Pageable pageable);

	List<UserEntity> findByStatusNotLike(String status);

	List<UserEntity> findByStatus(String status);

	long countByStatus(String status);

	@Modifying
	@Transactional
	@Query("UPDATE UserEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") String status);

	List<UserEntity> findByUsername(String username);

	@Modifying
	@Transactional
	@Query(value = "UPDATE taikhoan p SET p.name = ?1,p.username=?2,p.phone=?3,p.email=?4,p.ngaysinh=?5,p.gender=?6 WHERE p.id = ?7", nativeQuery = true)
	int update(String name, String username, String phone, String email, Date ngaySinh, String gender, int id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE taikhoan p SET p.name = ?1,p.username=?2,p.phone=?3,p.email=?4,p.ngaysinh=?5,p.role=?6,p.status=?7 WHERE p.id = ?8", nativeQuery = true)
	int updateFromAndroid(String name, String username, String phone, String email, Date ngaySinh, String role,
			String status, int id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE taikhoan p SET p.fcm_tokens = ?1 WHERE p.id = ?2", nativeQuery = true)
	void updateFCMtoken(String token, int id);

	@Query(value = "SELECT p.fcm_tokens FROM  taikhoan p WHERE p.id = ?1", nativeQuery = true)
	String getTokenUser(int id);

	long countByRole(String role);
	
	boolean existsByNameAllIgnoreCase(String name);
	
	boolean existsByEmailAllIgnoreCase(String email);

	@Query(value = """
			SELECT COUNT(*) FROM taikhoan WHERE
			(:email IS NULL OR email = :email) AND
			(:phone IS NULL OR phone = :phone) AND
			role='khách hàng' AND status = 'lock'
			""", nativeQuery = true)
		int countLockedAccount(@Param("email") String email, @Param("phone") String phone);

	List<UserEntity> findByEmail(String email);
	
	List<UserEntity> findByResetTokenHash(String resetTokenHash);
}
