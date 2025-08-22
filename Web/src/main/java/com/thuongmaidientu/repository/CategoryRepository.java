package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.CategoryEntity;

import jakarta.transaction.Transactional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	Page<CategoryEntity> findByTrash(String trash, Pageable pageable);
	
	List<CategoryEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE CategoryEntity p SET p.status = :status WHERE p.maThuongHieu = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE CategoryEntity p SET p.trash = :trash WHERE p.maThuongHieu = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	boolean existsByTenThuongHieuAllIgnoreCase(String th);
	
	@Query(value="SELECT image FROM thuonghieu WHERE mathuonghieu = :id",nativeQuery = true)
	String findImageByTenMauSac(@Param("id") int ma);
	
	List<CategoryEntity> findByTenThuongHieu(String tenThuongHieu);

}
