package com.thuongmaidientu.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.DoanhThuByWeekOrMonthDTO;
import com.thuongmaidientu.dto.OrderByWeekOrMonthDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.dto.PnPxByMonthDTO;
import com.thuongmaidientu.dto.PnPxByQuarterDTO;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;
import com.thuongmaidientu.repository.PhieuNhapRepository;
import com.thuongmaidientu.repository.PhieuXuatRepository;
import com.thuongmaidientu.service.IPhieuXuatService;

@Service
public class PhieuXuatService implements IPhieuXuatService {

	@Autowired
	private PhieuXuatRepository phieuXuatRepository;

	@Autowired
	private KhachHangService khachHangRepository;

	@Autowired
	private PhieuNhapRepository phieuNhapRepository;

	@Override
	public PhieuXuatDTO save(PhieuXuatDTO pn) {
		PhieuXuatEntity entity = convertToEntity(pn);
		PhieuXuatEntity savedEntity = phieuXuatRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	// Convert Entity -> DTO
	private PhieuXuatDTO convertToDTO(PhieuXuatEntity entity) {
		PhieuXuatDTO dto = new PhieuXuatDTO();
		dto.setId(Long.valueOf(entity.getId()));
		dto.setThoiGian(entity.getThoiGian());
		dto.setIDKhachHang(entity.getKhachHangMua().getMaKhachHang());
		dto.setMakh(entity.getKhachHangMua().getMaKhachHang());
		dto.setKhachHangName(entity.getKhachHangMua().getHoTen());
		dto.setCodeCart(entity.getCodeCart());
		dto.setPayment(entity.getPayment());
		dto.setTongTien(entity.getTongTien());
		dto.setSave(entity.getSave());
		dto.setCartShipping(entity.getCartShipping());
		dto.setDiscountCode(entity.getDiscountCode());
		dto.setFeeTransport(entity.getFeeTransport());
		dto.setFeeback(entity.getFeeback());
		dto.setSdt(entity.getKhachHangMua().getSoDienThoai());
		dto.setStatus(entity.getStatus());
		dto.setDate(entity.getThoiGian());
		
		return dto;
	}

	// Convert DTO -> Entity
	public PhieuXuatEntity convertToEntity(PhieuXuatDTO dto) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formattedDateTime = LocalDateTime.now().format(formatter);

		LocalDateTime localDateTime = LocalDateTime.now();

		// Chuyển đổi LocalDateTime thành Date
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		PhieuXuatEntity entity = new PhieuXuatEntity();

//		Optional.ofNullable(dto.getIDKhachHang()).ifPresent(id -> {
//			KhachHangDTO productDTO = khachHangRepository.findById(id);
//
//			entity.setKhachHangMua(khachHangRepository.convertToEntity(productDTO));
//		});

		KhachHangEntity etEntity = new KhachHangEntity();
		etEntity.setMaKhachHang(dto.getIDKhachHang());
		entity.setKhachHangMua(etEntity);

		entity.setThoiGian(date);

		entity.setTongTien(dto.getTongTien());

		entity.setCodeCart(dto.getCodeCart());

		entity.setStatus(dto.getStatus());

		entity.setPayment(dto.getPayment());

		entity.setCartShipping(dto.getCartShipping());

		entity.setDiscountCode(dto.getDiscountCode());

		entity.setFeeTransport(dto.getFeeTransport());

		entity.setFeeback(dto.getFeeback());

		entity.setUpdatedAt(date);

		return entity;
	}

