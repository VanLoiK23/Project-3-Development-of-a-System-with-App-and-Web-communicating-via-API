package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.SlideDTO;

public interface ISlideService {
	List<SlideDTO> findAll(Pageable pageable);
	
	List<SlideDTO> selectAll();
	
	SlideDTO findById(int id);
	
	SlideDTO save(SlideDTO ncc);
	
	List<SlideDTO> findTrash();
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
}
