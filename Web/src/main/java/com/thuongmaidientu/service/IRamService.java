package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.RamDTO;

public interface IRamService {
	List<RamDTO> findAll(Pageable pageable);

	List<RamDTO> selectAll();
	
	RamDTO findById(int id);
	
	List<RamDTO> findTrash();
	
	RamDTO save(RamDTO r);
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	int getIdByValue(Integer r);
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(Integer value);

}
