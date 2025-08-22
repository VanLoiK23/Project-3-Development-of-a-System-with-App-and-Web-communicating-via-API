package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.ContactDTO;

public interface IContactService {
	List<ContactDTO> findAll(Pageable pageable);
		
	ContactDTO save(ContactDTO r);
	
	int getTotalItem();
			
	void updateStatus(int id,int status,String email,String feeback,String nameClient,String nameSender);
		
	void delete(int id);
	
	ContactDTO findById(Integer id);
	
}
