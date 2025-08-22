package com.thuongmaidientu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.entity.KhuVucKhoEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.KhuVucKhoRepository;
import com.thuongmaidientu.service.IKhuVucKhoService;
import com.thuongmaidientu.service.IProductService;

@Service
public class KhuVucKhoService implements IKhuVucKhoService {
	@Autowired
	private KhuVucKhoRepository KhuVucKhoRepository;
	
	@Autowired
	private ProductService productService;

	@Override
	public List<KhuVucKhoDTO> findAll(Pageable pageable) {
		List<KhuVucKhoEntity> entities = KhuVucKhoRepository.findByTrash("active", pageable).getContent();
	    List<KhuVucKhoDTO> result = new ArrayList<>();

	    for (KhuVucKhoEntity entity : entities) {
	        KhuVucKhoDTO dto = convertToDTO(entity);

	       List<ProductDTO> productDTOs=productService.findByKho(dto.getId().intValue());

	        dto.setProducts(productDTOs);
	        result.add(dto);
	    }

	    return result;
	}
	
	@Override
	public List<KhuVucKhoDTO> selectAll() {
		List<KhuVucKhoEntity> entities = KhuVucKhoRepository.findByTrash("active");
	    return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	
	private KhuVucKhoDTO convertToDTO(KhuVucKhoEntity entity) {
		KhuVucKhoDTO dto = new KhuVucKhoDTO();
		dto.setId(entity.getMaKhuVuc() != null ? entity.getMaKhuVuc().longValue() : null);
		dto.setTenKhuVuc(entity.getTenKhuVuc());
		dto.setGhiChu(entity.getGhiChu());
		dto.setStatus(entity.getStatus());

		return dto;
	}
	
	public KhuVucKhoEntity convertToEntity(KhuVucKhoDTO dto) {

		KhuVucKhoEntity entity = new KhuVucKhoEntity();
		entity.setMaKhuVuc(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTenKhuVuc(dto.getTenKhuVuc());
		entity.setGhiChu(dto.getGhiChu());

		return entity;
	}
	
	@Override
	public void updateStatus(int id, int status) {
		KhuVucKhoRepository.updateStatusById(id, status);
	}
	
	@Override
	public void updateTrash(int id, String trash) {
		KhuVucKhoRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		KhuVucKhoRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public KhuVucKhoDTO findById(int masp) {
	    Optional<KhuVucKhoEntity> entity = KhuVucKhoRepository.findById((long) masp);
	    return entity.map(this::convertToDTO).orElse(null); 
	}
	
	@Override
	public int getTotalItem() {
		int numTotal=(int) KhuVucKhoRepository.count();
		int numTrash=(int) KhuVucKhoRepository.countByTrash("disable");
		
		return (int) (numTotal-numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) KhuVucKhoRepository.countByTrash("disable");
	}

	@Override
	public KhuVucKhoDTO save(KhuVucKhoDTO ncc) {
		KhuVucKhoEntity entity = convertToEntity(ncc);
		KhuVucKhoEntity savedEntity = KhuVucKhoRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public boolean checkNewValue(String value) {
		return KhuVucKhoRepository.existsByTenKhuVucAllIgnoreCase(value);
	}

	@Override
	public List<KhuVucKhoDTO> findTrash() {
		List<KhuVucKhoEntity> entities = KhuVucKhoRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}


}
