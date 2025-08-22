package com.thuongmaidientu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmaidientu.entity.KhachHangEntity;
import com.thuongmaidientu.entity.PhieuXuatEntity;

import jakarta.transaction.Transactional;

public interface PhieuXuatRepository extends JpaRepository<PhieuXuatEntity, Long> {

	List<PhieuXuatEntity> findBySave(String save);

	Page<PhieuXuatEntity> findBySave(String trash, Pageable pageable);

	long countBySave(String save);

	@Modifying
	@Transactional
	@Query("UPDATE PhieuXuatEntity p SET p.save = :trash WHERE p.id = :id")
	int updateSaveById(@Param("id") int id, @Param("trash") String status);

	@Modifying
	@Transactional
	@Query("UPDATE PhieuXuatEntity p SET p.status = :st WHERE p.id = :id")
	int updateStatus(@Param("id") int id, @Param("st") Integer st);

	@Query("""
			    SELECT p FROM PhieuXuatEntity p
			    WHERE (:fromDate IS NULL OR DATE(p.thoiGian) >= :fromDate)
			    AND (:st IS NULL OR p.status = :st) AND save='active'
			""")
	List<PhieuXuatEntity> findAllByOptionalFilters(@Param("fromDate") Date fromDate, @Param("st") Integer st,
			Pageable pageable);

	@Query("""
			    SELECT COUNT(p) FROM PhieuXuatEntity p
			    WHERE (:fromDate IS NULL OR DATE(p.thoiGian) >= :fromDate)
			    AND (:st IS NULL OR p.status = :st) AND save='active'
			""")
	int countByOptionalFilters(@Param("fromDate") Date fromDate, @Param("st") Integer st);

	List<PhieuXuatEntity> findByKhachHangMua(KhachHangEntity khachHangMua);

	@Query(value = """
			    SELECT ship.hovaten, ship.sodienthoai, ship.street_name, ship.district, ship.city
			    FROM thongtingiaohang ship
			    LEFT JOIN phieuxuat px ON px.cart_shipping = ship.id
			    WHERE px.maphieuxuat=:id
			""", nativeQuery = true)

