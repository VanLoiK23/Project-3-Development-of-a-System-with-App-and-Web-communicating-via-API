package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.ProductDTO;

public interface IKhuVucKhoService {
	List<KhuVucKhoDTO> findAll(Pageable pageable);

	List<KhuVucKhoDTO> selectAll();
	
	KhuVucKhoDTO findById(int id);
	
	KhuVucKhoDTO save(KhuVucKhoDTO ncc);
	
	List<KhuVucKhoDTO> findTrash();
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);
}
