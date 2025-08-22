package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.HedieuhanhDTO;

public interface IHedieuhanhService {
	List<HedieuhanhDTO> findAll(Pageable pageable);

	List<HedieuhanhDTO> selectAll();
	
	HedieuhanhDTO findById(int id);
	
	HedieuhanhDTO save(HedieuhanhDTO hdh);
	
	int getTotalItem();
	
	List<HedieuhanhDTO> findTrash();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);

}
