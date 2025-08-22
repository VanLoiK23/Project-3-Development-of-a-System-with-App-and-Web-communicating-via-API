package com.thuongmaidientu.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.ArticleDTO;
import com.thuongmaidientu.entity.ArticleEntity;
import com.thuongmaidientu.repository.ArticleRepository;
import com.thuongmaidientu.service.IArticleService;

@Service
public class ArticleService implements IArticleService {
	@Autowired
	private ArticleRepository ArticleRepository;

	@Autowired
	private CommentArticleService commentArticleService;

	@Override
	public List<ArticleDTO> findAll(Pageable pageable) {
		List<ArticleEntity> entities = ArticleRepository.findByTrash("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ArticleDTO> selectAll() {
		List<ArticleEntity> entities = ArticleRepository.findByTrash("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private ArticleDTO convertToDTO(ArticleEntity entity) {
		ArticleDTO dto = new ArticleDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setTitle(entity.getTitle());
		dto.setSortDesc(entity.getSortDesc());
		dto.setNum_trash(getTotalItemTrash());
		dto.setImage(entity.getImage());
		dto.setDetails(entity.getDetails());
		dto.setListComment(
				entity.getListComment().stream().map(commentArticleService::convertToDTO).collect(Collectors.toList()));
		dto.setStatus(entity.getStatus());
		dto.setTrash(entity.getTrash());
		dto.setCreatedDate(entity.getCreateAt());


		return dto;
	}

	public ArticleEntity convertToEntity(ArticleDTO dto) {

		ArticleEntity entity = new ArticleEntity();
		
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTitle(dto.getTitle());
		entity.setSortDesc(dto.getSortDesc());
		entity.setImage(dto.getImage());
		entity.setDetails(dto.getDetails());
		entity.setStatus(dto.getStatus());
		entity.setTrash(dto.getTrash());
		entity.setCreateAt(new Date());
		
		return entity;
	}

	@Override
	public void updateStatus(int id, int status) {
		ArticleRepository.updateStatusById(id, status);
	}

	@Override
	public void updateTrash(int id, String trash) {
		ArticleRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		ArticleRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public ArticleDTO findById(int masp) {

		Optional<ArticleEntity> entity = ArticleRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) ArticleRepository.count();
		int numTrash = (int) ArticleRepository.countByTrash("disable");

		return (int) (numTotal - numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) ArticleRepository.countByTrash("disable");
	}

	@Override
	public ArticleDTO save(ArticleDTO ncc) {
		ArticleEntity entity = convertToEntity(ncc);
		ArticleEntity savedEntity = ArticleRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public int getIdByValue(String value) {
		return ArticleRepository.selectIdByValue(value);
	}

	@Override
	public boolean checkNewValue(String value) {
		return ArticleRepository.existsByTitleAllIgnoreCase(value);
	}

	@Override
	public List<ArticleDTO> findTrash() {
		List<ArticleEntity> entities = ArticleRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Integer getNextId(Integer id) {
		Integer idNextInteger=ArticleRepository.getNextIdArticle(id);
		return (idNextInteger!=null)?idNextInteger:ArticleRepository.getMinID();
	}

	@Override
	public Integer getPreviousId(Integer id) {
		Integer idPreInteger=ArticleRepository.getPreviousIdArticle(id);

		return (idPreInteger!=null)?idPreInteger:ArticleRepository.getMaxID();
	}
}
