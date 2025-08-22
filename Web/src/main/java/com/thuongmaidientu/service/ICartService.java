package com.thuongmaidientu.service;

import java.util.List;

import com.thuongmaidientu.dto.CartDTO;
import com.thuongmaidientu.dto.CartItemDTO;

public interface ICartService {

	List<CartDTO> getAllCarts();

	CartDTO updateCart(CartDTO cart);

	void updateCartItem(Integer cartId, CartItemDTO cartItemDTO);

	void deleteCart(Integer maPB);
	
	CartDTO getCartById(Integer id);

	Integer getNumberVersionProductInCartClient(Integer maPBSP);

	Integer getIdCartActiveByClient(Integer idClient);

	List<CartItemDTO> getCartItemDTOsByClientId(Integer clientID);

	Boolean deleteByPhienbansanphamAndClientId(Integer maPB, Integer idClient);
	
	Boolean updateExistProductInCartItem(Integer maPB, Integer idClient,Integer quantity);

	Integer getQuantityExistInCartByIdVersionAndClientId(Integer maPB, Integer idClient);

	CartItemDTO getCartItemDTOsByClientIdAndIdVersion(Integer clientID,Integer idVersion);
	
	CartItemDTO findByCartItemID(Integer cartItemID);
	
	Integer isExistVersionProduct(Integer cartID,Integer maPB);
	
	List<Integer> getListVerionProductIdByCartId(Integer cartId);
	
	void deleteAllCartItemByCartId(Integer cartId);
	
	void deleteCartItemByVersionId(Integer cartId,Integer versionId);

}
