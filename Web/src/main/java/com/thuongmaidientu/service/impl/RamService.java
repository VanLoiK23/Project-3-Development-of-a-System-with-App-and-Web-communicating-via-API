package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.entity.CategoryEntity;
import com.thuongmaidientu.entity.RamEntity;
import com.thuongmaidientu.repository.RamRepository;
import com.thuongmaidientu.service.IRamService;

@Service
public class RamService implements IRamService {
	@Autowired
	private RamRepository ramRepository;

	@Override
	public List<RamDTO> findAll(Pageable pageable) {
		List<RamEntity> entities = ramRepository.findByTrash("active", pageable).getContent();
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<RamDTO> selectAll() {
		List<RamEntity> entities = ramRepository.findByTrash("active");
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	
	private RamDTO convertToDTO(RamEntity entity) {
		RamDTO dto = new RamDTO();
		dto.setId(entity.getMaDungLuongRam() != null ? entity.getMaDungLuongRam().longValue() : null);
		dto.setKichThuocRam(entity.getKichThuocRam());
		dto.setStatus(entity.getStatus());
		dto.setNum_trash(getTotalItemTrash());

		return dto;
	}
	
	public RamEntity convertToEntity(RamDTO dto) {
		

		RamEntity entity = new RamEntity();
		entity.setMaDungLuongRam(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setKichThuocRam(dto.getKichThuocRam());
	
		return entity;
	}
	
	@Override
	public void updateStatus(int id, int status) {
		ramRepository.updateStatusById(id, status);
	}
	
	@Override
	public void updateTrash(int id, String trash) {
		ramRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		ramRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public RamDTO findById(int masp) {
		
	    Optional<RamEntity> entity = ramRepository.findById((long) masp);
	    return entity.map(this::convertToDTO).orElse(null); 
	}
	
	@Override
	public int getTotalItem() {
		int numTotal=(int) ramRepository.count();
		int numTrash=(int) ramRepository.countByTrash("disable");
		
		return (int) (numTotal-numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) ramRepository.countByTrash("disable");
	}

	@Override
	public RamDTO save(RamDTO ncc) {
		RamEntity entity = convertToEntity(ncc);
		RamEntity savedEntity = ramRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public int getIdByValue(Integer r) {
		return ramRepository.selectIdByValue(r);
	}

	@Override
	public boolean checkNewValue(Integer value) {
		return ramRepository.existsByKichThuocRamAllIgnoreCase(value);
	}

	@Override
	public List<RamDTO> findTrash() {
		List<RamEntity> entities = ramRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
