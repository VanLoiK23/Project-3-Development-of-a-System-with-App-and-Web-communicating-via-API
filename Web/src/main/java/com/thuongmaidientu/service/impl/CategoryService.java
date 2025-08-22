package com.thuongmaidientu.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.entity.CategoryEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.CategoryRepository;
import com.thuongmaidientu.service.ICategotyService;

@Service
public class CategoryService implements ICategotyService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> findAll(Pageable pageable) {
		List<CategoryEntity> entities = categoryRepository.findByTrash("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<CategoryDTO> selectAll() {
		List<CategoryEntity> entities = categoryRepository.findByTrash("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private CategoryDTO convertToDTO(CategoryEntity entity) {
		CategoryDTO dto = new CategoryDTO();
		dto.setId(entity.getMaThuongHieu() != null ? entity.getMaThuongHieu().longValue() : null);
		dto.setTenThuongHieu(entity.getTenThuongHieu());
		dto.setImage(entity.getImage());
		dto.setStatus(entity.getStatus());
		dto.setNum_trash(getTotalItemTrash());
		dto.setTrash(entity.getTrash());

		return dto;
	}

	public CategoryEntity convertToEntity(CategoryDTO dto) {

		CategoryEntity entity = new CategoryEntity();

		if (dto.getId() != null && dto.getId() != -1) {
			entity.setMaThuongHieu(dto.getId().intValue());

		} else {
			entity.setMaThuongHieu(null);
		}

		entity.setTenThuongHieu(dto.getTenThuongHieu());
		entity.setImage(dto.getImage());

		return entity;
	}

	@Override
	public void updateStatus(int id, int status) {
		categoryRepository.updateStatusById(id, status);
	}

	@Override
	public void updateTrash(int id, String trash) {
		categoryRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		categoryRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public CategoryDTO findById(int masp) {
		Optional<CategoryEntity> entity = categoryRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) categoryRepository.count();
		int numTrash = (int) categoryRepository.countByTrash("disable");

		return (int) (numTotal - numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) categoryRepository.countByTrash("disable");
	}

	@Override
	public CategoryDTO save(CategoryDTO cate) {
		CategoryEntity entity = convertToEntity(cate);
		CategoryEntity savedEntity = categoryRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewValue(String value) {
		return categoryRepository.existsByTenThuongHieuAllIgnoreCase(value);
	}

	@Override
	public String getImageOld(int ma) {
		return categoryRepository.findImageByTenMauSac(ma);
	}

	@Override
	public List<CategoryDTO> findTrash() {
		List<CategoryEntity> entities = categoryRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Integer getIdCategory(String name) {
		List<CategoryEntity> categoryEntities = categoryRepository.findByTenThuongHieu(name);
		return (categoryEntities != null) ? categoryEntities.get(0).getMaThuongHieu() : null;
	}
}
