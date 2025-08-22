package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.entity.CategoryEntity;
import com.thuongmaidientu.entity.HedieuhanhEntity;
import com.thuongmaidientu.repository.HedieuhanhRepository;
import com.thuongmaidientu.service.IHedieuhanhService;

@Service
public class HedieuhanhService implements IHedieuhanhService {
	@Autowired
	private HedieuhanhRepository hedieuhanhRepository;

	@Override
	public List<HedieuhanhDTO> findAll(Pageable pageable) {
		List<HedieuhanhEntity> entities = hedieuhanhRepository.findByTrash("active", pageable).getContent();
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<HedieuhanhDTO> selectAll() {
		List<HedieuhanhEntity> entities = hedieuhanhRepository.findByTrash("active");
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	
	private HedieuhanhDTO convertToDTO(HedieuhanhEntity entity) {
		HedieuhanhDTO dto = new HedieuhanhDTO();
		dto.setId(entity.getMaHeDieuHanh() != null ? entity.getMaHeDieuHanh().longValue() : null);
		dto.setTenHeDieuHanh(entity.getTenHeDieuHanh());
		dto.setStatus(entity.getStatus());
		dto.setNum_trash(getTotalItemTrash());

		return dto;
	}
	
	public HedieuhanhEntity convertToEntity(HedieuhanhDTO dto) {

		HedieuhanhEntity entity = new HedieuhanhEntity();
		entity.setMaHeDieuHanh(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTenHeDieuHanh(dto.getTenHeDieuHanh());
		
		return entity;
	}
	
	@Override
	public void updateStatus(int id, int status) {
		hedieuhanhRepository.updateStatusById(id, status);
	}
	
	@Override
	public void updateTrash(int id, String trash) {
		hedieuhanhRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		hedieuhanhRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public HedieuhanhDTO findById(int masp) {
	    Optional<HedieuhanhEntity> entity = hedieuhanhRepository.findById((long) masp);
	    return entity.map(this::convertToDTO).orElse(null); 
	}
	
	@Override
	public int getTotalItem() {
		int numTotal=(int) hedieuhanhRepository.count();
		int numTrash=(int) hedieuhanhRepository.countByTrash("disable");
		
		return (int) (numTotal-numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) hedieuhanhRepository.countByTrash("disable");
	}

	@Override
	public HedieuhanhDTO save(HedieuhanhDTO ncc) {
		HedieuhanhEntity entity = convertToEntity(ncc);
		HedieuhanhEntity savedEntity = hedieuhanhRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewValue(String value) {
		return hedieuhanhRepository.existsByTenHeDieuHanhAllIgnoreCase(value);
	}

	@Override
	public List<HedieuhanhDTO> findTrash() {
		List<HedieuhanhEntity> entities = hedieuhanhRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
