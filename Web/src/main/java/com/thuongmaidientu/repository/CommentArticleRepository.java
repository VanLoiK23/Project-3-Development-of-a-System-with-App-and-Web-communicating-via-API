package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.ArticleEntity;
import com.thuongmaidientu.entity.CommentAndRateEntity;
import com.thuongmaidientu.entity.CommentArticleEntity;
import com.thuongmaidientu.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface CommentArticleRepository extends JpaRepository<CommentArticleEntity, Integer> {

	@Query("""
			    SELECT p FROM CommentArticleEntity p
			    WHERE (:rate IS NULL OR (p.rating >= :rate AND p.rating < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	List<CommentArticleEntity> findAllByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st,
			Pageable pageable);

	@Query("""
			    SELECT COUNT(p) FROM CommentArticleEntity p
			    WHERE (:rate IS NULL OR (p.rating >= :rate AND p.rating < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	Long coundOfFindAllByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st);

	@Query("""
			    SELECT COUNT(p) FROM CommentArticleEntity p
			    WHERE (:rate IS NULL OR (p.rating >= :rate AND p.rating < (:rate + 1)))
			    AND (:st IS NULL OR p.feedback = :st)
			""")
	int countByOptionalFilters(@Param("rate") Integer rate, @Param("st") Integer st);

	@Modifying
	@Transactional
	@Query(value = "UPDATE commentcomm_for_articles p SET p.feedback = :feeback,p.content_feedback = :feeback_content,p.nhanvien = :nhanvien,p.ngayphanhoi = :ngayphanhoi WHERE p.id = :id", nativeQuery = true)
	int updateFeeback(@Param("id") int id, @Param("feeback") Integer feeback,
			@Param("feeback_content") String feeback_content, @Param("nhanvien") String nhanvien,
			@Param("ngayphanhoi") Date ngayphanhoi);

	@Modifying
	@Transactional
	@Query(value = "UPDATE commentcomm_for_articles p SET p.content = :content,p.rating = :rate  WHERE p.id = :id", nativeQuery = true)
	int updateReview(@Param("id") int id, @Param("content") String content, @Param("rate") Double rate);

	List<CommentArticleEntity> findByArticle(ArticleEntity article);

	@Modifying
	@Transactional
	void deleteByArticle(ArticleEntity article);

}
