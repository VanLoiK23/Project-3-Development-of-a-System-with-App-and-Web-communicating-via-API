package com.thuongmaidientu.service;

import java.util.List;

import com.thuongmaidientu.dto.ChiTietPhieuXuatDTO;

public interface IChiTietPXService {
	void delete(Long mapb);
	
	Integer countNumberProductSold(Integer mapb);
	
	Integer countNumberProductXuatANDMaPBSP(Integer mapb,Integer mapx);
	
	void saveCTPX(ChiTietPhieuXuatDTO px);
	
	List<ChiTietPhieuXuatDTO> getListCTPX(Integer id);
	
	void deletePX(Integer maPB);

}
