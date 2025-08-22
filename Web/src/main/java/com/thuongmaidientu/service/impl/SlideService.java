package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.SlideDTO;
import com.thuongmaidientu.entity.SlideEntity;
import com.thuongmaidientu.repository.SlideRepository;
import com.thuongmaidientu.service.ISlideService;

@Service
public class SlideService implements ISlideService {
	@Autowired
	private SlideRepository SlideRepository;

	@Override
	public List<SlideDTO> findAll(Pageable pageable) {
		List<SlideEntity> entities = SlideRepository.findByTrash("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<SlideDTO> selectAll() {
		List<SlideEntity> entities = SlideRepository.findAll();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private SlideDTO convertToDTO(SlideEntity entity) {
		SlideDTO dto = new SlideDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setImage(entity.getImage());
		dto.setStatus(entity.getStatus());
		dto.setTrash(entity.getTrash());

		return dto;
	}

	private SlideEntity convertToEntity(SlideDTO dto) {

		SlideEntity entity = new SlideEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setImage(dto.getImage());
		entity.setTrash(dto.getTrash());
		entity.setStatus(entity.getStatus());

		return entity;
	}

	@Override
	public void updateStatus(int id, int status) {
		SlideRepository.updateStatusById(id, status);
	}

	@Override
	public void updateTrash(int id, String trash) {
		SlideRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		SlideRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public SlideDTO findById(int masp) {
		Optional<SlideEntity> entity = SlideRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) SlideRepository.count();
		int numTrash = (int) SlideRepository.countByTrash("disable");

		return (int) (numTotal - numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) SlideRepository.countByTrash("disable");
	}

	@Override
	public SlideDTO save(SlideDTO ncc) {
		SlideEntity entity = convertToEntity(ncc);
		SlideEntity savedEntity = SlideRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public List<SlideDTO> findTrash() {
		List<SlideEntity> entities = SlideRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
}
