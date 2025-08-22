package com.thuongmaidientu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.service.IThongKeService;

@Service
public class ThongKeService implements IThongKeService {

	@Autowired
	private ProductService productService;

	@Autowired
	private KhuVucKhoService khoService;

	@Autowired
	private KhachHangService khachHangService;

	@Autowired
	private PhieuNhapService phieuNhapService;

	@Autowired
	private PhieuXuatService phieuXuatService;

	@Override
	public Integer getCountWareHouse() {
		return khoService.getTotalItem();
	}

	@Override
	public Integer getCountProduct() {
		return productService.getTotalItem();
	}

	@Override
	public Integer getCountUser() {
		return khachHangService.getUserRegister();
	}

	@Override
	public Integer getCountPurchase() {
		return phieuNhapService.getAllTotal() + phieuXuatService.getAllTotalNotCancel();
	}

}
