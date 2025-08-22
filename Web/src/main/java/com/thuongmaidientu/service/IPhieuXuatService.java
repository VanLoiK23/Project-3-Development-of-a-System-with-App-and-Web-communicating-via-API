package com.thuongmaidientu.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.thuongmaidientu.dto.DoanhThuByWeekOrMonthDTO;
import com.thuongmaidientu.dto.OrderByWeekOrMonthDTO;
import com.thuongmaidientu.dto.PhieuXuatDTO;
import com.thuongmaidientu.dto.PnPxByMonthDTO;
import com.thuongmaidientu.dto.PnPxByQuarterDTO;

public interface IPhieuXuatService {

	PhieuXuatDTO findById(int masp);

	PhieuXuatDTO save(PhieuXuatDTO pn);

	List<PhieuXuatDTO> findTrash();

	List<PhieuXuatDTO> findAll(Pageable pageable);
	
	List<PhieuXuatDTO> selectAll();

	int getTotalItem();

	int getTotalItemTrash();

	void updateTrash(int id, String status);
	
	void updateStatus(int id, Integer st);

	List<PhieuXuatDTO> findAllByDateAndStatus(Date fromDate, Integer st, Pageable pageable);

	int getTotalItemByDateAndStatus(Date fromDate, Integer st);
	
	List<PhieuXuatDTO> getAllOrderByUserID(int userID);
	
	PhieuXuatDTO getInfoShipping(Integer id);
	
	void updateStatusCancel(Integer id,String reason);
	
	Integer getUserIdByOrder(Integer orderId);
	
	 List<OrderByWeekOrMonthDTO> getStatisticalFollowWeek();
	 
	 List<OrderByWeekOrMonthDTO> getStatisticalFollowMonth();
	 
	 List<DoanhThuByWeekOrMonthDTO> getStatisticalDoanhThuFollowWeek();
	 
	 List<DoanhThuByWeekOrMonthDTO> getStatisticalDoanhThuFollowMonth();
	 
	 PnPxByMonthDTO getPnPxByMonth();
	 
	 PnPxByQuarterDTO getPnPxByQuarter();

}
