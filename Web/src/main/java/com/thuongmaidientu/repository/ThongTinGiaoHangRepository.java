package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.ThongTinGiaoHangEntity;

public interface ThongTinGiaoHangRepository extends JpaRepository<ThongTinGiaoHangEntity, Long>{
	List<ThongTinGiaoHangEntity> findByKhachHangDat(KhachHangEntity khachHangDat);

}
