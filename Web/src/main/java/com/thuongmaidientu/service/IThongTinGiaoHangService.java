package com.thuongmaidientu.service;

import java.util.List;

import com.thuongmaidientu.dto.ThongTinGiaoHangDTO;

public interface IThongTinGiaoHangService {

	List<ThongTinGiaoHangDTO> getAllByIdkh(Integer idkh);
	
	ThongTinGiaoHangDTO insertAddress(ThongTinGiaoHangDTO address);
		
	boolean deleteAddress(Integer id);
	
	ThongTinGiaoHangDTO findById(Integer id);
	
}
