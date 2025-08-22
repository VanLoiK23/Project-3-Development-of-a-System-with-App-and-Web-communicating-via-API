package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.XuatXuDTO;

public interface IXuatxuService {
	List<XuatXuDTO> findAll(Pageable pageable);

	List<XuatXuDTO> selectAll();
	
	XuatXuDTO findById(int id);
	
	List<XuatXuDTO> findTrash();
	
	XuatXuDTO save(XuatXuDTO ncc);
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);

}
