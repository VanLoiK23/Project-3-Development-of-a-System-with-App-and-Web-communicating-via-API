package com.example.quanlybandienthoai.model.remote.api

import com.example.quanlybandienthoai.model.remote.entity.Cart
import com.example.quanlybandienthoai.model.remote.entity.Discount
import com.example.quanlybandienthoai.model.remote.entity.EmailRequest
import com.example.quanlybandienthoai.model.remote.entity.ImportProduct
import com.example.quanlybandienthoai.model.remote.entity.MomoResponse
import com.example.quanlybandienthoai.model.remote.entity.PnPxByMonth
import com.example.quanlybandienthoai.model.remote.entity.PnPxByQuarter
import com.example.quanlybandienthoai.model.remote.entity.PurchaseRequest
import com.example.quanlybandienthoai.model.remote.entity.RequestProductInteraction
import com.example.quanlybandienthoai.model.remote.entity.ResponseId
import com.example.quanlybandienthoai.model.remote.entity.RevenueOrderFollowWeekOrMonth
import com.example.quanlybandienthoai.model.remote.entity.RevenueProductTopSelling
import com.example.quanlybandienthoai.model.remote.entity.RevenuePurchaseFollowWeekOrMonth
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Supplier
import com.example.quanlybandienthoai.model.remote.entity.ThongKeEntity
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.model.remote.entity.color
import com.example.quanlybandienthoai.model.remote.entity.hedieuhanh
import com.example.quanlybandienthoai.model.remote.entity.khuvuckho
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.remote.entity.ram
import com.example.quanlybandienthoai.model.remote.entity.rom
import com.example.quanlybandienthoai.model.remote.entity.xuatxu
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface QuanlydienthoaiApi {
    //user
    @GET("san-pham")
    suspend fun getProducts(): List<Sanpham>

    @GET("banner")
    suspend fun getSlides(): List<Slide>

    @GET("thuoc-tinh")
    suspend fun getThuongHieu(): List<Thuonghieu>

    @GET("khach-hang")
    suspend fun getUsers(): List<User>

    @GET("danh-gia")
    suspend fun getReviews(): List<Review>

    @PUT("khach-hang/login/{id}/{token}")
    suspend fun saveFCMLogin(@Path("id") id: Int, @Path("token") token: String): Response<Unit>

    @GET("don-hang/cart")
    suspend fun getCarts(): List<Cart>

    @POST("don-hang/atm/{total}")
    suspend fun getUrlMomoATM(@Path("total") total: Double): MomoResponse

    @POST("don-hang/visa/{total}")
    suspend fun getUrlMomoVISA(@Path("total") total: Double): MomoResponse

    @POST("don-hang")
    suspend fun purchaseProduct(
        @Body request: PurchaseRequest
    ): Response<ResponseBody>

    @GET("don-hang/address/{idkh}")
    suspend fun getAddress(@Path("idkh") id: Int): List<address>

    @POST("don-hang/address")
    suspend fun addAddress(@Body address: address): address

    @PUT("don-hang/address/{id}")
    suspend fun updateAddress(
        @Path("id") id: Int,
        @Body address: address
    ): Response<ResponseBody>

    @DELETE("don-hang/address/{id}")
    suspend fun deleteAddress(@Path("id") id: Int): Response<Unit>

    @GET("don-hang/discount")
    suspend fun getAllDiscount(): List<Discount>

    @POST("khach-hang")
    suspend fun addUser(@Body user: User): User

    @PUT("khach-hang")
    suspend fun updateUserInSession(@Body user: User)

    @POST("khach-hang/send-Password-Reset")
    suspend fun sendEmailResetPassword(@Body user: User): Response<ResponseBody>

    @PUT("don-hang/cart")
    suspend fun updateCartInSession(@Body cart: Cart)

    @PUT("don-hang/cart/completed/{cartId}")
    suspend fun setStatusCompleted(@Path("cartId") cartId: Int)

    @GET("don-hang/orderOfMy/{id}")
    suspend fun getAllOrderByUserId(@Path("id") id: Int): List<phieuxuat>

    @POST("don-hang/sendEmail")
    suspend fun sendEmailOrder(@Body emailRequest:EmailRequest)

    @PUT("don-hang/cancel/{id}/{reason}")
    suspend fun cancelOrder(@Path("id") id: Int, @Path("reason") reason: String)

    @POST("don-hang/review")
    suspend fun reviewProduct(@Body review: Review): ResponseId

    @PUT("don-hang/review")
    suspend fun updateReviewProduct(@Body review: Review)


    // admin

    //product
    @GET("nha-cung-cap")
    suspend fun getSupplier(): List<Supplier>

    @POST("san-pham/import")
    suspend fun importProduct(
        @Body importProduct: ImportProduct
    )

    @DELETE("san-pham/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    )

    @POST("san-pham/add_sp")
    suspend fun insertProduct(
        @Body requestProductInteraction: RequestProductInteraction
    )

    @PUT("san-pham/edit_sp")
    suspend fun updateProduct(
        @Body requestProductInteraction: RequestProductInteraction
    )

    @GET("thuoc-tinh/ram")
    suspend fun getRam(): List<ram>
    @GET("thuoc-tinh/rom")
    suspend fun getRom(): List<rom>
    @GET("thuoc-tinh/color")
    suspend fun getColor(): List<color>
    @GET("khu-vuc-kho")
    suspend fun getWareHouse(): List<khuvuckho>
    @GET("thuoc-tinh/xuatxu")
    suspend fun getXX(): List<xuatxu>
    @GET("thuoc-tinh/hedieuhanh")
    suspend fun getHDH(): List<hedieuhanh>


    //category
    @POST("thuoc-tinh")
    suspend fun upsertCategory(
        @Body category: Thuonghieu
    ): Thuonghieu

    //order
    @GET("don-hang/status/{id}/{status}")
    suspend fun updateStatus(
        @Path("id") id: Int,
        @Path("status") status: Int,
    )

    @GET("don-hang")
    suspend fun getAllOrders(): List<phieuxuat>


    //customer
    @POST("khach-hang/status/{id}/{status}")
    suspend fun updateCustomerStatus(
        @Path("id") id: Int,
        @Path("status") status: String,
    )

    //edit

    //homepage
    @GET("thong-ke")
    suspend fun getInfoGeneral(): ThongKeEntity

    @GET("thong-ke/don-hang/week")
    suspend fun revenueOrderWeek(): List<RevenueOrderFollowWeekOrMonth>

    @GET("thong-ke/don-hang/month")
    suspend fun revenueOrderMonth(): List<RevenueOrderFollowWeekOrMonth>

    @GET("thong-ke/doanh-thu/week")
    suspend fun revenuePurchaseWeek(): List<RevenuePurchaseFollowWeekOrMonth>

    @GET("thong-ke/doanh-thu/month")
    suspend fun revenuePurchaseMonth(): List<RevenuePurchaseFollowWeekOrMonth>

    @GET("thong-ke/san-pham/top")
    suspend fun revenueProductTopSelling(): List<RevenueProductTopSelling>

    @GET("thong-ke/nhap-xuat/year")
    suspend fun revenuePnPxFollowYear(): PnPxByMonth

    @GET("thong-ke/nhap-xuat/quarter")
    suspend fun revenuePnPxFollowQuarter(): PnPxByQuarter
}
