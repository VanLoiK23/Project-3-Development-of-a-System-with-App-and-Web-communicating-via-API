package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.ProductDTO;

public interface INhacungcapService {
	List<NhaCungCapDTO> findAll(Pageable pageable);
	
	List<NhaCungCapDTO> findTrash();

	List<NhaCungCapDTO> selectAll();
	
	NhaCungCapDTO findById(int id);
	
	NhaCungCapDTO save(NhaCungCapDTO ncc);
	
	int getTotalItem();
	
	int getTotalItemTrash();
	
	void updateStatus(int id,int status);
	
	void updateTrash(int id,String status);
	
	void delete(int id);
	
	boolean checkNewValue(String value);

}
