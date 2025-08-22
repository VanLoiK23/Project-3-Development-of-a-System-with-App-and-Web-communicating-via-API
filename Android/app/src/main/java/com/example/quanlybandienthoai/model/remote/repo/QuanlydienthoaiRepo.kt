package com.example.quanlybandienthoai.model.remote.repo

import android.util.Log
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.Discount
import com.example.quanlybandienthoai.model.remote.entity.EmailRequest
import com.example.quanlybandienthoai.model.remote.entity.ImportProduct
import com.example.quanlybandienthoai.model.remote.entity.PnPxByMonth
import com.example.quanlybandienthoai.model.remote.entity.PnPxByQuarter
import com.example.quanlybandienthoai.model.remote.entity.PurchaseRequest
import com.example.quanlybandienthoai.model.remote.entity.RequestProductInteraction
import com.example.quanlybandienthoai.model.remote.entity.ResponseId
import com.example.quanlybandienthoai.model.remote.entity.RevenueOrderFollowWeekOrMonth
import com.example.quanlybandienthoai.model.remote.entity.RevenueProductTopSelling
import com.example.quanlybandienthoai.model.remote.entity.RevenuePurchaseFollowWeekOrMonth
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Supplier
import com.example.quanlybandienthoai.model.remote.entity.ThongKeEntity
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.model.remote.entity.chitietphieuxuat
import com.example.quanlybandienthoai.model.remote.entity.color
import com.example.quanlybandienthoai.model.remote.entity.hedieuhanh
import com.example.quanlybandienthoai.model.remote.entity.khuvuckho
import com.example.quanlybandienthoai.model.remote.entity.momo
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.remote.entity.ram
import com.example.quanlybandienthoai.model.remote.entity.rom
import com.example.quanlybandienthoai.model.remote.entity.xuatxu
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

object QuanlydienthoaiRepo {
    private const val BASE_URL =
        "http://192.168.1.74:8080/Spring-mvc/quan-tri/"

    //change Long to Date
    val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
            Date(json.asJsonPrimitive.asLong)
        })
        .create()


    private val emerceApi: QuanlydienthoaiApi by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
//            GsonConverterFactory.create()
            GsonConverterFactory.create(gson)
        )
            .build().create(QuanlydienthoaiApi::class.java)
    }

    //    suspend fun getUsers() = emerceApi.getUsers()
