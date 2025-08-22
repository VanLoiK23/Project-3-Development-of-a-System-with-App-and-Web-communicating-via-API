package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.CTSanPhamDTO;
import com.thuongmaidientu.entity.CTSanPhamEntity;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.PhieuNhapEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;
import com.thuongmaidientu.repository.ChiTietSPRepository;
import com.thuongmaidientu.service.IChiTietSPService;

@Service
public class ChitietSPService implements IChiTietSPService {
	@Autowired
	private ChiTietSPRepository chiTietSPRepository;

	@Override
	public void delete(Long mapb) {
		chiTietSPRepository.deleteByMaPhienBanSP(mapb.intValue());
	}

	@Override
	public void save(CTSanPhamDTO ct) {
		chiTietSPRepository.save(convertToEntity(ct));
	}

	public CTSanPhamEntity convertToEntity(CTSanPhamDTO dto) {

		CTSanPhamEntity entity = new CTSanPhamEntity();

		Optional.ofNullable(dto.getProductVersionId()).ifPresent(id -> {
			PhienBanSanPhamEntity pn = new PhienBanSanPhamEntity();
			pn.setMaPhienBanSP(id);

			entity.setProductVersion(pn);
		});
		entity.setMaImei(dto.getMaImei());

		PhieuNhapEntity pNhapEntity = new PhieuNhapEntity();
		pNhapEntity.setId(dto.getPnId());

		entity.setPn(pNhapEntity);

		Optional.ofNullable(dto.getPxId()).ifPresent(id -> {
			PhieuXuatEntity pn = new PhieuXuatEntity();
			pn.setId(id);
			entity.setPx(pn);
		});

		Optional.ofNullable(dto.getTinhTrang()).ifPresent(id -> {

			entity.setTinhTrang(id);
		});

		return entity;
	}

	@Override
	public List<Long> selectListImei(Integer mapb, Pageable page) {
		return chiTietSPRepository.findImeisByPhienBan(mapb, page);
	}

	@Override
	public List<Long> selectListImeiForCancle(Integer orderId, Integer mapb) {
		return chiTietSPRepository.findImeisByPhienBanForCancle(orderId, mapb);
	}
	
	@Override
	public void updateCTSP(int mapx, int mapb, Long imei) {
		chiTietSPRepository.updateCTSP(mapx, mapb, imei);
	}

	@Override
	public void updateCTSPForCancle(int mapx, int mapb, Long imei) {
		chiTietSPRepository.updateCTSPForCancle(mapx, mapb, imei);
	}

	
	@Override
	public void deleteCTSP(Integer maPB) {		
		chiTietSPRepository.deleteByMaPhienBanSP(maPB);
	}

	@Override
	public Integer getQuantityVersionProduct(Integer masp, Integer maPBSP) {
		return chiTietSPRepository.getQuantityVersionProductInStock(masp, maPBSP);
	}

	@Override
	public Integer getQuantityProductByStatus(Integer masp, Integer status) {
		return chiTietSPRepository.getQuantityProductByStatus(masp, status);
	}


}
