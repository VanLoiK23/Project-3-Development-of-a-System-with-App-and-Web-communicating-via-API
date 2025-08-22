package com.thuongmaidientu.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuongmaidientu.dto.MauSacDTO;
import com.thuongmaidientu.dto.PhienBanSanPhamDTO;
import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.dto.RamDTO;
import com.thuongmaidientu.dto.RomDTO;
import com.thuongmaidientu.entity.NhanVienEntity;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.RamEntity;
import com.thuongmaidientu.entity.RomEntity;
import com.thuongmaidientu.repository.PhienbansanphamRepository;
import com.thuongmaidientu.service.IPhienbanspService;

@Service
public class PhienbansanphamService implements IPhienbanspService {
	@Autowired
	private PhienbansanphamRepository phienbanSPRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ColorService colorRepository;

	@Autowired
	private RamService ramRepository;

	@Autowired
	private RomService romRepository;

	@Autowired
	private ProductService prRepository;

	@Override
	public List<PhienBanSanPhamDTO> findAll(int masp) {
		ProductDTO productDTO = productService.findById(masp);

		if (productDTO == null) {
			return Collections.emptyList();
		}

		List<PhienBanSanPhamDTO> dtos = productDTO.getPbspList();

		if (dtos == null || dtos.isEmpty()) {
			return Collections.emptyList();
		}

		System.out.println(dtos);

		return dtos;

//		List<PhienBanSanPhamEntity> entities = phienbanSPRepository.selectAllPhienBanSP(masp);
//
//		if (entities == null || entities.isEmpty()) {
//			return Collections.emptyList();
//		}
//
//		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public PhienBanSanPhamDTO convertToDTO(PhienBanSanPhamEntity entity) {
		PhienBanSanPhamDTO dto = new PhienBanSanPhamDTO();
		dto.setMaPhienbansanpham(entity.getMaPhienBanSP() != null ? entity.getMaPhienBanSP().longValue() : null);
		dto.setMaSanpham(entity.getSanPham().getId() != null ? entity.getSanPham().getId().longValue() : null);
		Integer ramSize = Optional.ofNullable(entity.getRam()).map(RamEntity::getKichThuocRam).orElse(null);
		Integer ramId = Optional.ofNullable(entity.getRam()).map(RamEntity::getMaDungLuongRam).orElse(0);
		if (ramSize == null) {
			dto.setRam(null);
		} else {
			dto.setRam(ramSize.toString());
		}
		dto.setRamId(ramId);
		Integer romSize = Optional.ofNullable(entity.getRom()).map(RomEntity::getKichThuocRom).orElse(null);
		Integer romId = Optional.ofNullable(entity.getRom()).map(RomEntity::getMaDungLuongRom).orElse(0);
		if (romSize == null) {
			dto.setRom(null);
		} else {
			dto.setRom(romSize.toString());
		}
		dto.setRomId(romId);
		dto.setColor(entity.getMauSac().getTenMauSac());
		dto.setColorId(entity.getMauSac().getMaMauSac());
		dto.setGiaNhap(entity.getGiaNhap());
		dto.setGiaXuat(entity.getGiaXuat());
		dto.setSoLuongTon(entity.getSoLuongTon());
		dto.setSale(entity.getSale());
		dto.setPriceSale(entity.getPriceSale());

		return dto;
	}

	// Convert DTO -> Entity
	public PhienBanSanPhamEntity convertToEntity(PhienBanSanPhamDTO dto) {

		PhienBanSanPhamEntity entity = new PhienBanSanPhamEntity();

		if (dto.getMaPhienbansanpham() != null) {
			entity.setMaPhienBanSP(dto.getMaPhienbansanpham().intValue());
		}

		Optional.ofNullable(dto.getMaSanpham()).ifPresent(id -> {
			ProductDTO productDTO = prRepository.findById(id.intValue());

			entity.setSanPham(prRepository.convertToEntity(productDTO));
		});

		Optional.ofNullable(dto.getColorId()).ifPresent(id -> {
			MauSacDTO c = colorRepository.findById(id.intValue());

			entity.setMauSac(colorRepository.convertToEntity(c));
		});
		Optional.ofNullable(dto.getRamId()).ifPresent(id -> {
			RamDTO r = ramRepository.findById(id.intValue());

			if (r != null) {
				entity.setRam(ramRepository.convertToEntity(r));
			}
		});
		Optional.ofNullable(dto.getRomId()).ifPresent(id -> {
			RomDTO r = romRepository.findById(id.intValue());

			if (r != null) {
				entity.setRom(romRepository.convertToEntity(r));
			}
		});
		entity.setGiaNhap(dto.getGiaNhap());
		entity.setGiaXuat(dto.getGiaXuat());
		entity.setSale(dto.getSale());
		entity.setPriceSale(dto.getPriceSale());

		return entity;
	}

	@Override
	public PhienBanSanPhamDTO save(PhienBanSanPhamDTO phienbanmoi) {
		// Chuyển DTO thành entity
		PhienBanSanPhamEntity entity = convertToEntity(phienbanmoi);
		PhienBanSanPhamEntity savedEntity = phienbanSPRepository.save(entity);
		return convertToDTO(savedEntity);
	}

	@Override
	public void delete(Long mapbsp) {

		phienbanSPRepository.deleteById(mapbsp);
	}

	@Override
	public List<Integer> selectAllMaPBSPByMaSP(int masp) {
		return phienbanSPRepository.selectAllMaphienbanSPByMaSP(masp);
	}

	@Override
	public Long findVariantId(PhienBanSanPhamDTO variant, Long masp) {
		return phienbanSPRepository.findVariantId(masp, variant.getRomId(), variant.getRamId(), variant.getColorId());
	}

	@Override
	public Integer countProductCon(Integer masp) {
		return phienbanSPRepository.countNumberByMaPhienBanSP(masp);
	}

	@Override
	public void update(PhienBanSanPhamDTO pb) {

		phienbanSPRepository.updatePBSP(pb.getMaPhienbansanpham(), pb.getRomId(), pb.getRamId(), pb.getColorId(),
				pb.getGiaNhap(), pb.getGiaXuat(), pb.getSale(), pb.getPriceSale());

	}

	@Override
	public void updateSL(Integer mapb, Integer sl) {
		phienbanSPRepository.updateSLPBSP(Long.valueOf(mapb), sl);
	}

	@Override
	public Integer findMaSP(Integer mapb) {
		return phienbanSPRepository.findMaSP(mapb);
	}

	@Override
	public PhienBanSanPhamDTO findById(int id) {
		Optional<PhienBanSanPhamEntity> entity = phienbanSPRepository.findById(Long.valueOf(id));
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public void deletePhienBanSP(Integer maPB) {
		phienbanSPRepository.deleteById(Long.valueOf(maPB));
	}

	@Override
	public PhienBanSanPhamDTO findByConfig(Integer masp, Integer ram, Integer rom, String color) {
//		List<PhienBanSanPhamEntity> results = phienbanSPRepository.findByConfig(masp, ram, rom, color);
//
//		PhienBanSanPhamDTO dto = new PhienBanSanPhamDTO();
//
//		if (!results.isEmpty()) {
//			dto = convertToDTO(results.get(0));
//		}
//		
		return null;
	}

	@Override
	public void setUpQuantity(Integer mapb, Integer sl) {
		phienbanSPRepository.setUpSLPBSP(Long.valueOf(mapb), sl);
	}

}
