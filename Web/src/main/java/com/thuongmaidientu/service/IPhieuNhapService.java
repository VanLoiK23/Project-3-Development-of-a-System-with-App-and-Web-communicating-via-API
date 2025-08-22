package com.thuongmaidientu.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.PhieuNhapDTO;

public interface IPhieuNhapService {

	PhieuNhapDTO findById(int masp);

	PhieuNhapDTO save(PhieuNhapDTO pn);

	List<PhieuNhapDTO> findTrash();

	List<PhieuNhapDTO> findAll(Pageable pageable);

	int getTotalItem();
	
	int getTotalItemTrash();
		
	void updateTrash(int id,String status);
	
	List<PhieuNhapDTO> findAllByDate(Date fromDate, Pageable pageable);
	int getTotalItemByDate(Date fromDate);
	
//	List<E>

}
