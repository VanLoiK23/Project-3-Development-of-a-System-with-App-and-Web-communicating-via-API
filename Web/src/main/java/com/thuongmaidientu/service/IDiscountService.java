package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.DiscountDTO;

public interface IDiscountService {
	List<DiscountDTO> findAll(Pageable pageable);

	List<DiscountDTO> selectAll();
	
	DiscountDTO findById(int id);
	
	List<DiscountDTO> findTrash();
	
	DiscountDTO save(DiscountDTO ncc);
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewCode(String value);
	
	void decrese(int id);

}
