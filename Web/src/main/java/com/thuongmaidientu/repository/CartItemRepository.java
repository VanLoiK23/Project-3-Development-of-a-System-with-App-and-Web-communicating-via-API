package com.thuongmaidientu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.CartEntity;
import com.thuongmaidientu.entity.CartItemEntity;
import com.thuongmaidientu.entity.PhienBanSanPhamEntity;

import jakarta.transaction.Transactional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
	List<CartItemEntity> getByPhienbansanpham(PhienBanSanPhamEntity phienbansanpham);

	@Modifying
	@Transactional
	void deleteByPhienbansanpham(PhienBanSanPhamEntity phienbansanpham);
	
	@Modifying
	@Transactional
	void deleteByPhienbansanphamAndIdCart(PhienBanSanPhamEntity phienbansanpham, CartEntity idCart);
	
	@Modifying
	@Transactional
	void deleteByIdCart(CartEntity idCart);

	long countByIdCart(CartEntity idCart);

	@Query(value = "SELECT SUM(ci.soluong) as cart_quantity\r\n" + "                     FROM cartitems ci\r\n"
			+ "                     JOIN cart c ON ci.cart_id = c.id\r\n"
			+ "                     WHERE ci.maphienbansp = :maPBSP AND c.status = 'active'", nativeQuery = true)
	Integer getNumberInCartClient(@Param("maPBSP") Integer mapbSP);

	@Query(value = "SELECT ci.cart_item_id \r\n" + "                     FROM cartitems ci\r\n"
			+ "                     JOIN cart c ON ci.cart_id = c.id\r\n"
			+ "                     WHERE ci.maphienbansp = :maPBSP AND c.id=:cartId AND c.status = 'active'", nativeQuery = true)
	Integer isExistVersionProduct(@Param("cartId") Integer cartId, @Param("maPBSP") Integer mapbSP);

	@Query(value = "SELECT ci.maphienbansp \r\n" + "                     FROM cartitems ci\r\n"
			+ "                     JOIN cart c ON ci.cart_id = c.id\r\n"
			+ "                     WHERE c.id=:cartId AND c.status = 'active'", nativeQuery = true)
	List<Integer> getListVerionProductIdByCartId(@Param("cartId") Integer cartId);

	@Query(value = """
			SELECT sp.tensp,sp.hinhanh,version.price_sale,cartitems.maphienbansp,cartitems.soluong,cartitems.cart_id,sp.masp FROM cartitems
			     LEFT JOIN cart ON cart.id=cartitems.cart_id
			     LEFT JOIN phienbansanpham as version ON version.maphienbansp = cartitems.maphienbansp
			     LEFT JOIN sanpham as sp ON sp.masp = version.masp
			     WHERE cart.idkh = :clientId AND cart.status = 'active'
			""", nativeQuery = true)
	List<Object[]> getByClientId(@Param("clientId") Integer clientId);

	@Modifying
	@Transactional
	@Query(value = """
			DELETE FROM cartitems
			     WHERE cartitems.cart_id IN (
			     SELECT id FROM cart WHERE idkh=:clientId AND status ='active'
			     ) AND cartitems.maphienbansp=:mapb
			""", nativeQuery = true)
	int deleteByPhienbansanphamAndClientId(@Param("clientId") Integer clientId, @Param("mapb") Integer maPBSP);

	@Query(value = """
			SELECT cartItem.soluong FROM cartitems as cartItem
			     JOIN cart ON cart.id=cartItem.cart_id
			     WHERE cartItem.maphienbansp=:mapb AND cart.idkh=:clientId AND cart.status ='active'
			""", nativeQuery = true)
	Integer getQuantityExistInCartByIdVersionAndClientId(@Param("clientId") Integer clientId,
			@Param("mapb") Integer maPBSP);

	@Modifying
	@Transactional
	@Query(value = """
			UPDATE cartitems SET soluong = :number
			                WHERE cart_id IN (SELECT id FROM cart WHERE idkh = :clientId AND status = 'active') AND maphienbansp = :mapb
			""", nativeQuery = true)
	int updateQuantityProductExist(@Param("clientId") Integer clientId, @Param("mapb") Integer maPBSP,
			@Param("number") Integer number);

	@Query(value = """
			SELECT cartitems.*  FROM cartitems
			     WHERE cartitems.cart_id IN(
			     SELECT id FROM cart WHERE idkh=:clientId AND status ='active'
			     ) AND cartitems.maphienbansp=:mapb
			""", nativeQuery = true)
	List<CartItemEntity> getByPhienbansanphamAndClientId(@Param("clientId") Integer clientId,
			@Param("mapb") Integer maPBSP);
}