	List<Object[]> getInfoShip(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE phieuxuat p SET p.status = -2,p.feeback=:reason WHERE p.maphieuxuat = :id", nativeQuery = true)
	int updateStatusCancle(@Param("id") int id, @Param("reason") String reason);

	@Query(value = "SELECT p.makh FROM  phieuxuat p WHERE p.maphieuxuat = ?1", nativeQuery = true)
	int getUserIdByOrder(int orderId);

	@Query(value = "SELECT COUNT(*) FROM phieuxuat WHERE status!=-1 AND status !=-3", nativeQuery = true)
	Integer getCountOrderNotCancel();

	@Query(value = "SELECT CASE WHEN DAYOFWEEK(thoigian) = 1 THEN 'Chủ nhật' "
			+ "       WHEN DAYOFWEEK(thoigian) = 2 THEN 'Thứ 2'   " + "       WHEN DAYOFWEEK(thoigian) = 3 THEN 'Thứ 3'"
			+ "       WHEN DAYOFWEEK(thoigian) = 4 THEN 'Thứ 4'" + "       WHEN DAYOFWEEK(thoigian) = 5 THEN 'Thứ 5'"
			+ "       WHEN DAYOFWEEK(thoigian) = 6 THEN 'Thứ 6'" + "        WHEN DAYOFWEEK(thoigian) = 7 THEN 'Thứ 7'"
			+ "   END AS day_of_week, " + "   COUNT(maphieuxuat) AS count " + "    FROM phieuxuat "
			+ "    WHERE status != -3 AND status != -1 " + "  AND YEARWEEK(thoigian, 1) = YEARWEEK(CURDATE(), 1)"
			+ "   GROUP BY DAYOFWEEK(thoigian)", nativeQuery = true)
	List<Object[]> getStatisticalFollowWeek();

	@Query(value = " SELECT " + "    CASE " + "        WHEN CEIL(DAY(thoigian) / 7) = 1 THEN 'Tuần 1'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 2 THEN 'Tuần 2'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 3 THEN 'Tuần 3'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 4 THEN 'Tuần 4'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 5 THEN 'Tuần 5'" + "    END AS week_of_month, "
			+ "    COUNT(maphieuxuat) AS count " + "FROM phieuxuat " + "WHERE status NOT IN (-3, -1) "
			+ "  AND YEAR(thoigian) = YEAR(CURDATE()) " + "  AND MONTH(thoigian) = MONTH(CURDATE()) "
			+ "GROUP BY CEIL(DAY(thoigian) / 7) " + "ORDER BY CEIL(DAY(thoigian) / 7);", nativeQuery = true)
	List<Object[]> getStatisticalFollowMonth();

	@Query(value = "   SELECT " + "            CASE " + "                WHEN DAYOFWEEK(thoigian) = 1 THEN 'Chủ nhật'"
			+ "                WHEN DAYOFWEEK(thoigian) = 2 THEN 'Thứ 2'"
			+ "                WHEN DAYOFWEEK(thoigian) = 3 THEN 'Thứ 3'"
			+ "                WHEN DAYOFWEEK(thoigian) = 4 THEN 'Thứ 4'"
			+ "                WHEN DAYOFWEEK(thoigian) = 5 THEN 'Thứ 5'"
			+ "                WHEN DAYOFWEEK(thoigian) = 6 THEN 'Thứ 6'"
			+ "                WHEN DAYOFWEEK(thoigian) = 7 THEN 'Thứ 7'" + "            END AS day_of_week, "
			+ "            SUM(tongtien) AS total " + "        FROM phieuxuat "
			+ "        WHERE status != -3 AND status != -1 "
			+ "        AND YEARWEEK(thoigian, 1) = YEARWEEK(CURDATE(), 1) "
			+ "        GROUP BY DAYOFWEEK(thoigian)", nativeQuery = true)
	List<Object[]> getStatisticalDoanhThuFollowWeek();

	@Query(value = """
			SELECT
			    CASE MONTH(thoigian)
			        WHEN 1 THEN 'Tháng 1'
			        WHEN 2 THEN 'Tháng 2'
			        WHEN 3 THEN 'Tháng 3'
			        WHEN 4 THEN 'Tháng 4'
			        WHEN 5 THEN 'Tháng 5'
			        WHEN 6 THEN 'Tháng 6'
			        WHEN 7 THEN 'Tháng 7'
			        WHEN 8 THEN 'Tháng 8'
			        WHEN 9 THEN 'Tháng 9'
			        WHEN 10 THEN 'Tháng 10'
			        WHEN 11 THEN 'Tháng 11'
			        WHEN 12 THEN 'Tháng 12'
			    END AS month_of_year,
			    COUNT(maphieuxuat) AS count
			FROM phieuxuat
			WHERE status NOT IN (-3, -1)
			  AND YEAR(thoigian) = YEAR(CURDATE())
			GROUP BY MONTH(thoigian)
			ORDER BY MONTH(thoigian)
			""", nativeQuery = true)
	List<Object[]> getStatisticalOrderFollowYear();

	@Query(value = """
			SELECT
			    CASE MONTH(thoigian)
			        WHEN 1 THEN 'Tháng 1'
			        WHEN 2 THEN 'Tháng 2'
			        WHEN 3 THEN 'Tháng 3'
			        WHEN 4 THEN 'Tháng 4'
			        WHEN 5 THEN 'Tháng 5'
			        WHEN 6 THEN 'Tháng 6'
			        WHEN 7 THEN 'Tháng 7'
			        WHEN 8 THEN 'Tháng 8'
			        WHEN 9 THEN 'Tháng 9'
			        WHEN 10 THEN 'Tháng 10'
			        WHEN 11 THEN 'Tháng 11'
			        WHEN 12 THEN 'Tháng 12'
			    END AS month_of_year,
			    COUNT(maphieuxuat) AS count
			FROM phieuxuat
			WHERE status NOT IN (-3, -1)
			  AND YEAR(thoigian) = YEAR(CURDATE())
			GROUP BY MONTH(thoigian)
			ORDER BY MONTH(thoigian)
			""", nativeQuery = true)
	List<Object[]> getStatisticalPurchaseFollowYear();

	@Query(value = " SELECT " + "    CASE " + "       WHEN CEIL(DAY(thoigian) / 7) = 1 THEN 'Tuần 1'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 2 THEN 'Tuần 2'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 3 THEN 'Tuần 3'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 4 THEN 'Tuần 4'"
			+ "        WHEN CEIL(DAY(thoigian) / 7) = 5 THEN 'Tuần 5'" + "    END AS week_of_month, "
			+ "    SUM(tongtien) AS total " + "FROM phieuxuat " + "WHERE status NOT IN (-3, -1) "
			+ "  AND YEAR(thoigian) = YEAR(CURDATE()) " + "  AND MONTH(thoigian) = MONTH(CURDATE()) "
			+ "GROUP BY CEIL(DAY(thoigian) / 7) " + "ORDER BY CEIL(DAY(thoigian) / 7);", nativeQuery = true)
	List<Object[]> getStatisticalDoanhThuFollowMonth();

	@Query(value = """
			    SELECT
			        CASE
			            WHEN CEIL(DAY(thoigian) / 7) = 1 THEN 'Tuần 1'
			            WHEN CEIL(DAY(thoigian) / 7) = 2 THEN 'Tuần 2'
			            WHEN CEIL(DAY(thoigian) / 7) = 3 THEN 'Tuần 3'
			            WHEN CEIL(DAY(thoigian) / 7) = 4 THEN 'Tuần 4'
			            WHEN CEIL(DAY(thoigian) / 7) = 5 THEN 'Tuần 5'
			        END AS week_of_month,
			        SUM(tongtien) AS total
			    FROM phieuxuat
			    WHERE status NOT IN (-3, -1)
			      AND thoigian >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL DAY(CURDATE()) - 1 DAY), INTERVAL 1 MONTH)
			      AND thoigian < DATE_SUB(CURDATE(), INTERVAL DAY(CURDATE()) - 1 DAY)
			    GROUP BY CEIL(DAY(thoigian) / 7)
			    ORDER BY CEIL(DAY(thoigian) / 7)
			""", nativeQuery = true)
	List<Object[]> getStatisticalDoanhThuFollowPreviousMonthFromToday();

	@Query(value = "SELECT MONTH(p.thoigian), SUM(p.tongtien) FROM phieuxuat p WHERE p.status != -3 AND p.status != -1 AND YEAR(p.thoigian) = YEAR(CURRENT_DATE) GROUP BY MONTH(p.thoigian)", nativeQuery = true)
	List<Object[]> getTongPhieuXuatTheoThang();

	@Query(value = "SELECT QUARTER(thoigian) AS quarter, SUM(tongtien) AS total " + "FROM phieuxuat "
			+ "WHERE status != -3 AND status != -1 AND YEAR(thoigian) = YEAR(CURDATE()) "
			+ "GROUP BY QUARTER(thoigian)", nativeQuery = true)
	List<Object[]> getPhieuXuatTheoQuy();
}
