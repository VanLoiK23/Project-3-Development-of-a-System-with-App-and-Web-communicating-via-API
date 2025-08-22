package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.MauSacDTO;

public interface ICategotyService {
	List<CategoryDTO> findAll(Pageable pageable);

	List<CategoryDTO> selectAll();
	
	CategoryDTO findById(int id);
	
	CategoryDTO save(CategoryDTO ct);
	
	int getTotalItem();
	
	List<CategoryDTO> findTrash();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);
	
	String getImageOld(int ma);
	
	Integer getIdCategory(String name);

}
