package com.thuongmaidientu.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.DiscountDTO;
import com.thuongmaidientu.entity.DiscountEntity;
import com.thuongmaidientu.repository.DiscountRepository;
import com.thuongmaidientu.service.IDiscountService;

@Service
public class DiscountService implements IDiscountService {
	@Autowired
	private DiscountRepository DiscountRepository;

	@Override
	public List<DiscountDTO> findAll(Pageable pageable) {
		List<DiscountEntity> entities = DiscountRepository.findByTrash("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<DiscountDTO> selectAll() {
		List<DiscountEntity> entities = DiscountRepository.findByTrash("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private DiscountDTO convertToDTO(DiscountEntity entity) {
		DiscountDTO dto = new DiscountDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setCode(entity.getCode());
		dto.setStatus(entity.getStatus());
		dto.setDiscountAmount(entity.getDiscountAmount());
		dto.setNumberUsed(entity.getNumberUsed());
		dto.setExpirationDate(entity.getExpirationDate());
		dto.setPaymentLimit(entity.getPaymentLimit());
		dto.setDescription(entity.getDescription());
		dto.setCreatedDate(entity.getCreated());

		return dto;
	}

	public DiscountEntity convertToEntity(DiscountDTO dto) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formattedDateTime = LocalDateTime.now().format(formatter);

		LocalDateTime localDateTime = LocalDateTime.now();

		// Chuyển đổi LocalDateTime thành Date
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		DiscountEntity entity = new DiscountEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setCode(dto.getCode());
		entity.setDiscountAmount(dto.getDiscountAmount());
		entity.setNumberUsed(dto.getNumberUsed());
		entity.setExpirationDate(dto.getExpirationDate());
		entity.setPaymentLimit(dto.getPaymentLimit());
		entity.setDescription(dto.getDescription());
		entity.setCreated(date);
		entity.setStatus(dto.getStatus());

		return entity;
	}

	@Override
	public void updateStatus(int id, int status) {
		DiscountRepository.updateStatusById(id, status);
	}

	@Override
	public void updateTrash(int id, String trash) {
		DiscountRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		DiscountRepository.deleteById((id));
	}

	@Override
	public DiscountDTO findById(int masp) {
		Optional<DiscountEntity> entity = DiscountRepository.findById(masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) DiscountRepository.count();
		int numTrash = (int) DiscountRepository.countByTrash("disable");

		return (int) (numTotal - numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) DiscountRepository.countByTrash("disable");
	}

	@Override
	public DiscountDTO save(DiscountDTO ncc) {
		DiscountEntity entity = convertToEntity(ncc);
		DiscountEntity savedEntity = DiscountRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewCode(String value) {
		return DiscountRepository.existsByCodeAllIgnoreCase(value);
	}

	@Override
	public List<DiscountDTO> findTrash() {
		List<DiscountEntity> entities = DiscountRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public void decrese(int id) {
		DiscountRepository.decrese(id);
	}
}
