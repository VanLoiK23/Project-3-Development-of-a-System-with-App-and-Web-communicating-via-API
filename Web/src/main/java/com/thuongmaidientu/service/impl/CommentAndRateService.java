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

import com.thuongmaidientu.dto.CommentAndRateDTO;
import com.thuongmaidientu.entity.CommentAndRateEntity;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.CommentAndRateRepository;
import com.thuongmaidientu.service.ICommentAndRateService;

@Service
public class CommentAndRateService implements ICommentAndRateService {

	@Autowired
	private CommentAndRateRepository commentAndRateRepository;

	private CommentAndRateDTO convertToDTO(CommentAndRateEntity entity) {
		CommentAndRateDTO dto = new CommentAndRateDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setContent(entity.getContent());
		dto.setImg(entity.getImg());
		dto.setId_user(entity.getUser().getMaKhachHang());
		dto.setId_sp(entity.getProduct().getId());
		dto.setFeeback(entity.getFeedback());
		dto.setFeedbackContent(entity.getFeedbackContent());
		dto.setNhanvien(entity.getNhanVien());
		dto.setNgayPH(entity.getNgayPhanHoi());
		if (entity.getOrder() != null) {
			dto.setOrder_id(entity.getOrder().getId());
		}
		dto.setNgayDanhGia(entity.getNgayDanhGia());
		dto.setHoten(entity.getUser().getHoTen());
		dto.setTensp(entity.getProduct().getTenSanPham());
		dto.setRate(entity.getRate());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		if (entity.getNgayPhanHoi() != null) {
			dto.setNgayphanhoi(sdf1.format(dto.getNgayPH()));
		}
		dto.setNgay_đanhgia(sdf.format(dto.getNgayDanhGia()));

		return dto;
	}

	public CommentAndRateEntity convertToEntity(CommentAndRateDTO dto) {

		CommentAndRateEntity entity = new CommentAndRateEntity();

		if (dto.getId() != null && dto.getId() != -1) {
			entity.setId(dto.getId().intValue());

		} else {
			entity.setId(null);
		}

		Date todayDate = new Date();

		entity.setNgayDanhGia(todayDate);

		entity.setContent(dto.getContent());
		entity.setImg(dto.getImg());

		KhachHangEntity userEntity = new KhachHangEntity();
		userEntity.setMaKhachHang(dto.getId_user());
		entity.setUser(userEntity);

		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(dto.getId_sp());
		entity.setProduct(productEntity);

		entity.setFeedback(dto.getFeeback());
		entity.setFeedbackContent(dto.getFeedbackContent());
		entity.setNhanVien(dto.getNhanvien());

		entity.setNgayPhanHoi(dto.getNgayPH());

		PhieuXuatEntity phieuXuatEntity = new PhieuXuatEntity();
		phieuXuatEntity.setId(dto.getOrder_id());
		entity.setOrder(phieuXuatEntity);

		entity.setRate(dto.getRate());

		return entity;
	}

	@Override
	public List<CommentAndRateDTO> findAll(Pageable pageable) {
		List<CommentAndRateEntity> entities = commentAndRateRepository.findAll(pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<CommentAndRateDTO> selectAll() {
		List<CommentAndRateEntity> entities = commentAndRateRepository.findAll();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public CommentAndRateDTO findById(int id) {
		Optional<CommentAndRateEntity> entity = commentAndRateRepository.findById(id);
		System.out.println("Test " + entity);

		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public CommentAndRateDTO save(CommentAndRateDTO ncc) {
		CommentAndRateEntity entity = convertToEntity(ncc);
		CommentAndRateEntity savedEntity = commentAndRateRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) commentAndRateRepository.count();
		return numTotal;
	}

	@Override
	public void delete(int id) {
		commentAndRateRepository.deleteById(id);
	}

	@Override
	public List<CommentAndRateDTO> findAllByRateAndStatus(Integer rate, Integer st, Pageable pageable) {
		// TODO Auto-generated method stub
		List<CommentAndRateEntity> entities = commentAndRateRepository.findAllByOptionalFilters(rate, st, pageable);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Integer countOffindAllByRateAndStatus(Integer rate, Integer st) {
		// TODO Auto-generated method stub
		return commentAndRateRepository.countByOptionalFilters(rate, st);
	}

	@Override
	public int getTotalItemByRateAndStatus(Integer rate, Integer st) {
		return commentAndRateRepository.countByOptionalFilters(rate, st);
	}

	@Override
	public void updateFeeback(CommentAndRateDTO dto) {
		commentAndRateRepository.updateFeeback(dto.getId().intValue(), dto.getFeeback(), dto.getFeedbackContent(),
				dto.getNhanvien(), dto.getNgayPH());
	}

	@Override
	public void updateReview(CommentAndRateDTO dto) {
		commentAndRateRepository.updateReview(dto.getId().intValue(), dto.getContent(), dto.getRate(), dto.getImg());
	}

	@Override
	public List<CommentAndRateDTO> findByProductId(Integer id) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(id);
		List<CommentAndRateEntity> entities = commentAndRateRepository.findByProduct(productEntity);

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public void deleteComment(Integer maPB) {

		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(maPB);

		commentAndRateRepository.deleteByProduct(productEntity);
	}

	@Override
	public CommentAndRateDTO getExistCommentByMaspAndOrderId(Integer masp, Integer orderID, Integer clientId) {
		List<CommentAndRateEntity> entities = commentAndRateRepository.findByProductIdAndOrderId(masp, orderID,
				clientId);
		return (entities != null && !entities.isEmpty()) ? convertToDTO(entities.get(0)) : null;
	}

}
