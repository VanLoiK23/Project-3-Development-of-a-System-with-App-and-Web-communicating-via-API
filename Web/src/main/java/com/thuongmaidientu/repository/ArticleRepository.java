package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.ArticleEntity;

import jakarta.transaction.Transactional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

	Page<ArticleEntity> findByTrash(String trash, Pageable pageable);

	List<ArticleEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Query("SELECT p.id FROM ArticleEntity p WHERE p.title = :title")
	Integer selectIdByValue(@Param("title") String title);

	@Modifying
	@Transactional
	@Query("UPDATE ArticleEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE ArticleEntity p SET p.trash = :trash WHERE p.id = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);

	Integer findIdByTitle(String title);

	List<ArticleEntity> findByTitleContaining(String keyword);

	boolean existsByTitleAllIgnoreCase(String title);

	@Query(value = "SELECT MIN(id) FROM articles WHERE id > :id AND status=1 AND trash='active'", nativeQuery = true)
	Integer getNextIdArticle(@Param("id") Integer id);

	@Query(value = "SELECT MAX(id) FROM articles WHERE id < :id AND status=1 AND trash='active'", nativeQuery = true)
	Integer getPreviousIdArticle(@Param("id") Integer id);

	@Query(value = "SELECT MIN(id) FROM articles  WHERE status=1 AND trash='active'", nativeQuery = true)
	Integer getMinID();

	@Query(value = "SELECT MAX(id) FROM articles  WHERE status=1 AND trash='active'", nativeQuery = true)
	Integer getMaxID();

}