	@Override
	public List<PhieuXuatDTO> findTrash() {
		List<PhieuXuatEntity> entities = phieuXuatRepository.findBySave("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public PhieuXuatDTO findById(int masp) {
		Optional<PhieuXuatEntity> entity = phieuXuatRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public List<PhieuXuatDTO> findAll(Pageable pageable) {
		List<PhieuXuatEntity> entities = phieuXuatRepository.findBySave("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) phieuXuatRepository.count();
		int numTrash = (int) phieuXuatRepository.countBySave("disable");

		return (int) (numTotal - numTrash);
	}

	public int getAllTotalNotCancel() {
		int numTotal = (int) phieuXuatRepository.getCountOrderNotCancel();

		return (int) (numTotal);
	}

	@Override
	public List<OrderByWeekOrMonthDTO> getStatisticalFollowWeek() {
		List<Object[]> results = phieuXuatRepository.getStatisticalFollowWeek();

		// Map chứa dữ liệu thực tế
		Map<String, Integer> dataMap = new HashMap<>();
		for (Object[] record : results) {
			String dayOf = (String) record[0];
			int count = ((Number) record[1]).intValue();
			dataMap.put(dayOf, count);
		}

		// Danh sách đầy đủ các ngày trong tuần
		List<String> fullDays = List.of("Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7");

		List<OrderByWeekOrMonthDTO> dtoList = new ArrayList<>();
		for (String day : fullDays) {
			OrderByWeekOrMonthDTO dto = new OrderByWeekOrMonthDTO();
			dto.setDayOf(day);
			dto.setCount(dataMap.getOrDefault(day, 0));
			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public List<OrderByWeekOrMonthDTO> getStatisticalFollowMonth() {
		List<Object[]> results = phieuXuatRepository.getStatisticalFollowMonth();

		Map<String, Integer> dataMap = new HashMap<>();
		for (Object[] record : results) {
			String weekOfMonth = (String) record[0];
			int count = ((Number) record[1]).intValue();
			dataMap.put(weekOfMonth, count);
		}

		// Danh sách đủ 5 tuần (dù có tháng chỉ có 4 tuần)
		List<String> fullWeeks = List.of("Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5");

		List<OrderByWeekOrMonthDTO> dtoList = new ArrayList<>();
		for (String week : fullWeeks) {
			OrderByWeekOrMonthDTO dto = new OrderByWeekOrMonthDTO();
			dto.setDayOf(week);
			dto.setCount(dataMap.getOrDefault(week, 0));
			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public List<DoanhThuByWeekOrMonthDTO> getStatisticalDoanhThuFollowWeek() {
		List<Object[]> results = phieuXuatRepository.getStatisticalDoanhThuFollowWeek();

		// Tạo map từ kết quả SQL
		Map<String, Double> dataMap = new HashMap<>();
		for (Object[] record : results) {
			String dayOf = (String) record[0];
			Double total = ((Number) record[1]).doubleValue();
			dataMap.put(dayOf, total);
		}

		// Danh sách đầy đủ 7 ngày trong tuần
		List<String> fullDays = List.of("Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7");

		// Ánh xạ ra DTO, đảm bảo ngày nào không có thì gán giá trị 0
		List<DoanhThuByWeekOrMonthDTO> dtoList = new ArrayList<>();
		for (String day : fullDays) {
			DoanhThuByWeekOrMonthDTO dto = new DoanhThuByWeekOrMonthDTO();
			dto.setDayOf(day);
			dto.setTotal(dataMap.getOrDefault(day, 0.0));
			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public List<DoanhThuByWeekOrMonthDTO> getStatisticalDoanhThuFollowMonth() {
		List<Object[]> results = phieuXuatRepository.getStatisticalDoanhThuFollowMonth();

		Map<String, Double> dataMap = new HashMap<>();
		for (Object[] record : results) {
			String weekOfMonth = (String) record[0];
			Double total = ((Number) record[1]).doubleValue();
			dataMap.put(weekOfMonth, total);
		}

		// Danh sách đủ 5 tuần (dù có tháng chỉ có 4 tuần)
		List<String> fullWeeks = List.of("Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5");

		List<DoanhThuByWeekOrMonthDTO> dtoList = new ArrayList<>();
		for (String week : fullWeeks) {
			DoanhThuByWeekOrMonthDTO dto = new DoanhThuByWeekOrMonthDTO();
			dto.setDayOf(week);
			dto.setTotal(dataMap.getOrDefault(week, 0.0));
			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public PnPxByMonthDTO getPnPxByMonth() {
		PnPxByMonthDTO dto = new PnPxByMonthDTO();

		// Phiếu xuất
		List<Object[]> phieuXuatList = phieuXuatRepository.getTongPhieuXuatTheoThang();
		for (Object[] obj : phieuXuatList) {
			Integer month = (Integer) obj[0];
			Double total = ((Number) obj[1]).doubleValue();
			dto.getPhieuxuat().put(month, total);
		}

		// Phiếu nhập
		List<Object[]> phieuNhapList = phieuNhapRepository.getTongPhieuNhapTheoThang();
		for (Object[] obj : phieuNhapList) {
			Integer month = (Integer) obj[0];
			Double total = ((Number) obj[1]).doubleValue();
			dto.getPhieunhap().put(month, total);
		}

		return dto;
	}

	public PnPxByQuarterDTO getPnPxByQuarter() {
		PnPxByQuarterDTO dto = new PnPxByQuarterDTO();

		List<Object[]> xuatList = phieuXuatRepository.getPhieuXuatTheoQuy();
		for (Object[] obj : xuatList) {
			int quarter = ((Number) obj[0]).intValue();
			double total = ((Number) obj[1]).doubleValue();
			dto.getPhieuxuat().put(quarter, total);
		}

		List<Object[]> nhapList = phieuNhapRepository.getPhieuNhapTheoQuy();
		for (Object[] obj : nhapList) {
			int quarter = ((Number) obj[0]).intValue();
			double total = ((Number) obj[1]).doubleValue();
			dto.getPhieunhap().put(quarter, total);
		}

		return dto;
	}

	@Override
	public int getTotalItemTrash() {
		return (int) phieuXuatRepository.countBySave("disable");
	}

	@Override
	public void updateTrash(int id, String trash) {
		phieuXuatRepository.updateSaveById(id, trash);
	}

	@Override
	public List<PhieuXuatDTO> findAllByDateAndStatus(Date fromDate, Integer st, Pageable pageable) {
		// TODO Auto-generated method stub

		List<PhieuXuatEntity> entities = phieuXuatRepository.findAllByOptionalFilters(fromDate, st, pageable);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public int getTotalItemByDateAndStatus(Date fromDate, Integer st) {
		return phieuXuatRepository.countByOptionalFilters(fromDate, st);
	}

	@Override
	public void updateStatus(int id, Integer st) {
		phieuXuatRepository.updateStatus(id, st);
	}

	@Override
	public List<PhieuXuatDTO> getAllOrderByUserID(int userID) {
		KhachHangEntity khachHangEntity = new KhachHangEntity();
		khachHangEntity.setMaKhachHang(userID);

		List<PhieuXuatEntity> entities = phieuXuatRepository.findByKhachHangMua(khachHangEntity);

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public PhieuXuatDTO getInfoShipping(Integer id) {
		List<Object[]> results = phieuXuatRepository.getInfoShip(id);

		// Tạo danh sách DTO từ kết quả
		PhieuXuatDTO dto = new PhieuXuatDTO();

		for (Object[] record : results) {
			dto.setName((String) record[0]);
			dto.setPhone((String) record[1]);
			dto.setAddress((String) record[2] + " " + record[3] + " " + record[4]);
		}
		return dto;

	}

	@Override
	public void updateStatusCancel(Integer id, String reason) {
		phieuXuatRepository.updateStatusCancle(id, reason);
	}

	@Override
	public Integer getUserIdByOrder(Integer orderId) {
		return phieuXuatRepository.getUserIdByOrder(orderId);
	}

	@Override
	public List<PhieuXuatDTO> selectAll() {
		List<PhieuXuatEntity> entities = phieuXuatRepository.findBySave("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

}
