package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.MauSacDTO;

public interface IColorService {
	List<MauSacDTO> findAll(Pageable pageable);

	List<MauSacDTO> selectAll();
	
	MauSacDTO findById(int id);
	
	MauSacDTO save(MauSacDTO color);
	
	int getTotalItem();
	
	List<MauSacDTO> findTrash();
	
	int getTotalItemTrash();
	
	int getIdByValue(String color);
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);
}
