package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.RomDTO;

public interface IRomService {
	List<RomDTO> findAll(Pageable pageable);

	List<RomDTO> selectAll();
	
	RomDTO findById(int id);
	
	RomDTO save(RomDTO r);
	
	List<RomDTO> findTrash();
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	int getIdByValue(Integer r);
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(Integer value);

}
