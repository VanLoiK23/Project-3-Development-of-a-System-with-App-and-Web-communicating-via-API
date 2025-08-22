package com.thuongmaidientu.service;

import java.util.List;

import com.thuongmaidientu.dto.PhienBanSanPhamDTO;

public interface IPhienbanspService {
	List<PhienBanSanPhamDTO> findAll(int masp);

	PhienBanSanPhamDTO save(PhienBanSanPhamDTO phienbanmoi);

	void delete(Long mapbsp);

	List<Integer> selectAllMaPBSPByMaSP(int masp);

	Long findVariantId(PhienBanSanPhamDTO variant, Long masp);

	Integer countProductCon(Integer masp);

	Integer findMaSP(Integer mapb);

	void update(PhienBanSanPhamDTO pb);

	void updateSL(Integer mapb, Integer sl);

	PhienBanSanPhamDTO findById(int id);

	void deletePhienBanSP(Integer maPB);

	PhienBanSanPhamDTO findByConfig(Integer masp, Integer ram, Integer rom, String color);
	
	void setUpQuantity(Integer mapb, Integer sl);


}
