package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.ChiTietPhieuXuatDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.entity.ChiTietPhieuNhapEntity;
import com.thuongmaidientu.entity.ChiTietPhieuXuatEntity;
import com.thuongmaidientu.entity.ChiTietPhieuXuatKey;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.PhieuNhapEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;
import com.thuongmaidientu.repository.ChitietPXRepository;
import com.thuongmaidientu.repository.PhieuXuatRepository;
import com.thuongmaidientu.service.IChiTietPXService;

@Service
public class ChitietPXService implements IChiTietPXService{

	@Autowired
	private ChitietPXRepository chitietPXRepository;
	
	@Autowired
	private PhieuXuatRepository phieuXuatRepository;
	
	@Autowired
	private PhieuXuatService phieuXuatService;

	@Autowired
	private PhienbansanphamService phienbansanphamService;

	
	@Override
	public void delete(Long mapb) {
		chitietPXRepository.deleteByMaPhienBanSP(mapb.intValue());
	}

	@Override
	public Integer countNumberProductSold(Integer mapb) {
		return chitietPXRepository.countSLSOLD(mapb);
	}

	@Override
	public Integer countNumberProductXuatANDMaPBSP(Integer mapb, Integer mapx) {
		return chitietPXRepository.countSLXuatByMaPBANDMaPN(mapb, mapx);
	}

	@Override
	public void saveCTPX(ChiTietPhieuXuatDTO px) {
		chitietPXRepository.save(convertToEntity(px));		
	}

	@Override
	public List<ChiTietPhieuXuatDTO> getListCTPX(Integer id) {
		List<ChiTietPhieuXuatEntity> entities = chitietPXRepository.getListCTPX(id);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	public ChiTietPhieuXuatEntity convertToEntity(ChiTietPhieuXuatDTO dto) {
		ChiTietPhieuXuatEntity entity = new ChiTietPhieuXuatEntity();

		ChiTietPhieuXuatKey key = new ChiTietPhieuXuatKey();

		Optional.ofNullable(dto.getPhieuXuatId()).ifPresent(id -> {
			PhieuXuatDTO r = phieuXuatService.findById(id.intValue());
			if (r != null) {
				PhieuXuatEntity phieuXuatEntity = phieuXuatService.convertToEntity(r);
//				entity.setPhieuXuat(phieuXuatEntity); // Gán PhieuXuatEntity vào entity
				key.setMaphieuxuat(id); // Gán ID cho key
			} else {
				// Xử lý trường hợp không tìm thấy PhieuXuatDTO, có thể ném exception hoặc log
				// lỗi
				throw new IllegalArgumentException("PhieuXuatDTO không tồn tại");
			}
		});
		Optional.ofNullable(dto.getPhienBanSanPhamXuatId()).ifPresent(id -> {
//			PhienBanSanPhamDTO pn = phienbansanphamService.findById(id.intValue());

			PhienBanSanPhamEntity pBanSanPhamEntity=new PhienBanSanPhamEntity();
			pBanSanPhamEntity.setMaPhienBanSP(id);
			entity.setPhienBanSanPhamXuat(pBanSanPhamEntity);
			key.setMaPhienBanSP(id);
		});
		if (key != null) {
			entity.setId(key);
		}

		entity.setSoLuong(dto.getSoLuong());
		entity.setDonGia(dto.getDonGia());
		entity.setCodeCart(dto.getCodeCart());

		return entity;
	}
	
	public ChiTietPhieuXuatDTO convertToDTO(ChiTietPhieuXuatEntity entity) {
		ChiTietPhieuXuatDTO dto = new ChiTietPhieuXuatDTO();

	    if (entity.getId() != null) {
	        dto.setPhieuXuatId(entity.getId().getMaphieuxuat());
	        dto.setPhienBanSanPhamXuatId(entity.getId().getMaPhienBanSP());
	    }

	    dto.setSoLuong(entity.getSoLuong());
	    dto.setDonGia(entity.getDonGia());
	    dto.setCodeCart(entity.getCodeCart());


	    return dto;
	}

	@Override
	public void deletePX(Integer maPB) {

		PhienBanSanPhamEntity phienBanSanPhamEntity = new PhienBanSanPhamEntity();
		phienBanSanPhamEntity.setMaPhienBanSP(maPB);
		List<ChiTietPhieuXuatEntity> chiTietPhieuXuatEntities = chitietPXRepository.getByPhienBanSanPhamXuat(phienBanSanPhamEntity);

		chiTietPhieuXuatEntities.forEach(ct -> {

			chitietPXRepository.deleteByPhienBanSanPhamXuat(phienBanSanPhamEntity);
			
			PhieuXuatEntity phieuXuatEntity=new PhieuXuatEntity();
			phieuXuatEntity.setId(ct.getPhieuXuat().getId());
			
			if(chitietPXRepository.countByPhieuXuat(phieuXuatEntity)==0) {
				phieuXuatRepository.delete(phieuXuatEntity);
			}
		});
	}

}
