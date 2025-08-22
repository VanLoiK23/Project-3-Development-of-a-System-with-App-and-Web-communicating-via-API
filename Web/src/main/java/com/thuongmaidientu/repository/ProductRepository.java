package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.dto.ProductDTO;
import com.thuongmaidientu.entity.PhieuXuatEntity;
import com.thuongmaidientu.entity.ProductEntity;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	Page<ProductEntity> findByTrash(String trash, Pageable pageable);

	List<ProductEntity> findByTrash(String trash);

	long countByTrash(String trash);

	@Modifying
	@Transactional
	@Query("UPDATE ProductEntity p SET p.status = :status WHERE p.id = :id")
	int updateStatusById(@Param("id") int id, @Param("status") int status);

	@Modifying
	@Transactional
	@Query("UPDATE ProductEntity p SET p.trash = :trash WHERE p.id = :id")
	int updateTrashById(@Param("id") int id, @Param("trash") String status);

	// cau sql thuan nativeQuery
	@Query(value = "SELECT  DISTINCT  * FROM sanpham WHERE LOWER(tensp) LIKE LOWER(CONCAT('%', :name, '%')) LIMIT 10", nativeQuery = true)
	List<ProductEntity> searchByName(@Param("name") String name);

	@Query(value = "SELECT DISTINCT  *  FROM sanpham as sp WHERE "
			+ "                                                                   sp.khuvuckho=:makv "
			+ "                                                                   LIMIT 0,15", nativeQuery = true)
	List<ProductEntity> getListSPFromKho(@Param("makv") int makv);

	boolean existsByTenSanPhamAllIgnoreCase(String tensp);

	@Query("SELECT p.hinhAnh FROM ProductEntity p WHERE p.id = :id")
	String findHinhAnhById(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE sanpham SET soluongnhap=:slN,soluongban=:slB WHERE masp=:masp", nativeQuery = true)
	void updateSLProduct(@Param("slN") Integer slN, @Param("slB") Integer slB, @Param("masp") Long masp);

	@Modifying
	@Transactional
	@Query(value = "UPDATE sanpham SET soluongdanhgia=:total,tongsao=:totalRate WHERE masp=:masp", nativeQuery = true)
	void updateReviewAllProduct(@Param("total") Integer total, @Param("totalRate") Double totalRate,
			@Param("masp") Long masp);

	@Modifying
	@Transactional
	@Query(value = "UPDATE sanpham SET soluongdanhgia=soluongdanhgia+:total,tongsao=tongsao+:totalRate WHERE masp=:masp", nativeQuery = true)
	void updateReviewProduct(@Param("total") Integer total, @Param("totalRate") Double totalRate,
			@Param("masp") Long masp);

	@Query(value = "SELECT pb.maphienbansp, tensp, kichthuocram, kichthuocrom, tenmausac, gianhap, maimei "
			+ "FROM phienbansanpham as pb " + "LEFT JOIN dungluongram as ram ON pb.ram = ram.madungluongram "
			+ "LEFT JOIN dungluongrom as rom ON pb.rom = rom.madungluongrom "
			+ "LEFT JOIN mausac ON pb.mausac = mausac.mamausac " + "LEFT JOIN sanpham ON pb.masp = sanpham.masp "
			+ "LEFT JOIN ctsanpham ON pb.maphienbansp = ctsanpham.maphienbansp "
			+ "WHERE pb.maphienbansp =:mapb AND ctsanpham.maphieunhap=:mapn", nativeQuery = true)
	List<Object[]> findChiTietPhienBanSanPhamByPhieuNhap(@Param("mapb") Integer maPhienBanSp,
			@Param("mapn") Integer mapn);

	@Query(value = "SELECT pb.maphienbansp, tensp, kichthuocram, kichthuocrom, tenmausac, price_sale, maimei "
			+ "FROM phienbansanpham as pb " + "LEFT JOIN dungluongram as ram ON pb.ram = ram.madungluongram "
			+ "LEFT JOIN dungluongrom as rom ON pb.rom = rom.madungluongrom "
			+ "LEFT JOIN mausac ON pb.mausac = mausac.mamausac " + "LEFT JOIN sanpham ON pb.masp = sanpham.masp "
			+ "LEFT JOIN ctsanpham ON pb.maphienbansp = ctsanpham.maphienbansp "
			+ "WHERE pb.maphienbansp =:mapb AND ctsanpham.maphieuxuat=:mapx", nativeQuery = true)
	List<Object[]> findChiTietPhienBanSanPhamByPhieuXuat(@Param("mapb") Integer maPhienBanSp,
			@Param("mapx") Integer mapx);

	// danh cho order ben android
	@Query(value = "SELECT sp.tensp, ram.kichthuocram, rom.kichthuocrom, color.tenmausac, sp.hinhanh,sp.masp "
			+ "FROM sanpham as sp " + "LEFT JOIN phienbansanpham as pb ON pb.masp = sp.masp "
			+ "LEFT JOIN dungluongram as ram ON pb.ram = ram.madungluongram "
			+ "LEFT JOIN dungluongrom as rom ON pb.rom = rom.madungluongrom "
			+ "LEFT JOIN mausac as color ON pb.mausac = color.mamausac "
			+ "WHERE pb.maphienbansp =:mapb", nativeQuery = true)
	List<Object[]> findInfoSP(@Param("mapb") Integer maPhienBanSp);

	@Query(value = "  SELECT sp.tensp AS product_name, sp.soluongban AS total_sold,sp.hinhanh AS img "
			+ "        FROM sanpham sp" + "        ORDER BY total_sold DESC " + "        LIMIT 5;", nativeQuery = true)
	List<Object[]> getStatisticalBuySellingProduct();

	@Query(value = """
			SELECT
			    sp.*, version.giaxuat, version.price_sale,
			    sp.soluongdanhgia AS total_reviews,
			    COALESCE(ROUND((sp.tongsao / NULLIF(sp.soluongdanhgia, 0)), 1), 0) AS average_rating,
			    JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].value')) AS promo_value
			FROM
			    sanpham AS sp
			JOIN
			    phienbansanpham AS version ON sp.masp = version.masp
			WHERE
			    JSON_SEARCH(sp.promo, 'one', ?1) IS NOT NULL
			GROUP BY
			    sp.masp
			ORDER BY
			    total_reviews DESC
			""", countQuery = """
			    SELECT COUNT(*) FROM sanpham
			    WHERE JSON_SEARCH(promo, 'one', ?1) IS NOT NULL
			""", nativeQuery = true)
	Page<ProductEntity> getProductByHighlight(String highlight, Pageable pageable);

	@Query(value = """
			SELECT
			    sp.*,
			    version.giaxuat, version.price_sale,
			    sp.soluongdanhgia AS total_reviews,
			    ROUND(AVG(comment_and_rate.rate), 1) AS average_rating,
			    JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].name')) AS promo_name,
			    version.giaxuat - (
			        CAST(IFNULL(JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].value')), '0') AS DECIMAL(10,2)) * version.giaxuat / 100
			    ) AS price_sale
			FROM sanpham AS sp
			JOIN phienbansanpham AS version ON sp.masp = version.masp
			LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
			GROUP BY sp.masp
			HAVING average_rating >= ?1
			ORDER BY total_reviews DESC
			""", countQuery = "SELECT COUNT(DISTINCT sp.masp) FROM sanpham AS sp JOIN phienbansanpham AS version ON sp.masp = version.masp LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp", nativeQuery = true)
	Page<ProductEntity> getProductTopRate(double minRating, Pageable pageable);

	@Query(value = """
			SELECT
			    sp.*,
			    version.giaxuat, version.price_sale,
			    sp.soluongdanhgia AS total_reviews,
			    ROUND(AVG(comment_and_rate.rate), 1) AS average_rating,
			    JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].name')) AS promo_name,
			    version.giaxuat - (
			        CAST(IFNULL(JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].value')), '0') AS DECIMAL(10,2)) * version.giaxuat / 100
			    ) AS price_sale
			FROM sanpham AS sp
			JOIN phienbansanpham AS version ON sp.masp = version.masp
			LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
			WHERE sp.created >= NOW() - INTERVAL 14 DAY
			GROUP BY sp.masp
			ORDER BY total_reviews DESC
			""", countQuery = """
			    SELECT COUNT(DISTINCT sp.masp)
			    FROM sanpham AS sp
			    JOIN phienbansanpham AS version ON sp.masp = version.masp
			    WHERE sp.created >= NOW() - INTERVAL 14 DAY
			""", nativeQuery = true)
	Page<ProductEntity> getProductNewlyReleasedWithinTwoWeeks(Pageable pageable);

	@Query(value = """
			   SELECT
			    sp.*,
			    version.giaxuat, version.price_sale,
			    sp.soluongdanhgia AS total_reviews,
			    ROUND(AVG(comment_and_rate.rate), 1) AS average_rating,
			    JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].name')) AS promo_name,
			    version.giaxuat - (
			        CAST(IFNULL(JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].value')), '0') AS DECIMAL(10,2)) * version.giaxuat / 100
			    ) AS price_sale
			FROM sanpham AS sp
			JOIN phienbansanpham AS version ON sp.masp = version.masp
			LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
			GROUP BY sp.masp
			HAVING version.price_sale < 2000000
			ORDER BY total_reviews DESC
			""", countQuery = "SELECT COUNT(DISTINCT sp.masp) FROM sanpham AS sp JOIN phienbansanpham AS version ON sp.masp = version.masp WHERE version.price_sale < 2000000", nativeQuery = true)
	Page<ProductEntity> findProductsWithPriceSaleUnder2Million(Pageable pageable);

	@Query(value = "SELECT ram.kichthuocram, rom.kichthuocrom, color.tenmausac,pb.giaxuat,pb.price_sale "
			+ "FROM sanpham as sp " + "LEFT JOIN phienbansanpham as pb ON pb.masp = sp.masp "
			+ "LEFT JOIN dungluongram as ram ON pb.ram = ram.madungluongram "
			+ "LEFT JOIN dungluongrom as rom ON pb.rom = rom.madungluongrom "
			+ "LEFT JOIN mausac as color ON pb.mausac = color.mamausac "
			+ "WHERE sp.alias=?1 AND pb.maphienbansp =?2", nativeQuery = true)
	List<Object[]> findProductByAlias(String alias, Integer idpbsp);

	@Query(value = """
			SELECT sp.tensp FROM sanpham as sp
			WHERE sp.tensp LIKE ?1
			""", nativeQuery = true)
	List<String> findListProductByAlphabet(String alphabet);

	@Query(value = """
			SELECT * FROM (
			    SELECT
			        sp.*,
			        version.giaxuat,
			        MIN(version.price_sale) AS min_price,
			        sp.soluongdanhgia AS total_reviews,
			        ROUND(AVG(comment_and_rate.rate), 1) AS average_rating,
			        JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].name')) AS promo_name,
			        version.giaxuat - (
			            CAST(IFNULL(JSON_UNQUOTE(JSON_EXTRACT(sp.promo, '$[0].value')), '0') AS DECIMAL(10,2)) * version.giaxuat / 100
			        ) AS calculated_price_sale
			    FROM sanpham AS sp
			    JOIN phienbansanpham AS version ON sp.masp = version.masp
			    LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
			    WHERE (:promo IS NULL OR (JSON_SEARCH(sp.promo, 'one', :promo) IS NOT NULL  AND (:promo != 'moiramat' OR sp.created >= DATE_SUB(CURDATE(), INTERVAL 14 DAY))))
			      AND (:minPrice IS NULL OR :maxPrice IS NULL OR version.price_sale BETWEEN :minPrice AND :maxPrice)
			      AND (:company IS NULL OR sp.thuonghieu = :company)
			      AND (:search IS NULL OR sp.tensp LIKE LOWER(CONCAT('%', :search, '%')))
			    GROUP BY sp.masp
			    HAVING (:star IS NULL OR ROUND(AVG(comment_and_rate.rate), 1) >= :star)
			) AS sub
			""", countQuery = """
			SELECT COUNT(*) FROM (
			    SELECT sp.masp
			    FROM sanpham AS sp
			    JOIN phienbansanpham AS version ON sp.masp = version.masp
			    LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
				WHERE (:promo IS NULL OR (JSON_SEARCH(sp.promo, 'one', :promo) IS NOT NULL  AND (:promo != 'moiramat' OR sp.created >= DATE_SUB(CURDATE(), INTERVAL 14 DAY))))
			      AND (:minPrice IS NULL OR :maxPrice IS NULL OR version.price_sale BETWEEN :minPrice AND :maxPrice)
			      AND (:company IS NULL OR sp.thuonghieu = :company)
			      AND (:search IS NULL OR sp.tensp LIKE LOWER(CONCAT('%', :search, '%')))
			    GROUP BY sp.masp
			    HAVING (:star IS NULL OR ROUND(AVG(comment_and_rate.rate), 1) >= :star)
			) AS sub_count
			""", nativeQuery = true)
	Page<ProductEntity> findAllByOptionalFilters(@Param("promo") String promo, @Param("star") Integer star,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("company") Integer company,
			@Param("search") String search, Pageable pageable);

	@Query(value = """
			SELECT COUNT(*) FROM (
			    SELECT sp.masp
			    FROM sanpham AS sp
			    JOIN phienbansanpham AS version ON sp.masp = version.masp
			    LEFT JOIN comment_and_rate ON sp.masp = comment_and_rate.id_sp
				WHERE (:promo IS NULL OR (JSON_SEARCH(sp.promo, 'one', :promo) IS NOT NULL  AND (:promo != 'moiramat' OR sp.created >= DATE_SUB(CURDATE(), INTERVAL 14 DAY))))
			      AND (:minPrice IS NULL OR :maxPrice IS NULL OR version.price_sale BETWEEN :minPrice AND :maxPrice)
			      AND (:company IS NULL OR sp.thuonghieu = :company)
			      AND (:search IS NULL OR sp.tensp LIKE LOWER(CONCAT('%', :search, '%')))
			    GROUP BY sp.masp
			    HAVING (:star IS NULL OR ROUND(AVG(comment_and_rate.rate), 1) >= :star)
			) AS counted
			""", nativeQuery = true)
	Long countFilteredProducts(@Param("promo") String promo, @Param("star") Integer star,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("company") Integer company,
			@Param("search") String search);

	List<ProductEntity> findByAlias(String alias);

	@Query(value = """
			SELECT sp.* FROM sanpham as sp
			LEFT JOIN phienbansanpham as version ON version.masp=sp.masp
			WHERE version.maphienbansp=:maPB
			""", nativeQuery = true)
	List<ProductEntity> findProductByIdVersionProduct(@Param("maPB") Integer maPB);
	
	@Query(value = "SELECT sp.tensp, ram.kichthuocram, rom.kichthuocrom, color.tenmausac, sp.hinhanh,sp.masp,sp.sortDesc,pb.giaxuat,pb.price_sale "
			+ "FROM sanpham as sp " + "LEFT JOIN phienbansanpham as pb ON pb.masp = sp.masp "
			+ "LEFT JOIN dungluongram as ram ON pb.ram = ram.madungluongram "
			+ "LEFT JOIN dungluongrom as rom ON pb.rom = rom.madungluongrom "
			+ "LEFT JOIN mausac as color ON pb.mausac = color.mamausac "
			+ "WHERE pb.maphienbansp =:mapb", nativeQuery = true)
	List<Object[]> findInfoProductForOrderInWeb(@Param("mapb") Integer maPhienBanSp);
}
