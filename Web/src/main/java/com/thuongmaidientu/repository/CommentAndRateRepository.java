package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.CommentAndRateEntity;
import com.thuongmaidientu.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface CommentAndRateRepository extends JpaRepository<CommentAndRateEntity, Integer> {

	@Query("""
			    SELECT p FROM CommentAndRateEntity p
			    WHERE (:rate IS NULL OR (p.rate >= :rate AND p.rate < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	List<CommentAndRateEntity> findAllByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st,
			Pageable pageable);

	@Query("""
			    SELECT COUNT(p) FROM CommentAndRateEntity p
			    WHERE (:rate IS NULL OR (p.rate >= :rate AND p.rate < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	Long coundOfFindAllByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st);

	@Query("""
			    SELECT COUNT(p) FROM CommentAndRateEntity p
			    WHERE (:rate IS NULL OR (p.rate >= :rate AND p.rate < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	int countByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st);

	@Modifying
	@Transactional
	@Query(value = "UPDATE comment_and_rate p SET p.feeback = :feeback,p.feeback_content = :feeback_content,p.nhanvien = :nhanvien,p.ngayphanhoi = :ngayphanhoi WHERE p.id = :id", nativeQuery = true)
	int updateFeeback(@Param("id") int id, @Param("feeback") Integer feeback,
			@Param("feeback_content") String feeback_content, @Param("nhanvien") String nhanvien,
			@Param("ngayphanhoi") Date ngayphanhoi);

	@Modifying
	@Transactional
	@Query(value = "UPDATE comment_and_rate p SET p.content = :content,p.rate = :rate,p.img = :img  WHERE p.id = :id", nativeQuery = true)
	int updateReview(@Param("id") int id, @Param("content") String content, @Param("rate") Double rate,
			@Param("img") String img);

	List<CommentAndRateEntity> findByProduct(ProductEntity product);

	@Modifying
	@Transactional
	void deleteByProduct(ProductEntity product);
	
	@Query(value="""
		   SELECT cm.* 
                FROM comment_and_rate as cm
                WHERE cm.id_sp = ?1 AND cm.order_id = ?2 AND cm.id_user = ?3
		""",nativeQuery = true)
	List<CommentAndRateEntity> findByProductIdAndOrderId(Integer masp,Integer orderId,Integer clientId);
}
