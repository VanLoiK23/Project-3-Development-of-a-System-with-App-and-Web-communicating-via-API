package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.entity.KhuVucKhoEntity;
import com.thuongmaidientu.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface KhuVucKhoRepository extends JpaRepository<KhuVucKhoEntity, Long> {

	Page<KhuVucKhoEntity> findByTrash(String trash, Pageable pageable);

	List<KhuVucKhoEntity> findByTrash(String trash);
	
	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE KhuVucKhoEntity p SET p.status = :status WHERE p.maKhuVuc = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE KhuVucKhoEntity p SET p.trash = :trash WHERE p.maKhuVuc = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);
	
	boolean existsByTenKhuVucAllIgnoreCase(String th);
	

}
