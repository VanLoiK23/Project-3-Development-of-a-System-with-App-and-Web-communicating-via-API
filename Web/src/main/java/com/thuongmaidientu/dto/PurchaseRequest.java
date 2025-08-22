package com.thuongmaidientu.dto;


import java.util.List;

import lombok.Data;

@Data
public class PurchaseRequest {
	public MomoDTO momo;
	public PhieuXuatDTO donhang;
	public List<ChiTietPhieuXuatDTO> chitietphieuxuatList;
}
