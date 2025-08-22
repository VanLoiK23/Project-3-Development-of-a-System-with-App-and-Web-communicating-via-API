package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.CTSanPhamDTO;

public interface IChiTietSPService {
	void delete(Long mapb);

	void save(CTSanPhamDTO ct);

	List<Long> selectListImei(Integer mapb, Pageable page);
	
	List<Long> selectListImeiForCancle(Integer orderId,Integer mapb);

	void updateCTSP(int mapx, int mapb, Long imei);

	void updateCTSPForCancle(int mapx, int mapb, Long imei);

	void deleteCTSP(Integer maPB);

	Integer getQuantityVersionProduct(Integer masp, Integer maPBSP);

	Integer getQuantityProductByStatus(Integer masp, Integer status);
}
