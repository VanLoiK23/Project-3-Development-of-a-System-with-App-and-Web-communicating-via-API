package com.thuongmaidientu.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.CategoryDTO;
import com.thuongmaidientu.dto.HedieuhanhDTO;
import com.thuongmaidientu.dto.KhuVucKhoDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.ThongKeSellingProductDTO;
import com.thuongmaidientu.dto.XuatXuDTO;
import com.thuongmaidientu.entity.HedieuhanhEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.ProductRepository;
import com.thuongmaidientu.service.IProductService;

@Service
public class ProductService implements IProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PhienbansanphamService phienbansanphamService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ChitietPNService chitietPNService;

	@Autowired
	private ChitietPXService chitietPXService;

	@Autowired
	private ChitietSPService chitietSPService;

	@Autowired
	private CommentAndRateService commentAndRateService;

	@Autowired
	private HedieuhanhService heDieuHanhRepository;

	@Autowired
	private XuatxuService xuatxuRepository;

	@Autowired
	private KhuVucKhoService khoRepository;

	@Autowired
	private CategoryService brandRepository;

	@Override
	public List<ProductDTO> findAll(Pageable pageable) {
		/*
		 * List<ProductEntity> entities =
		 * productRepository.findAll(pageable).getContent(); return
		 * entities.stream().map(this::convertToDTO).collect(Collectors.toList());
		 */

		List<ProductEntity> entities = productRepository.findByTrash("active", pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public ProductDTO save(ProductDTO sanphammoi) {
		ProductEntity entity = convertToEntity(sanphammoi);
		ProductEntity savedEntity = productRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public int getTotalItem() {
		int numTotal = (int) productRepository.count();
		int numTrash = (int) productRepository.countByTrash("disable");

		return (int) (numTotal - numTrash);
	}

	@Override
	public int getTotalItemTrash() {
		return (int) productRepository.countByTrash("disable");
	}

	// Convert Entity -> DTO
	public ProductDTO convertToDTO(ProductEntity entity) {
		ProductDTO dto = new ProductDTO();
		dto.setId(entity.getId() != null ? entity.getId().longValue() : null);
		dto.setTenSanPham(entity.getTenSanPham());
		dto.setAlias(entity.getAlias());
		dto.setHinhAnh(entity.getHinhAnh());
		dto.setXuatXu(entity.getXuatXu().getTenXuatXu());
		dto.setXuatXuId(entity.getXuatXu().getMaXuatXu());
		dto.setDungLuongPin(entity.getDungLuongPin());
		dto.setManHinh(entity.getManHinh());
		dto.setHeDieuHanh(Optional.ofNullable(entity.getHeDieuHanh()).map(HedieuhanhEntity::getTenHeDieuHanh)
				.orElse("Không có hệ điều hành"));
		dto.setHeDieuHanhId(
				Optional.ofNullable(entity.getHeDieuHanh()).map(HedieuhanhEntity::getMaHeDieuHanh).orElse(null));
		dto.setPhienBanHDH(entity.getPhienBanHDH());
		dto.setCameraSau(entity.getCameraSau());
		dto.setCameraTruoc(entity.getCameraTruoc());
		dto.setThuongHieu(entity.getThuongHieu().getTenThuongHieu());
		dto.setThuongHieuId(entity.getThuongHieu().getMaThuongHieu());
		dto.setKhuVucKho(entity.getKhuVucKho().getTenKhuVuc());
		dto.setKhuVucKhoId(entity.getKhuVucKho().getMaKhuVuc());
		dto.setStatus(entity.getStatus());

		if (entity.getSoLuongNhap() != null) {
			dto.setSoLuongNhap(entity.getSoLuongNhap());
		}
		if (entity.getSoLuongBan() != null) {
			dto.setSoLuongBan(entity.getSoLuongBan());
		}
		if (entity.getSoLuongNhap() != null && entity.getSoLuongBan() != null) {
			dto.setSoLuongCon(entity.getSoLuongNhap() - entity.getSoLuongBan());
		}
		dto.setPromo(entity.getPromo());
		dto.setSortDesc(entity.getSortDesc());
		dto.setDetail(entity.getDetail());
		dto.setDiemTrungBinhSao(entity.getTongSao() / entity.getSoLuongDanhGia());
		dto.setStatus(entity.getStatus());
		dto.setTrash(entity.getTrash());
		dto.setTongSao(entity.getTongSao());
		dto.setSoLuongDanhGia(entity.getSoLuongDanhGia());
		dto.setCreatedDate(entity.getCreated());
		dto.setPbspList(entity.getListPhienBanSP().stream().map(phienbansanphamService::convertToDTO)
				.collect(Collectors.toList()));

		return dto;
	}

	// Convert DTO -> Entity
	public ProductEntity convertToEntity(ProductDTO dto) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formattedDateTime = LocalDateTime.now().format(formatter);

		LocalDateTime localDateTime = LocalDateTime.now();

		// Chuyển đổi LocalDateTime thành Date
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		ProductEntity entity = new ProductEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setTenSanPham(dto.getTenSanPham());
		entity.setAlias(dto.getAlias());
		entity.setHinhAnh(dto.getHinhAnh());
		if (dto.getXuatXuId() != null) {
			XuatXuDTO xx = xuatxuRepository.findById(dto.getXuatXuId());

			entity.setXuatXu(xuatxuRepository.convertToEntity(xx));
		}
		entity.setDungLuongPin(dto.getDungLuongPin());
		entity.setManHinh(dto.getManHinh());
		if (dto.getHeDieuHanhId() != null) {
			HedieuhanhDTO heDieuHanh = heDieuHanhRepository.findById(dto.getHeDieuHanhId());

			entity.setHeDieuHanh(heDieuHanhRepository.convertToEntity(heDieuHanh));
		}
		entity.setPhienBanHDH(dto.getPhienBanHDH());
		entity.setCameraSau(dto.getCameraSau());
		entity.setCameraTruoc(dto.getCameraTruoc());
		if (dto.getThuongHieuId() != null) {
			CategoryDTO k = brandRepository.findById(dto.getThuongHieuId());

			entity.setThuongHieu(brandRepository.convertToEntity(k));
		}
		if (dto.getKhuVucKhoId() != null) {
			KhuVucKhoDTO k = khoRepository.findById(dto.getKhuVucKhoId());

			entity.setKhuVucKho(khoRepository.convertToEntity(k));
		}
		entity.setSoLuongNhap(dto.getSoLuongNhap());
		entity.setSoLuongBan(dto.getSoLuongBan());
		entity.setPromo(dto.getPromo());
		entity.setSortDesc(dto.getSortDesc());
		entity.setDetail(dto.getDetail());

		entity.setSoLuongDanhGia(dto.getSoLuongDanhGia());
		entity.setTongSao(dto.getTongSao());
		entity.setModified(date);
		entity.setModifiedBy("Huy");
		entity.setCreated(date);
		entity.setCreatedBy("Huy");

		if (dto.getStatus() == null) {
			entity.setStatus(1);
		} else {
			entity.setStatus(dto.getStatus());
		}

		return entity;
	}

	@Override
	public void updateStatus(int id, int status) {
		productRepository.updateStatusById(id, status);
	}

	@Override
	public void updateTrash(int id, String trash) {
		productRepository.updateTrashById(id, trash);
	}

	@Override
	public void delete(int id) {
		List<PhienBanSanPhamDTO> phienBanSanPhamDTOs = phienbansanphamService.findAll(id);

		phienBanSanPhamDTOs.forEach(pb -> {
			cartService.deleteCart(pb.getMaPhienbansanpham().intValue());
			chitietSPService.deleteCTSP(pb.getMaPhienbansanpham().intValue());

			chitietPNService.deletePN(pb.getMaPhienbansanpham().intValue());
			chitietPXService.deletePX(pb.getMaPhienbansanpham().intValue());
			commentAndRateService.deleteComment(id);

			phienbansanphamService.deletePhienBanSP(pb.getMaPhienbansanpham().intValue());
		});

		productRepository.deleteById(Long.valueOf(id));
	}

	@Override
	public ProductDTO findById(int masp) {
		Optional<ProductEntity> entity = productRepository.findById((long) masp);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public List<ProductDTO> searchLikeName(String name) {
		List<ProductEntity> entities = productRepository.searchByName(name);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public boolean checkExistTenSanPham(String tensp) {
		return productRepository.existsByTenSanPhamAllIgnoreCase(tensp);
	}

	@Override
	public String selectImages(Integer masp) {
		return productRepository.findHinhAnhById(masp);
	}

	@Override
	public void updateSLProduct(Integer Nhap, Integer Ban, Integer masp) {

		productRepository.updateSLProduct(Nhap, Ban, Long.valueOf(masp));

	}

	@Override
	public List<ProductDTO> findTrash() {
		List<ProductEntity> entities = productRepository.findByTrash("disable");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> getALlProducts() {
		List<ProductEntity> entities = productRepository.findByTrash("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> findByKho(Integer makv) {
		List<ProductEntity> entities = productRepository.getListSPFromKho(makv);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<Object[]> findChiTietPhienBanSanPhamByPhieuNhap(Integer mapb, Integer mapn) {
		return productRepository.findChiTietPhienBanSanPhamByPhieuNhap(mapb, mapn);
	}

	@Override
	public List<Object[]> findChiTietPhienBanSanPhamByPhieuXuat(Integer mapb, Integer mapx) {
		return productRepository.findChiTietPhienBanSanPhamByPhieuXuat(mapb, mapx);
	}

	@Override
	public ProductDTO findProductByIDtosForAndroid(Integer mapb) {

		List<Object[]> results = productRepository.findInfoSP(mapb);

		ProductDTO dto = new ProductDTO();

		for (Object[] record : results) {

			dto.setTenSanPham((String) record[0]);
			dto.setKichThuocRam((Integer) record[1]);
			dto.setKichThuocRom((Integer) record[2]);
			dto.setColor((String) record[3]);
			dto.setHinhAnh((String) record[4]);
			dto.setMap((Integer) record[5]);
			;

		}

		return dto;
	}

	@Override
	public void updateReviewProductAll(Integer total, Double totalRate, Long masp) {
		productRepository.updateReviewAllProduct(total, totalRate, masp);
	}

	@Override
	public void updateReviewProduct(Integer total, Double totalRate, Long masp) {
		productRepository.updateReviewProduct(total, totalRate, masp);
	}

	@Override
	public List<ThongKeSellingProductDTO> getTopSellingProduct() {
		List<Object[]> results = productRepository.getStatisticalBuySellingProduct();

		List<ThongKeSellingProductDTO> thongKeSellingProductDTOs = new ArrayList<>();

		for (Object[] record : results) {
			ThongKeSellingProductDTO dto = new ThongKeSellingProductDTO();
			dto.setName((String) record[0]);
			dto.setSoluongban(((Number) record[1]).intValue());

			thongKeSellingProductDTOs.add(dto);
		}

		return thongKeSellingProductDTOs;
	}

	@Override
	public List<ProductDTO> getProductDTOByHighlight(String highlight, Boolean isFull) {
		List<ProductEntity> entities = isFull
				? productRepository.getProductByHighlight(highlight, Pageable.unpaged()).getContent()
				: productRepository.getProductByHighlight(highlight, PageRequest.of(0, 5)).getContent();

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> getProductDTO_TopRated(Integer minRate, Boolean isFull) {

		List<ProductEntity> entities = isFull
				? productRepository.getProductTopRate(minRate, Pageable.unpaged()).getContent()
				: productRepository.getProductTopRate(minRate, PageRequest.of(0, 5)).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> getProductDTOUnder2M(Boolean isFull) {
		List<ProductEntity> entities = isFull
				? productRepository.findProductsWithPriceSaleUnder2Million(Pageable.unpaged()).getContent()
				: productRepository.findProductsWithPriceSaleUnder2Million(PageRequest.of(0, 5)).getContent();

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<ProductDTO> getProductDTO_NewRelease(Boolean isFull) {
		List<ProductEntity> entities = isFull
				? productRepository.getProductNewlyReleasedWithinTwoWeeks(Pageable.unpaged()).getContent()
				: productRepository.getProductNewlyReleasedWithinTwoWeeks(PageRequest.of(0, 5)).getContent();

		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public ProductDTO getDetailProductDTOByAlias(String alias, Integer idPBSP) {
		List<Object[]> results = productRepository.findProductByAlias(alias, idPBSP);

		ProductDTO dto = new ProductDTO();

		for (Object[] record : results) {

			dto.setKichThuocRam((Integer) record[0]);
			dto.setKichThuocRom((Integer) record[1]);
			dto.setColor((String) record[2]);
			dto.setGiaXuat(((Number) record[3]).doubleValue());
			dto.setPriceSale(((Number) record[4]).doubleValue());
		}

		return dto;
	}

	@Override
	public List<String> findByAlphabet(String keyword) {
		return productRepository.findListProductByAlphabet("%" + keyword + "%");
	}

	@Override
	public List<ProductDTO> filterProducts(String promo, Integer star, Double minPrice, Double maxPrice,
			Integer company, String search, Pageable pageable) {
		List<ProductEntity> entities = productRepository
				.findAllByOptionalFilters(promo, star, minPrice, maxPrice, company, search, pageable).getContent();
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Integer countFilterProducts(String promo, Integer star, Double minPrice, Double maxPrice, Integer company,
			String search) {
		Long totalCount = productRepository.countFilteredProducts(promo, star, minPrice, maxPrice, company, search);
		return totalCount.intValue();
	}

	@Override
	public List<ProductDTO> findByAlias(String alias) {
		List<ProductEntity> entities = productRepository.findByAlias(alias);
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public ProductDTO getProductByIdVersionProduct(Integer maPB) {
		List<ProductEntity> productEntities = productRepository.findProductByIdVersionProduct(maPB);

		return (productEntities != null) ? convertToDTO(productEntities.get(0)) : null;
	}

	
	@Override
	public ProductDTO findInfoProductForOrderInWeb(Integer maPB) {

		List<Object[]> results = productRepository.findInfoProductForOrderInWeb(maPB);

		ProductDTO dto = new ProductDTO();

		for (Object[] record : results) {

			dto.setTenSanPham((String) record[0]);
			dto.setKichThuocRam((Integer) record[1]);
			dto.setKichThuocRom((Integer) record[2]);
			dto.setColor((String) record[3]);
			dto.setHinhAnh((String) record[4]);
			dto.setMap((Integer) record[5]);;
			dto.setSortDesc((String) record[6]);;
			dto.setGiaXuat(((Number) record[7]).doubleValue());;
			dto.setPriceSale(((Number) record[8]).doubleValue());;

		}

		return dto;
	}
}
