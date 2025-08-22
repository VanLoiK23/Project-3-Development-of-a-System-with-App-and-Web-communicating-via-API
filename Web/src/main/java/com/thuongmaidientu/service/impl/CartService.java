package com.thuongmaidientu.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.thuongmaidientu.dto.CartDTO;
import com.thuongmaidientu.dto.CartItemDTO;
import com.thuongmaidientu.entity.CartEntity;
import com.thuongmaidientu.entity.CartItemEntity;
import com.thuongmaidientu.entity.CategoryEntity;
import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.MauSacEntity;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;
import com.thuongmaidientu.entity.ProductEntity;
import com.thuongmaidientu.repository.CartItemRepository;
import com.thuongmaidientu.repository.CartRepository;
import com.thuongmaidientu.service.ICartService;

@Service
public class CartService implements ICartService {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public List<CartDTO> getAllCarts() {
		List<CartEntity> entities = cartRepository.findByStatus("active");
		return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private CartDTO convertToDTO(CartEntity entity) {
		CartDTO dto = new CartDTO();
		dto.setId(entity.getId() != null ? entity.getId().intValue() : null);
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		dto.setStatus(entity.getStatus());
		dto.setCartItems(entity.getCart().stream().map(this::convertToDTOCartItem).collect(Collectors.toList()));
		dto.setIdkh(entity.getKhachHang().getMaKhachHang());

		return dto;
	}

	public CartEntity convertToEntity(CartDTO dto) {

		CartEntity entity = new CartEntity();
		entity.setId(dto.getId() != null ? dto.getId().intValue() : null);
		entity.setStatus(dto.getStatus());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());
		KhachHangEntity etEntity = new KhachHangEntity();
		etEntity.setMaKhachHang(dto.getIdkh());

		entity.setKhachHang(etEntity);
//		entity.setCart(dto.getCartItems().stream().map(this::convertToCartItemEntity).collect(Collectors.toList()));

		return entity;
	}

	private CartItemDTO convertToDTOCartItem(CartItemEntity entity) {
		CartItemDTO dto = new CartItemDTO();
		dto.setCart_id(entity.getIdCart().getId());
		dto.setCart_item_id(entity.getId());
		dto.setMaphienbansp(entity.getPhienbansanpham().getMaPhienBanSP());
		dto.setSoluong(entity.getSoluong());
		dto.setMasp(entity.getPhienbansanpham().getSanPham().getId());

		return dto;
	}

	public CartItemEntity convertToCartItemEntity(CartItemDTO dto) {

		CartItemEntity entity = new CartItemEntity();

		CartEntity cartEntity = new CartEntity();
		cartEntity.setId(dto.getCart_id());
		entity.setIdCart(cartEntity);

		PhienBanSanPhamEntity phienBanSanPhamEntity = new PhienBanSanPhamEntity();
		phienBanSanPhamEntity.setMaPhienBanSP(dto.getMaphienbansp());
		entity.setPhienbansanpham(phienBanSanPhamEntity);

		entity.setId(dto.getCart_item_id() != null ? dto.getCart_item_id().intValue() : null);
		entity.setSoluong(dto.getSoluong());

		return entity;
	}

	@Override
	public CartDTO updateCart(CartDTO cart) {
		CartEntity cartEntity = cartRepository.save(convertToEntity(cart));

		return convertToDTO(cartEntity);
	}

	@Override
	public void updateCartItem(Integer cartId, CartItemDTO cartItemDTO) {
		cartItemDTO.setCart_id(cartId);

		cartItemRepository.save(convertToCartItemEntity(cartItemDTO));
	}

	@Override
	public void deleteCart(Integer maPB) {
		PhienBanSanPhamEntity phienBanSanPhamEntity = new PhienBanSanPhamEntity();
		phienBanSanPhamEntity.setMaPhienBanSP(maPB);
		List<CartItemEntity> cartItemEntities = cartItemRepository.getByPhienbansanpham(phienBanSanPhamEntity);

		cartItemEntities.forEach(cartItem -> {

			cartItemRepository.deleteByPhienbansanpham(phienBanSanPhamEntity);

			CartEntity cartEntity = new CartEntity();
			cartEntity.setId(cartItem.getIdCart().getId());

			if (cartItemRepository.countByIdCart(cartEntity) == 0) {
				cartRepository.delete(cartEntity);
			}
		});
	}

