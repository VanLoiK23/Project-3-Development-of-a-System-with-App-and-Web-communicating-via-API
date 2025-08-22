package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
	List<CartEntity> findByStatus(String status);

	@Query(value = "SELECT id FROM cart WHERE idkh=:idClient AND status ='active'",nativeQuery = true)
	Integer findIdCartActiveByClient(@Param("idClient") Integer idKH);
}
