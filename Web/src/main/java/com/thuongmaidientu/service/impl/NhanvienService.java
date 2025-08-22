package com.thuongmaidientu.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.NhanVienDTO;
import com.thuongmaidientu.entity.NhanVienEntity;
import com.thuongmaidientu.repository.NhanvienRepository;
import com.thuongmaidientu.service.INhanvienService;

@Service
public class NhanvienService implements INhanvienService {

	@Autowired
	private NhanvienRepository nhanvienRepository;

	@Override
	public NhanVienDTO findById(int id) {
		Optional<NhanVienEntity> entity = nhanvienRepository.findById(id);
		return entity.map(this::convertToDTO).orElse(null);
	}

	private NhanVienDTO convertToDTO(NhanVienEntity entity) {
		NhanVienDTO dto = new NhanVienDTO();

		dto.setId(entity.getId());
		dto.setDiaChi(entity.getDiaChi());
		dto.setEmail(entity.getEmail());
		dto.setGioiTinh(entity.getGioiTinh());
		dto.setHoTen(entity.getHoTen());
		dto.setMatKhau(entity.getMatKhau());
		dto.setNgaySinh(entity.getNgaySinh());
		dto.setSoDienThoai(entity.getSoDienThoai());
		dto.setTrangThai(entity.getTrangThai());

		return dto;
	}
	
	public NhanVienEntity convertToEntity(NhanVienDTO dto) {

		NhanVienEntity entity = new NhanVienEntity();
		if(dto.getId()!=null) {
			entity.setId(dto.getId());			
		}
		entity.setDiaChi(dto.getDiaChi());
		entity.setEmail(dto.getEmail());
		entity.setGioiTinh(dto.getGioiTinh());
		entity.setHoTen(dto.getHoTen());
		entity.setMatKhau(dto.getMatKhau());
		entity.setNgaySinh(dto.getNgaySinh());
		entity.setSoDienThoai(dto.getSoDienThoai());
		entity.setTrangThai(dto.getTrangThai());

		return entity;
	}

}