//    suspend fun getCategories() = emerceApi.getCategories()
    suspend fun getProducts() = emerceApi.getProducts()

    suspend fun getUrlMomo(total: Double, type: String): String {
        return try {
            if (type == "atm") {
                val response = emerceApi.getUrlMomoATM(total)
                response.payUrl

            } else {
                val response = emerceApi.getUrlMomoVISA(total)
                response.payUrl
            }
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API MoMo", e)
            ""
        }
    }


    suspend fun insertPhieuXuat(
        momo: momo,
        phieuxuat: phieuxuat,
        chitietphieuxuatList: List<chitietphieuxuat>
    ): String? {
        return try {
            val response =
                emerceApi.purchaseProduct(PurchaseRequest(momo, phieuxuat, chitietphieuxuatList))
            val responseBody = response.body()?.string()
            if (response.isSuccessful && responseBody == "Export Success") {
                return "Export Success"
            } else {
                return "Export Failed"
            }

        } catch (e: Exception) {
            "Lỗi: ${e.message}"
        }
    }

    suspend fun sendEmailResetPassword(
       user:User
    ): String {
        return try {
            val response =
                emerceApi.sendEmailResetPassword(user)
            val responseBody = response.body()?.string()
            if (response.isSuccessful && responseBody == "Email khôi phục đã được gửi!") {
                return "Email khôi phục đã được gửi!"
            } else {
                return "Not found user"
            }

        } catch (e: Exception) {
              "Lỗi: ${e.message}"
        }
    }


    suspend fun getAddress(id: Int): List<address> {
        return try {
            emerceApi.getAddress(id)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun addAddress(address: address): address? {
        return try {
            emerceApi.addAddress(address)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateAddress(id: Int, address: address): String? {
        return try {
            val response = emerceApi.updateAddress(id, address)
            if (response.isSuccessful) {
                response.body()?.string() // Trả về JSON dưới dạng String
            } else {
                "Lỗi: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            "Lỗi: ${e.message}"
        }
    }


    suspend fun deleteAddress(id: Int): Boolean {
        return try {
            emerceApi.deleteAddress(id).isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllDiscount(): List<Discount> {
        return try {
            emerceApi.getAllDiscount()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getAllUser(): List<User> {
        return try {
            emerceApi.getUsers()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getAllReviews(): List<Review> {
        return try {
            emerceApi.getReviews()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getAllSlides(): List<Slide> {
        return try {
            emerceApi.getSlides()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getAllThuongHieus(): List<Thuonghieu> {
        return try {
            emerceApi.getThuongHieu()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun addUser(user: User): User? {
        return try {
            emerceApi.addUser(user)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserInSession(users: User) {
        try {
            emerceApi.updateUserInSession(users)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getAllCarts(): List<Cart> {
        return try {
            emerceApi.getCarts()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun updateCartInSession(cart: Cart) {
        try {
            emerceApi.updateCartInSession(cart)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setCartCompleted(cartId: Int) {
        try {
            emerceApi.setStatusCompleted(cartId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getAllOrderByUserId(id: Int): List<phieuxuat> {
        return try {
            emerceApi.getAllOrderByUserId(id)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun cancel(id: Int, reason: String) {
        try {
            emerceApi.cancelOrder(id, reason)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    suspend fun sendEmailOrder(emailRequest: EmailRequest) {
         try {
            emerceApi.sendEmailOrder(emailRequest)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
        }
    }

    suspend fun saveFCMToken(userId: Int, token: String) {
        try {
            val response = emerceApi.saveFCMLogin(userId, token)
            if (response.isSuccessful) {
                // Xử lý khi gọi thành công
                Log.d("FCM", "Token saved successfully")
            } else {
                // Xử lý khi có lỗi
                Log.e("FCM", "Error saving token: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("FCM", "Error during token save", e)
        }
    }

    suspend fun reviewProduct(review: Review): ResponseId? {
        return try {
            emerceApi.reviewProduct(review)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            null
        }
    }

    suspend fun updateReviewProduct(review: Review) {
        try {
            emerceApi.updateReviewProduct(review)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }


    //admin

    //product
    suspend fun getSuppliers(): List<Supplier>? {
        return try {
            emerceApi.getSupplier()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }

    suspend fun importProduct(importProduct: ImportProduct) {
        try {
            emerceApi.importProduct(importProduct)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    suspend fun insertProduct(requestProductInteraction: RequestProductInteraction) {
        try {
            emerceApi.insertProduct(requestProductInteraction)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    suspend fun updateProduct(requestProductInteraction: RequestProductInteraction) {
        try {
            emerceApi.updateProduct(requestProductInteraction)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }


    suspend fun deleteProduct(id: Int) {
        try {
            emerceApi.deleteProduct(id)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    suspend fun getRams(): List<ram>? {
        return try {
            emerceApi.getRam()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }
    suspend fun getRoms(): List<rom>? {
        return try {
            emerceApi.getRom()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }
    suspend fun getColors(): List<color>? {
        return try {
            emerceApi.getColor()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }
    suspend fun getXX(): List<xuatxu>? {
        return try {
            emerceApi.getXX()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }
    suspend fun getKho(): List<khuvuckho>? {
        return try {
            emerceApi.getWareHouse()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }
    suspend fun getHDH(): List<hedieuhanh>? {
        return try {
            emerceApi.getHDH()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            emptyList()
        }
    }

    //category
    suspend fun upsertCategory(category: Thuonghieu): Thuonghieu? {
        return try {
            emerceApi.upsertCategory(category)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
            null
        }
    }

    //order
    suspend fun getAllOrders(): List<phieuxuat> {
        return try {
            emerceApi.getAllOrders()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun updateStatusOrder(id: Int, status: Int) {
        try {
            emerceApi.updateStatus(id, status)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    //customer
    suspend fun updateStatusCustomer(id: Int, status: String) {
        try {
            emerceApi.updateCustomerStatus(id, status)
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi ", e)
        }
    }

    //homepage
    suspend fun getInfoGeneral(): ThongKeEntity? {
        return try {
            emerceApi.getInfoGeneral()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            null
        }
    }

    suspend fun getOrderFollowWeek(): List<RevenueOrderFollowWeekOrMonth>? {
        return try {
            emerceApi.revenueOrderWeek()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getPurchaseFollowWeek(): List<RevenuePurchaseFollowWeekOrMonth>? {
        return try {
            emerceApi.revenuePurchaseWeek()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getOrderFollowMonth(): List<RevenueOrderFollowWeekOrMonth>? {
        return try {
            emerceApi.revenueOrderMonth()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getPurchaseFollowMonth(): List<RevenuePurchaseFollowWeekOrMonth>? {
        return try {
            emerceApi.revenuePurchaseMonth()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getProductTopSelling(): List<RevenueProductTopSelling>? {
        return try {
            emerceApi.revenueProductTopSelling()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            emptyList()
        }
    }

    suspend fun getPnPxFollowYear(): PnPxByMonth? {
        return try {
            emerceApi.revenuePnPxFollowYear()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            null
        }
    }

    suspend fun getPnPxFollowQuarter(): PnPxByQuarter? {
        return try {
            emerceApi.revenuePnPxFollowQuarter()
        } catch (e: Exception) {
            Log.e("Retrofit", "Lỗi khi gọi API getUsers", e)
            null
        }
    }

}