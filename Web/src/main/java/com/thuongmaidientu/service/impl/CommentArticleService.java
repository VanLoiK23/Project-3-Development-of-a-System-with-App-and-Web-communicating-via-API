package com.thuongmaidientu.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.CommentArticleDTO;
import com.thuongmaidientu.entity.ArticleEntity;
import com.thuongmaidientu.entity.CommentArticleEntity;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.repository.CommentArticleRepository;
import com.thuongmaidientu.service.ICommentArticleService;

@Service
public class CommentArticleService implements ICommentArticleService {

	@Autowired
	private CommentArticleRepository commentArticleRepository;


	public CommentArticleDTO convertToDTO(CommentArticleEntity entity) {
		CommentArticleDTO dto = new CommentArticleDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setContent(entity.getContent());
		dto.setNameClient(entity.getKh().getHoTen());
		dto.setIdClient(entity.getKh().getMaKhachHang());
		dto.setNameArticle(entity.getArticle().getTitle());
		dto.setIdArticle(entity.getArticle().getId());
		dto.setNgayPhanHoi(entity.getNgayPhanHoi());
		dto.setNgayDanhGia(entity.getNgayDanhGia());
		dto.setRating(entity.getRating());
		dto.setFeedback(entity.getFeedback());
		dto.setContentFeedback(entity.getContentFeedback());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		if (entity.getNgayPhanHoi() != null) {
			dto.setNgayPhanHoiString(sdf1.format(dto.getNgayPhanHoi()));
		}
		dto.setNgayDanhGiaString(sdf.format(dto.getNgayDanhGia()));

		return dto;
	}

	public CommentArticleEntity convertToEntity(CommentArticleDTO dto) {

		CommentArticleEntity entity = new CommentArticleEntity();

		if (dto.getId() != null && dto.getId() != -1) {
			entity.setId(dto.getId().intValue());

		} else {
			entity.setId(null);
		}
		
		Date todayDate = new Date();

		entity.setNgayDanhGia(todayDate);

		entity.setContent(dto.getContent());

		KhachHangEntity userEntity = new KhachHangEntity();
		userEntity.setMaKhachHang(dto.getIdClient());
		entity.setKh(userEntity);

		ArticleEntity articleEntity = new ArticleEntity();
		articleEntity.setId(dto.getIdArticle());
		entity.setArticle(articleEntity);

		entity.setFeedback(dto.getFeedback());
		entity.setContentFeedback(dto.getContentFeedback());
		entity.setNhanVien(dto.getNhanVien());

		entity.setNgayPhanHoi(dto.getNgayPhanHoi());

		entity.setRating(dto.getRating());

		return entity;
	}

	@Override
	public List<CommentArticleDTO> findAll(Pageable pageable) {
		List<CommentArticleEntity> entities = commentArticleRepository.findAll(pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<CommentArticleDTO> selectAll() {
		List<CommentArticleEntity> entities = commentArticleRepository.findAll();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public CommentArticleDTO findById(int id) {
		Optional<CommentArticleEntity> entity = commentArticleRepository.findById(id);
		System.out.println("Test "+entity);
		
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public CommentArticleDTO save(CommentArticleDTO ncc) {
		CommentArticleEntity entity = convertToEntity(ncc);
		CommentArticleEntity savedEntity = commentArticleRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) commentArticleRepository.count();
		return numTotal;
	}

	@Override
	public void delete(int id) {
		commentArticleRepository.deleteById(id);
	}

	@Override
	public List<CommentArticleDTO> findAllByRateAndStatus(Integer rate, Integer st, Pageable pageable) {
		// TODO Auto-generated method stub
		List<CommentArticleEntity> entities = commentArticleRepository.findAllByOptionalFilters(rate, st, pageable);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	@Override
	public Integer countOffindAllByRateAndStatus(Integer rate, Integer st) {
		// TODO Auto-generated method stub
		return commentArticleRepository.countByOptionalFilters(rate, st);
	}

	@Override
	public int getTotalItemByRateAndStatus(Integer rate, Integer st) {
		return commentArticleRepository.countByOptionalFilters(rate, st);
	}

	@Override
	public void updateFeeback(CommentArticleDTO dto) {
		commentArticleRepository.updateFeeback(dto.getId().intValue(), dto.getFeedback(), dto.getContentFeedback(),
				dto.getNhanVien(), dto.getNgayPhanHoi());
	}

	@Override
	public void updateReview(CommentArticleDTO dto) {
		commentArticleRepository.updateReview(dto.getId().intValue(), dto.getContent(), dto.getRating());
	}

	@Override
	public List<CommentArticleDTO> findByArticleId(Integer id) {
		ArticleEntity articleEntity=new ArticleEntity();
		articleEntity.setId(id);
		List<CommentArticleEntity> entities = commentArticleRepository.findByArticle(articleEntity);

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public void deleteComment(Integer maPB) {

		CommentArticleEntity commentArticleEntity=new CommentArticleEntity();
		commentArticleEntity.setId(maPB);
		
		commentArticleRepository.delete(commentArticleEntity);
	}
	
}