	@Override
	public Integer getNumberVersionProductInCartClient(Integer maPBSP) {
		return cartItemRepository.getNumberInCartClient(maPBSP);
	}

	@Override
	public Integer getIdCartActiveByClient(Integer idClient) {
		return cartRepository.findIdCartActiveByClient(idClient);
	}

	@Override
	public List<CartItemDTO> getCartItemDTOsByClientId(Integer clientID) {
		List<Object[]> resultList = cartItemRepository.getByClientId(clientID);

		List<CartItemDTO> cartItemDTOs = new ArrayList<CartItemDTO>();

		for (Object[] record : resultList) {
			CartItemDTO cartItemDTO = new CartItemDTO();

			String imgListString = (String) record[1];
			String[] images = imgListString.split(",");
			String firstImage = images.length > 0 ? images[0] : "";

			cartItemDTO.setTensp((String) record[0]);
			cartItemDTO.setImg(firstImage);
			cartItemDTO.setPriceSale(((Number) record[2]).intValue());
			cartItemDTO.setMaphienbansp((Integer) record[3]);
			cartItemDTO.setSoluong((Integer) record[4]);
			cartItemDTO.setCart_id((Integer) record[5]);
			cartItemDTO.setMasp((Integer) record[6]);

			cartItemDTOs.add(cartItemDTO);
		}

		return cartItemDTOs;
	}

	@Override
	public Boolean deleteByPhienbansanphamAndClientId(Integer maPB, Integer idClient) {
		return (cartItemRepository.deleteByPhienbansanphamAndClientId(idClient, maPB) > 0);
	}

	@Override
	public Integer getQuantityExistInCartByIdVersionAndClientId(Integer maPB, Integer idClient) {
		return cartItemRepository.getQuantityExistInCartByIdVersionAndClientId(idClient, maPB);
	}

	@Override
	public CartItemDTO getCartItemDTOsByClientIdAndIdVersion(Integer clientID, Integer idVersion) {
		List<CartItemEntity> cartItemEntities = cartItemRepository.getByPhienbansanphamAndClientId(clientID, idVersion);

		return (cartItemEntities != null) ? convertToDTOCartItem(cartItemEntities.get(0)) : null;
	}

	@Override
	public Boolean updateExistProductInCartItem(Integer maPB, Integer idClient, Integer quantity) {
		return (cartItemRepository.updateQuantityProductExist(idClient, maPB, quantity) > 0);
	}

	@Override
	public CartDTO getCartById(Integer id) {
		Optional<CartEntity> entity = cartRepository.findById((long) id);
		return entity.map(this::convertToDTO).orElse(null);
	}

	@Override
	public CartItemDTO findByCartItemID(Integer cartItemID) {
		Optional<CartItemEntity> entity = cartItemRepository.findById((long) cartItemID);
		return entity.map(this::convertToDTOCartItem).orElse(null);
	}

	@Override
	public Integer isExistVersionProduct(Integer cartID, Integer maPB) {
		return cartItemRepository.isExistVersionProduct(cartID, maPB);
	}

	@Override
	public List<Integer> getListVerionProductIdByCartId(Integer cartId) {
		return cartItemRepository.getListVerionProductIdByCartId(cartId);
	}

	@Override
	public void deleteAllCartItemByCartId(Integer cartId) {

		CartEntity cartEntity = new CartEntity();

		cartEntity.setId(cartId);

		cartItemRepository.deleteByIdCart(cartEntity);
	}

	@Override
	public void deleteCartItemByVersionId(Integer cartId, Integer versionId) {
		CartEntity cartEntity = new CartEntity();
		cartEntity.setId(cartId);
		
		PhienBanSanPhamEntity phienBanSanPhamEntity=new PhienBanSanPhamEntity();	
		phienBanSanPhamEntity.setMaPhienBanSP(versionId);
	
		cartItemRepository.deleteByPhienbansanphamAndIdCart(phienBanSanPhamEntity, cartEntity);
	}

}
