package com.example.quanlybandienthoai.model.repository

import android.util.Log
import com.example.quanlybandienthoai.model.remote.entity.CartItem
import com.example.quanlybandienthoai.model.remote.entity.Phienbansanpham
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.google.firebase.firestore.Query
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.firestore.ktx.firestoreSettings
//import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

object ProductRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val productCollectionRef: CollectionReference = UserRepository.db.collection("sanpham")

    //  val db by lazy { Firebase.firestore }

    //  private val productCollectionRef by lazy { db.collection("product") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    suspend fun getAllProducts(): List<Sanpham> =
        productCollectionRef.whereNotEqualTo("trash", "disable").get().await().toObjects(
            Sanpham::class.java
        )

    suspend fun getAllPhienBanSanPham(masp: Int): List<Phienbansanpham> {
        return try {
            // Lấy sản phẩm để kiểm tra promo
            val productSnapshot = productCollectionRef.document(masp.toString()).get().await()
            val product = productSnapshot.toObject(Sanpham::class.java) ?: return emptyList()

            // Lấy tất cả phiên bản sản phẩm
            val snapshot = productCollectionRef.document(masp.toString())
                .collection("phienbansanpham").get().await()
            val variants = snapshot.toObjects(Phienbansanpham::class.java)

            // Tính toán lại giá xuất (nếu có promo hợp lệ)
            variants.map { variant ->

                val discountValue = product.promo.find { it["name"] == "giamgia" }
                    ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

                val shockingValue = product.promo.find { it["name"] == "giareonline" }
                    ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

                variant.copy(
                    giaxuat = variant.giaxuat - (variant.giaxuat * (discountValue / 100)),
                    price_sale = variant.giaxuat - (variant.giaxuat * (shockingValue / 100))
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

//    suspend fun updateSoLuongSanPhamVaPhienBan(
//        masp: Int,
//        soLuongBanTheoPhienBan: Map<String, Int>
//    ) {
//        try {
//            val productRef = productCollectionRef.document(masp.toString())
//            val productSnapshot = productRef.get().await()
//            val product = productSnapshot.toObject(Sanpham::class.java) ?: return
//
//            val variantRef = productRef.collection("phienbansanpham")
//            val snapshot = variantRef.get().await()
//
//            var tongSoLuongBanMoi = 0
//
//            snapshot.documents.forEach { doc ->
//                val variant = doc.toObject(Phienbansanpham::class.java)
//                val variantId = doc.id
//
//                if (variant != null && soLuongBanTheoPhienBan.containsKey(variantId)) {
//                    val soLuongBan = soLuongBanTheoPhienBan[variantId] ?: 0
//
//                    val soLuongTonCu = variant.soluongton ?: 0
//
//                    val soLuongTonMoi = soLuongTonCu - soLuongBan
//
//                    // Cập nhật phiên bản
//                    variantRef.document(variantId).update(
//                        mapOf(
//                            "soluongton" to soLuongTonMoi
//                        )
//                    ).await()
//
//                    // Cộng vào tổng bán mới
//                    tongSoLuongBanMoi += soLuongBan
//                }
//            }
//
//            // Cập nhật tổng số lượng bán vào sản phẩm
//            val soLuongBanCuSanPham = product.soluongban ?: 0
//            productRef.update("soluongban", soLuongBanCuSanPham + tongSoLuongBanMoi).await()
//
//        } catch (e: Exception) {
//            Log.e("UpdateSoLuong", "Lỗi khi cập nhật số lượng: ${e.message}")
//        }
//    }

    suspend fun updateSoLuongChiTuMapPhienBan(
        soLuongBanTheoPhienBan: Map<Int, Int>
    ) {
        try {
            val productDocsToUpdate =
                mutableMapOf<String, Int>() // map masp -> tổng số lượng bán thêm

            soLuongBanTheoPhienBan.forEach { (maphienban, soLuongBan) ->
                // Duyệt toàn bộ sản phẩm để tìm xem phiên bản này nằm ở sản phẩm nào
                val sanphamSnapshot = productCollectionRef.get().await()
                Log.d("UpdateSoLuong", "Đang cập nhật: masp = Ok, maphienban = $maphienban, trừ $soLuongBan")

                for (productDoc in sanphamSnapshot.documents) {
                    val masp = productDoc.id.toString()
                    val variantRef = productCollectionRef.document(masp)
                        .collection("phienbansanpham").document(maphienban.toString())

                    val variantSnapshot = variantRef.get().await()

                    if (variantSnapshot.exists()) {
                        val variant =
                            variantSnapshot.toObject(Phienbansanpham::class.java) ?: continue

                        val soluongtonCu = variant.soluongton ?: 0

                        // Cập nhật phiên bản
                        variantRef.update(
                            "soluongton", FieldValue.increment(-soLuongBan.toLong())
                        ).await()

                        // Đánh dấu sản phẩm cha cần cộng thêm số lượng
                        productDocsToUpdate[masp] =
                            productDocsToUpdate.getOrDefault(masp, 0) + soLuongBan
                        Log.d("UpdateSoLuong", "Đang cập nhật: masp = $masp, maphienban = $maphienban, trừ $soLuongBan")


                        break // Đã tìm thấy phiên bản -> không cần duyệt tiếp
                    }
                }
            }

            // Sau khi cập nhật hết phiên bản, cập nhật tổng số lượng bán của sản phẩm cha
            for ((masp, tongBanMoi) in productDocsToUpdate) {
                val productRef = productCollectionRef.document(masp)
                val productSnapshot = productRef.get().await()
                val product = productSnapshot.toObject(Sanpham::class.java) ?: continue

                val banCu = product.soluongban ?: 0
                productRef.update("soluongban", banCu + tongBanMoi).await()
            }

        }catch (e: CancellationException) {
            Log.e("UpdateSoLuong", "Coroutine bị huỷ: ${e.message}", e)
            throw e // ⚠️ Phải rethrow nếu là CancellationException, nếu không coroutine sẽ không bị huỷ đúng cách
        } catch (e: Exception) {
            Log.e("UpdateSoLuong", "Lỗi khác khi cập nhật: ${e.message}", e)
        }

    }

    suspend fun updateSoLuongImportTuMapPhienBan(
        soLuongNhapTheoPhienBan: Map<Int, Int>
    ) {
        try {
            val productDocsToUpdate =
                mutableMapOf<String, Int>() // map masp -> tổng số lượng bán thêm

            soLuongNhapTheoPhienBan.forEach { (maphienban, soLuongNhap) ->
                // Duyệt toàn bộ sản phẩm để tìm xem phiên bản này nằm ở sản phẩm nào
                val sanphamSnapshot = productCollectionRef.get().await()
                Log.d("UpdateSoLuong", "Đang cập nhật: masp = Ok, maphienban = $maphienban, cong $soLuongNhap")

                for (productDoc in sanphamSnapshot.documents) {
                    val masp = productDoc.id.toString()
                    val variantRef = productCollectionRef.document(masp)
                        .collection("phienbansanpham").document(maphienban.toString())

                    val variantSnapshot = variantRef.get().await()

                    if (variantSnapshot.exists()) {
                        val variant =
                            variantSnapshot.toObject(Phienbansanpham::class.java) ?: continue

                        // Cập nhật phiên bản
                        variantRef.update(
                            "soluongton", FieldValue.increment(soLuongNhap.toLong())
                        ).await()

                        // Đánh dấu sản phẩm cha cần cộng thêm số lượng
                        productDocsToUpdate[masp] =
                            productDocsToUpdate.getOrDefault(masp, 0) + soLuongNhap
                        Log.d("UpdateSoLuong", "Đang cập nhật: masp = $masp, maphienban = $maphienban, cong $soLuongNhap")

                        break // Đã tìm thấy phiên bản -> không cần duyệt tiếp
                    }
                }
            }

            Log.d("TestImport",productDocsToUpdate.toString())

            // Sau khi cập nhật hết phiên bản, cập nhật tổng số lượng bán của sản phẩm cha
            for ((masp, tongNhapMoi) in productDocsToUpdate) {
                val productRef = productCollectionRef.document(masp)
//                val productSnapshot = productRef.get().await()
//                val product = productSnapshot.toObject(Sanpham::class.java) ?: continue
//                val nhapCu = product.soluongnhap ?: 0
//                productRef.update("soluongnhap", nhapCu + tongNhapMoi).await()
                productRef.update(
                    "soluongnhap", FieldValue.increment(tongNhapMoi.toLong())
                ).await()
            }

        }catch (e: CancellationException) {
            Log.e("UpdateSoLuong", "Coroutine bị huỷ: ${e.message}", e)
            throw e // ⚠️ Phải rethrow nếu là CancellationException, nếu không coroutine sẽ không bị huỷ đúng cách
        } catch (e: Exception) {
            Log.e("UpdateSoLuong", "Lỗi khác khi cập nhật: ${e.message}", e)
        }

    }


    suspend fun insertSanphamWithPhienbans(
        sanpham: Sanpham,
        phienbansanphamList: List<Phienbansanpham>
    ) {
        withContext(Dispatchers.IO) {
            insertProduct(sanpham)

            val maspCurrent = sanpham.masp

            phienbansanphamList.forEach { pb ->
                insertPhienBanSanPham(maspCurrent, pb)
            }
        }
    }


    suspend fun getProductsByBrand(brand: Int): List<Sanpham> {
        return productCollectionRef.whereNotEqualTo("trash", "disable")
            .whereEqualTo("thuonghieu", brand).get().await().toObjects(
            Sanpham::class.java
        )
    }

    suspend fun getProductByName(productName: String): List<Sanpham> {
        return try {
            val snapshot = productCollectionRef.whereNotEqualTo("trash", "disable")
                .whereGreaterThanOrEqualTo("tensp", productName)
                .whereLessThanOrEqualTo(
                    "tensp", productName + "\uf8ff"
                ) // Trick tìm kiếm unicode tìm các sp liên quan đến name
                .get().await()
            snapshot.toObjects(Sanpham::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProductById(productId: String): Sanpham? =
        productCollectionRef.document(productId).get().await().toObject(Sanpham::class.java)


    suspend fun updateRatingStats(masp: Int, tongSao: Double) {
        productCollectionRef.document(masp.toString())
            .update(
                mapOf(
                    "soluongdanhgia" to FieldValue.increment(1),
                    "tongsao" to FieldValue.increment(tongSao)
                )
            ).await()
    }

    suspend fun updateNumberProduct(masp: Int, soluongnhap: Double) {
        productCollectionRef.document(masp.toString())
            .update(
                mapOf(
                    "soluongnhap" to FieldValue.increment(soluongnhap)
                )
            ).await()
    }

    suspend fun updateProductPurchase(masp: Int, soluongban: Double) {
        productCollectionRef.document(masp.toString()).update(
            mapOf(
                "soluongban" to FieldValue.increment(soluongban)
            )
        ).await()
    }

    suspend fun updatePhienBanSanPhamPurchase(masp: Int, maPB:Int,soluongban: Double) {
        try {
            productCollectionRef.document(masp.toString())
                .collection("phienbansanpham")
                .document(maPB.toString()) // ID của Document
                .update(
                    mapOf(
                        "soluongton" to FieldValue.increment(soluongban)
                    )
                ).await()
            Log.d("Firestore", "Phienbansanpham update successfully!")
        } catch (e: Exception) {
            Log.w("Firestore", "Error update document", e)
        }
    }



    suspend fun updatePhienBanSanPham(masp: Int, phienBanSanPham: Phienbansanpham) {
        try {
            productCollectionRef.document(masp.toString()) // Lấy Document theo masp
                .collection("phienbansanpham") // Collection con
                .document(phienBanSanPham.maphienbansp.toString()) // ID của Document
                .set(phienBanSanPham).await()
            Log.d("Firestore", "Phienbansanpham update successfully!")
        } catch (e: Exception) {
            Log.w("Firestore", "Error update document", e)
        }
    }

    suspend fun getMaSPByMaPhienBanSP(maphienbansp: Int): Int? {

        // Lấy tất cả sản phẩm (sanpham) để duyệt tìm
        val sanphamDocs = productCollectionRef.get().await()

        for (sanphamDoc in sanphamDocs.documents) {
            val masp = sanphamDoc.id.toInt() // ID của sản phẩm chính là masp

            // Truy vấn collection con `phienbansanpham` của từng sản phẩm
            val phienbanDoc = sanphamDoc.reference
                .collection("phienbansanpham")
                .document(maphienbansp.toString())
                .get()
                .await()

            // Nếu tồn tại thì trả về `masp`
            if (phienbanDoc.exists()) {
                return masp
            }
        }

        return null // Không tìm thấy
    }

    suspend fun getMaPBSPByRomRamMauSac(masp: Int, ram: String, rom: String, color: String): Int? {
        val sanphamDoc = productCollectionRef.document(masp.toString()).get().await()
        if (!sanphamDoc.exists()) {
            return null
        }

        val phienbanDocs = sanphamDoc.reference.collection("phienbansanpham").get().await()

        for (phienbanDoc in phienbanDocs) {
            val phienban = phienbanDoc.toObject(Phienbansanpham::class.java)

            // Kiểm tra nếu các thuộc tính ram, rom, và màu sắc phù hợp
            if (phienban?.ram == ram && phienban?.rom == rom && phienban?.mausac == color) {
                return phienbanDoc.id.toInt() // Trả về ID của phiên bản sản phẩm
            }
        }

        // Nếu không tìm thấy phiên bản nào thỏa mãn, trả về null
        return null
    }

    suspend fun getNextProductId(): Int {
        val snapshot =
            productCollectionRef.orderBy("masp", Query.Direction.DESCENDING).limit(1).get().await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("masp") ?: 0
            (lastId + 1).toInt()
        } else {
            1 // Nếu chưa có sản phẩm nào, bắt đầu từ 1
        }
    }

    suspend fun getNextPhienBanId(masp: Int): Int {
        val snapshot = productCollectionRef.document(masp.toString()).collection("phienbansanpham")
            .orderBy("maphienbansp", Query.Direction.DESCENDING).limit(1).get().await()

        return if (!snapshot.isEmpty) {
            val lastId = snapshot.documents[0].getLong("maphienbansp") ?: 0
            (lastId + 1).toInt()
        } else {
            1
        }
    }

    suspend fun getSLByMaPhienBanSP(cart: CartItem): Int {
        val snapshot = productCollectionRef
            .document(cart.masp.toString())
            .collection("phienbansanpham")
            .document(cart.maphienbansp.toString())
            .get()
            .await()

        return if (snapshot.exists()) {
            snapshot.getLong("soluongton")?.toInt() ?: 0
        } else {
            0
        }
    }


    suspend fun getPriceByRom(masp: Int, maphienbansp: Int, rom: String): Pair<Float, Float> {
        if (rom != "") {
            val snapshot = productCollectionRef.document(masp.toString())
                .collection("phienbansanpham")
                .whereEqualTo("maphienbansp", maphienbansp)
                .whereEqualTo("rom", rom)
                .limit(1)
                .get()
                .await()

            return if (!snapshot.isEmpty) {
                val priceSale = snapshot.documents[0].getDouble("price_sale")?.toFloat() ?: 0f
                val priceOriginal = snapshot.documents[0].getDouble("giaxuat")?.toFloat() ?: 0f
                Pair(priceSale, priceOriginal)
            } else {
                Pair(0f, 0f) // Trả về giá trị mặc định nếu không tìm thấy
            }
        } else {
            return Pair(0f, 0f)
        }
    }

    suspend fun getColorByMapb(masp: Int, mapb: Int): List<String> {

        if (mapb != -1) {
            val snapshot = productCollectionRef.document(masp.toString())
                .collection("phienbansanpham")
                .whereEqualTo("maphienbansp", mapb)
                .get()
                .await()

            return snapshot.documents.mapNotNull { it.getString("mausac") }
        }
        return emptyList();
    }

    suspend fun getMapbByRomAndColor(masp: Int, rom: String, ram: String, color: String): Int {
        val snapshot = productCollectionRef.document(masp.toString())
            .collection("phienbansanpham")
            .whereEqualTo("rom", rom)
            .whereEqualTo("ram", ram)
            .whereEqualTo("mausac", color)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            return snapshot.documents.first().getLong("maphienbansp")?.toInt() ?: -1
        }

        return -1
    }


    suspend fun insertProduct(product: Sanpham) {
//        val documentId = getNextProductId()
        product.masp = product.masp
        productCollectionRef.document(product.masp.toString()).set(product)
    }

    suspend fun insertPhienBanSanPham(masp: Int, phienBanSanPham: Phienbansanpham) {
        try {
            productCollectionRef.document(masp.toString()) // Lấy Document theo masp
                .collection("phienbansanpham") // Collection con
                .document(
//                    getNextPhienBanId(masp = masp).toString()
                    phienBanSanPham.maphienbansp.toString()
                ) // ID của Document
                .set(phienBanSanPham).await()
            Log.d("Firestore", "Phienbansanpham added successfully!")
        } catch (e: Exception) {
            Log.w("Firestore", "Error adding document", e)
        }
    }


//    suspend fun insertReview(productId: String, review: Review) {
//        val product = getProductById(productId)
//        product?.let {
//            product.reviews.add(review)
//            productCollectionRef.document(productId).set(product).await()
//        }
//    }
//
//    suspend fun deleteReview(productId: String, userId: String) {
//        val product = getProductById(productId)
//        product?.let {
//            val reviews = mutableListOf<Review>()
//            for (review in product.reviews) if (review.uid != userId) reviews.add(review)
//
//            product.reviews = reviews
//            productCollectionRef.document(productId).set(product).await()
//        }
//    }
//
//    suspend fun updateReview(productId: String, updatedReview: Review) {
//        val product = getProductById(productId)
//        product?.let {
//            val reviews = mutableListOf<Review>()
//            for (review in product.reviews) if (review.uid != updatedReview.uid) reviews.add(review)
//            reviews.add(updatedReview)
//
//            product.reviews = reviews
//            productCollectionRef.document(productId).set(product).await()
//        }
//    }
//
//    suspend fun getReviewByUserAndProduct(userId: String, productId: String): Review? {
//        val product = getProductById(productId)
//        product?.let {
//            for (review in product.reviews) if (review.uid == userId) return review
//        }
//        return null
//    }


    suspend fun deleteProduct(productId: String) {
        productCollectionRef.document(productId).delete().await()
    }

    suspend fun getNewArrivals(): List<Sanpham> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -14) // Lấy thời điểm 2 tuần trước
        val twoWeeksAgo = calendar.time // Chuyển thành Date để so sánh

        val allProducts = productCollectionRef.whereNotEqualTo("trash", "disable")
            .orderBy("created", Query.Direction.DESCENDING) // Sắp xếp theo ngày
            // .limit(5)
            .get()
            .await()
            .toObjects(Sanpham::class.java)

        return allProducts.filter { product ->
            try {
                val createdDate =
                    dateFormat.parse(product.created) // Chuyển `created` từ String sang Date
                createdDate?.after(twoWeeksAgo) == true // Lọc sản phẩm tạo trong 2 tuần gần đây
            } catch (e: ParseException) {
                false // Bỏ qua nếu dữ liệu sai định dạng
            }
        }
    }


    suspend fun getTopRatedProducts(): List<Sanpham> {
        return productCollectionRef
            .whereNotEqualTo("trash", "disable")
            .orderBy("soluongdanhgia", Query.Direction.DESCENDING)
            .orderBy("tongsao", Query.Direction.DESCENDING)
//            .limit(5)
            .get().await().toObjects(Sanpham::class.java)
    }

    suspend fun getInstallmentProducts(): List<Sanpham> {
        return productCollectionRef.whereNotEqualTo("trash", "disable")
            .orderBy("soluongdanhgia", Query.Direction.DESCENDING)
            //            .limit(5)
            .get()
            .await().toObjects(Sanpham::class.java)
            .filter { product ->
                product.promo.any { it["name"] == "tragop" }
            }
    }

    suspend fun getProductsByPromo(promoName: String): List<Sanpham> {
        val products = productCollectionRef.whereNotEqualTo("trash", "disable").get().await()
            .toObjects(Sanpham::class.java)

        val filteredProducts =
            mutableListOf<Pair<Sanpham, Double>>() // Lưu sản phẩm & giá xuất thấp nhất

        for (product in products) {
            // Kiểm tra xem sản phẩm có promo không
            val promoValue = product.promo.find { it["name"] == promoName }
                ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

            if (promoValue > 0.0) {
                val variants = productCollectionRef.document(product.masp.toString())
                    .collection("phienbansanpham")
                    .get().await()
                    .toObjects(Phienbansanpham::class.java)

                // Tìm phiên bản có giá xuất thấp nhất
                var bestVariant: Phienbansanpham = Phienbansanpham()


                if (product.promo.find { it["name"] == "giamgia" } != null) {
                    val discountValue = product.promo.find { it["name"] == "giamgia" }
                        ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

                    bestVariant = variants.maxByOrNull {
                        it.giaxuat - (it.giaxuat * (discountValue / 100))
                    } ?: continue
                } else {

                    val shockingValue = product.promo.find { it["name"] == "giareonline" }
                        ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

                    bestVariant = variants.maxByOrNull {
                        it.giaxuat - (it.giaxuat * (shockingValue / 100))
                    } ?: continue

                }

                filteredProducts.add(product to bestVariant.giaxuat)
            }
        }

        // **Sắp xếp theo số lượng đánh giá nhiều nhất**
        return filteredProducts
            .sortedByDescending { it.first.soluongdanhgia }
            .map { it.first }
    }


    suspend fun getValuePromo(promoName: String): Double {
        val products = productCollectionRef.whereNotEqualTo("trash", "disable").get().await()
            .toObjects(Sanpham::class.java)

        var value: Double = 0.0

        for (product in products) {
            // Kiểm tra xem sản phẩm có promo không
            val promoValue = product.promo.find { it["name"] == promoName }
                ?.get("value")?.toString()?.toDoubleOrNull() ?: 0.0

            if (promoValue > 0.0) {
                value = promoValue
            }
        }
        return value
    }

    suspend fun getCheaperProducts(): List<Sanpham> {
        val products = productCollectionRef.whereNotEqualTo("trash", "disable").get().await()
            .toObjects(Sanpham::class.java)

        val filteredProducts = mutableListOf<Pair<Sanpham, Double>>()

        for (product in products) {
            // Kiểm tra xem sản phẩm có promo không
            val variants = productCollectionRef
                .document(product.masp.toString())
                .collection("phienbansanpham")
                .whereLessThanOrEqualTo("giaxuat", 2000000)
                .get().await()
                .toObjects(Phienbansanpham::class.java)

            // Tìm phiên bản có giá xuất thấp nhất
            val bestVariant = variants.minByOrNull { it.giaxuat } ?: continue

            filteredProducts.add(product to bestVariant.giaxuat)
        }

        // **Sắp xếp theo số lượng đánh giá nhiều nhất**
        return filteredProducts
//            .sortedByDescending { it.first.soluongdanhgia }
            .map { it.first }
    }

    /*
    //    suspend fun getCategoryProducts(
    //        cid: String, startPrice: Float, endPrice: Float
    //    ): List<Product>? {
    //        return productCollectionRef.whereEqualTo("cid", cid).whereGreaterThan("price", startPrice)
    //            .whereLessThan("price", endPrice).get().await().toObjects(
    //                Product::class.java
    //            )
    //    }
    //
    //    suspend fun getNormalFilterQuery(
    //        startPrice: Float, endPrice: Float
    //    ): List<Product>? {
    //        return productCollectionRef.whereGreaterThan("price", startPrice)
    //            .whereLessThan("price", endPrice).get().await().toObjects(
    //                Product::class.java
    //            )
    //    }

     */
}


