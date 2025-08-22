package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.KhachHangDTO;
import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.UserDTO;

public interface IKhachHangService {
	List<UserDTO> findAll(Pageable pageable);

	List<UserDTO> selectAll();

	List<UserDTO> findTrash();

	KhachHangDTO findById(int id);
	
	UserDTO findUserDTOById(int id);

	KhachHangDTO save(KhachHangDTO ncc);

	UserDTO save(UserDTO ncc);

	void update(UserDTO ncc);

	void updateFromAndroid(UserDTO ncc);

	int getTotalItem();

	int getTotalItemTrash();

	void updateStatus(int id, String status);

	void delete(int id);
	
	Integer getUserRegister();

	void saveFCMtoken(Integer userId, String token);
	
	String getTokenUser(Integer userId);
	
	UserDTO login(String userName);
	
	boolean checkIsLockByPhoneOrEmail(String email,String phone);
	
	boolean isExistUserName(String name);
	
	boolean isExistEmail(String email);
	
	UserDTO findByEmail(String email);
	
	UserDTO findByTokenHash(String tokenHash);

}
