package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.ArticleDTO;
import com.thuongmaidientu.dto.RamDTO;

public interface IArticleService {
	List<ArticleDTO> findAll(Pageable pageable);

	List<ArticleDTO> selectAll();
	
	ArticleDTO findById(int id);
	
	List<ArticleDTO> findTrash();
	
	ArticleDTO save(ArticleDTO r);
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	int getIdByValue(String value);
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);
	
	Integer getNextId(Integer id);
	
	Integer getPreviousId(Integer id);

}
