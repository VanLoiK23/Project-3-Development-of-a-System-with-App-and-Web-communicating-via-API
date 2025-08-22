package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.ThongTinGiaoHangDTO;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.NhaCungCapEntity;
import com.thuongmaidientu.entity.NhanVienEntity;
import com.thuongmaidientu.entity.RomEntity;
import com.thuongmaidientu.entity.ThongTinGiaoHangEntity;
import com.thuongmaidientu.repository.ThongTinGiaoHangRepository;
import com.thuongmaidientu.service.IThongTinGiaoHangService;

@Service
public class ThongTinGiaoHangService implements IThongTinGiaoHangService {

	@Autowired
	private ThongTinGiaoHangRepository thongTinGiaoHangRepository;

	@Autowired
	private KhachHangService khachHangService;

	@Override
	public List<ThongTinGiaoHangDTO> getAllByIdkh(Integer idkh) {
		KhachHangEntity khEntity=khachHangService.convertToEntity(khachHangService.findById(idkh));
		
		List<ThongTinGiaoHangEntity> entities = thongTinGiaoHangRepository.findByKhachHangDat(khEntity);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ThongTinGiaoHangDTO convertToDTO(ThongTinGiaoHangEntity entity) {
		ThongTinGiaoHangDTO dto = new ThongTinGiaoHangDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setHoVaTen(entity.getHoVaTen());
		dto.setIdkh(entity.getKhachHangDat().getMaKhachHang());
		dto.setEmail(entity.getEmail());
		dto.setSoDienThoai(entity.getSoDienThoai());
		dto.setStreetName(entity.getStreetName());
		dto.setDistrict(entity.getDistrict());
		dto.setCity(entity.getCity());
		dto.setCountry(entity.getCountry());
		dto.setNote(entity.getNote());

		return dto;
	}

	public ThongTinGiaoHangEntity convertToEntity(ThongTinGiaoHangDTO dto) {

		ThongTinGiaoHangEntity entity = new ThongTinGiaoHangEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setHoVaTen(dto.getHoVaTen());
		
		KhachHangEntity khEntity=khachHangService.convertToEntity(khachHangService.findById(dto.getIdkh()));
		
		entity.setKhachHangDat(khEntity);
		entity.setEmail(dto.getEmail());
		entity.setSoDienThoai(dto.getSoDienThoai());
		entity.setStreetName(dto.getStreetName());
		entity.setDistrict(dto.getDistrict());
		entity.setCity(dto.getCity());
		entity.setCountry(dto.getCountry());
		entity.setNote(dto.getNote());

		return entity;
	}

	@Override
	public ThongTinGiaoHangDTO insertAddress(ThongTinGiaoHangDTO address) {
		ThongTinGiaoHangEntity entity = convertToEntity(address);
		ThongTinGiaoHangEntity savedEntity = thongTinGiaoHangRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean deleteAddress(Integer id) {
		thongTinGiaoHangRepository.deleteById(Long.valueOf(id));
		return true;
	}

	@Override
	public ThongTinGiaoHangDTO findById(Integer id) {
		Optional<ThongTinGiaoHangEntity> entity = thongTinGiaoHangRepository.findById((long) id);
	    return entity.map(this::convertToDTO).orElse(null); 
	}

}
