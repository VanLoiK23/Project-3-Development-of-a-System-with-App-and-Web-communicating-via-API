package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.dto.CommentArticleDTO;

public interface ICommentArticleService {
	List<CommentArticleDTO> findAll(Pageable pageable);

	List<CommentArticleDTO> selectAll();

	CommentArticleDTO findById(int id);

	CommentArticleDTO save(CommentArticleDTO ncc);

	int getTotalItem();

	void delete(int id);
	
	List<CommentArticleDTO> findAllByRateAndStatus(Integer rate, Integer st, Pageable pageable);
	
	Integer countOffindAllByRateAndStatus(Integer rate, Integer st);

	int getTotalItemByRateAndStatus(Integer rate, Integer st);
	
	void updateFeeback(CommentArticleDTO dto);
	
	void updateReview(CommentArticleDTO dto);
	
	List<CommentArticleDTO> findByArticleId(Integer id);
	
	void deleteComment(Integer maPB);
}
