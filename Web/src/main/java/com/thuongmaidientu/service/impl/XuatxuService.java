package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.entity.CategoryEntity;
import com.thuongmaidientu.entity.XuatXuEntity;
import com.thuongmaidientu.repository.XuatxuRepository;
import com.thuongmaidientu.service.IXuatxuService;


@Service
public class XuatxuService implements IXuatxuService {
	@Autowired
	private XuatxuRepository XuatXuRepository;

	@Override
	public List<XuatXuDTO> findAll(Pageable pageable) {
		List<XuatXuEntity> entities = XuatXuRepository.findByTrash("active", pageable).getContent();
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<XuatXuDTO> selectAll() {
		List<XuatXuEntity> entities =  XuatXuRepository.findByTrash("active");
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	
	private XuatXuDTO convertToDTO(XuatXuEntity entity) {
		XuatXuDTO dto = new XuatXuDTO();
		dto.setId(entity.getMaXuatXu() != null ? entity.getMaXuatXu().longValue() : null);
		dto.setTenXuatXu(entity.getTenXuatXu());
		dto.setStatus(entity.getStatus());

		return dto;
	}
	
	public XuatXuEntity convertToEntity(XuatXuDTO dto) {

		XuatXuEntity entity = new XuatXuEntity();
		entity.setMaXuatXu(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTenXuatXu(dto.getTenXuatXu());
		
		return entity;
	}
	
	@Override
	public void updateStatus(int id, int status) {
		XuatXuRepository.updateStatusById(id, status);
	}
	
	@Override
	public void updateTrash(int id, String trash) {
		XuatXuRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		XuatXuRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public XuatXuDTO findById(int masp) {
	    Optional<XuatXuEntity> entity = XuatXuRepository.findById((long) masp);
	    return entity.map(this::convertToDTO).orElse(null); 
	}
	
	@Override
	public int getTotalItem() {
		int numTotal=(int) XuatXuRepository.count();
		int numTrash=(int) XuatXuRepository.countByTrash("disable");
		
		return (int) (numTotal-numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) XuatXuRepository.countByTrash("disable");
	}

	@Override
	public XuatXuDTO save(XuatXuDTO ncc) {
		XuatXuEntity entity = convertToEntity(ncc);
		XuatXuEntity savedEntity = XuatXuRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewValue(String value) {
		return XuatXuRepository.existsByTenXuatXuAllIgnoreCase(value);
	}

	@Override
	public List<XuatXuDTO> findTrash() {
		List<XuatXuEntity> entities = XuatXuRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
