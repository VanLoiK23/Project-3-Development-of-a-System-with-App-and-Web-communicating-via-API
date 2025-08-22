package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.CommentAndRateDTO;

public interface ICommentAndRateService {
	List<CommentAndRateDTO> findAll(Pageable pageable);

	List<CommentAndRateDTO> selectAll();

	CommentAndRateDTO findById(int id);

	CommentAndRateDTO save(CommentAndRateDTO ncc);

	int getTotalItem();

	void delete(int id);
	
	List<CommentAndRateDTO> findAllByRateAndStatus(Integer rate, Integer st, Pageable pageable);
	
	Integer countOffindAllByRateAndStatus(Integer rate, Integer st);

	int getTotalItemByRateAndStatus(Integer rate, Integer st);
	
	void updateFeeback(CommentAndRateDTO dto);
	
	void updateReview(CommentAndRateDTO dto);
	
	List<CommentAndRateDTO> findByProductId(Integer id);
	
	void deleteComment(Integer maPB);
	
	CommentAndRateDTO getExistCommentByMaspAndOrderId(Integer masp,Integer orderId,Integer clientId);
}
