package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.NhaCungCapDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.entity.NhaCungCapEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.NhacungcapRepository;
import com.thuongmaidientu.service.INhacungcapService;

@Service
public class NhacungcapService implements INhacungcapService {
	@Autowired
	private NhacungcapRepository nhacungcapRepository;

	@Override
	public List<NhaCungCapDTO> findAll(Pageable pageable) {
		List<NhaCungCapEntity> entities = nhacungcapRepository.findByTrash("active", pageable).getContent();
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<NhaCungCapDTO> selectAll() {
		List<NhaCungCapEntity> entities = nhacungcapRepository.findByTrash("active");
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	
	private NhaCungCapDTO convertToDTO(NhaCungCapEntity entity) {
		NhaCungCapDTO dto = new NhaCungCapDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setTenNhaCungCap(entity.getTenNhaCungCap());
		dto.setDiaChi(entity.getDiaChi());
		dto.setEmail(entity.getEmail());
		dto.setStatus(entity.getStatus());
		dto.setSoDienThoai(entity.getSoDienThoai());

		return dto;
	}
	
	public  NhaCungCapEntity convertToEntity(NhaCungCapDTO dto) {

		NhaCungCapEntity entity = new NhaCungCapEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTenNhaCungCap(dto.getTenNhaCungCap());
		entity.setDiaChi(dto.getDiaChi());
		entity.setEmail(dto.getEmail());
		entity.setSoDienThoai(dto.getSoDienThoai());

		return entity;
	}
	
	@Override
	public void updateStatus(int id, int status) {
		nhacungcapRepository.updateStatusById(id, status);
	}
	
	@Override
	public void updateTrash(int id, String trash) {
		nhacungcapRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		nhacungcapRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public NhaCungCapDTO findById(int masp) {
	    Optional<NhaCungCapEntity> entity = nhacungcapRepository.findById((long) masp);
	    return entity.map(this::convertToDTO).orElse(null); 
	}
	
	@Override
	public int getTotalItem() {
		int numTotal=(int) nhacungcapRepository.count();
		int numTrash=(int) nhacungcapRepository.countByTrash("disable");
		
		return (int) (numTotal-numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) nhacungcapRepository.countByTrash("disable");
	}
	
	@Override
	public List<NhaCungCapDTO> findTrash() {
		List<NhaCungCapEntity> entities = nhacungcapRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public NhaCungCapDTO save(NhaCungCapDTO ncc) {
		NhaCungCapEntity entity = convertToEntity(ncc);
		NhaCungCapEntity savedEntity = nhacungcapRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewValue(String value) {
		return nhacungcapRepository.existsByTenNhaCungCapAllIgnoreCase(value);
	}
}
