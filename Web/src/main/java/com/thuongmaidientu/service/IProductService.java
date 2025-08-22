package com.thuongmaidientu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongKeSellingProductDTO;

public interface IProductService {

	List<ProductDTO> findAll(Pageable pageable);

	List<ProductDTO> findTrash();

	ProductDTO findById(int masp);

	List<ProductDTO> searchLikeName(String name);

	ProductDTO save(ProductDTO sanphammoi);

	int getTotalItem();

	int getTotalItemTrash();

	void updateStatus(int id, int status);

	void updateTrash(int id, String status);

	void delete(int id);

	boolean checkExistTenSanPham(String tensp);

	String selectImages(Integer masp);

	void updateSLProduct(Integer Nhap, Integer Ban, Integer masp);

	List<ProductDTO> getALlProducts();

	List<ProductDTO> findByKho(Integer makv);

	List<Object[]> findChiTietPhienBanSanPhamByPhieuNhap(Integer mapb, Integer mapn);

	List<Object[]> findChiTietPhienBanSanPhamByPhieuXuat(Integer mapb, Integer mapx);

	ProductDTO findProductByIDtosForAndroid(Integer mapb);

	void updateReviewProductAll(Integer total, Double totalRate, Long masp);

	void updateReviewProduct(Integer total, Double totalRate, Long masp);

	List<ThongKeSellingProductDTO> getTopSellingProduct();

	List<ProductDTO> getProductDTOByHighlight(String highlight, Boolean isFull);

	List<ProductDTO> getProductDTO_TopRated(Integer minRate, Boolean isFull);

	List<ProductDTO> getProductDTO_NewRelease(Boolean isFull);

	List<ProductDTO> getProductDTOUnder2M(Boolean isFull);

	ProductDTO getDetailProductDTOByAlias(String alias, Integer idPBSP);

	List<String> findByAlphabet(String keyword);

	List<ProductDTO> filterProducts(String promo, Integer star, Double minPrice, Double maxPrice,Integer company,String search, Pageable pageable);
	
	Integer countFilterProducts(String promo, Integer star, Double minPrice, Double maxPrice,Integer company,String search);

	List<ProductDTO> findByAlias(String alias);
	
	ProductDTO getProductByIdVersionProduct(Integer maPB);
	
	ProductDTO findInfoProductForOrderInWeb(Integer maPB);
}
