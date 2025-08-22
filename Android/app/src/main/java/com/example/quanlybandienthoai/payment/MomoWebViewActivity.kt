package com.example.quanlybandienthoai.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.quanlybandienthoai.model.remote.entity.EmailRequest
import com.example.quanlybandienthoai.model.remote.entity.OrderItem
import com.example.quanlybandienthoai.model.remote.entity.chitietphieuxuat
import com.example.quanlybandienthoai.model.remote.entity.momo
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.viewmodel.PlaceOrderViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MomoWebViewActivity : ComponentActivity() {
    private lateinit var placeOrderViewModel: PlaceOrderViewModel


    private var amount = 10000.0
    private var idship = -1
    private var fee_trans = -1
    private var discountCod = -1
    private var idKh = -1
    private var ctpx: List<chitietphieuxuat> = emptyList()

    private val merchantCode = "MOMOBKUN20180529"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val payUrl = intent.getStringExtra("payUrl")

        placeOrderViewModel = ViewModelProvider(this)[PlaceOrderViewModel::class.java]

        val am = intent.getDoubleExtra("amount", 0.0)
        val idShipping = intent.getIntExtra("id_shipping", -1)
        val feeTransport = intent.getIntExtra("fee_transport", 0)
        val discountCode = intent.getIntExtra("discount_code", -1) // Nếu là Int
        val makh = intent.getIntExtra("makh", -1)

        val ctpxList = intent.getSerializableExtra("ctpxList") as? ArrayList<*>

        amount = am
        idship = idShipping
        fee_trans = feeTransport
        discountCod = discountCode
        idKh = makh
        ctpx = ctpxList
            ?.filterIsInstance<chitietphieuxuat>()
            ?: emptyList()


//        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
//        val formattedDate = sdf.format(Date())
//        val dateObject = sdf.parse(formattedDate)

        //thanh toan truc tiep cash
        if (payUrl == null) {


            val donhang = phieuxuat(
                -1, amount.toBigDecimal(),
                makh = idKh,
                codeCart = "",
                status = 0,
                payment = "",
                cartShipping = idship,
                discountCode = if (discountCod == -1) {
                    null
                } else {
                    discountCod
                },
                feeTransport = fee_trans,
                feeback = "",
                date = null
            )

            val ctpxList = ctpx

            Log.d("BBB", donhang.toString())
            Log.d("BBB", ctpxList.toString())

            placeOrderViewModel.placeOrder(
                momo("", 0, "", "", "", 0, "", 0), donhang, ctpxList
            ) { check ->
                if (check) {
                    placeOrderViewModel.markPaymentSuccess()

                    lifecycleScope.launch {
                        try {
                            ctpxList.forEach { ctpx ->
                                ProductRepository.updateSoLuongChiTuMapPhienBan(mapOf(ctpx.phienBanSanPhamXuatId to ctpx.soLuong))
                            }

                            Log.d("isRun", ctpxList.toString())


                            //ko kịp chạy phải đưa vào nếu ko muốn cancle
                            val resultIntent = Intent()
                            resultIntent.putExtra("payment_success", true)
                            setResult(RESULT_OK, resultIntent)
                            finish()
                        } catch (e: Exception) {
                            Log.e("onActivityResult", "Lỗi cập nhật số lượng: ${e.message}")
                        }
                    }

                }
            }
        } else {

            val webView = WebView(this)
            setContentView(webView)

            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    if (url.contains("/thankyou")) {
                        // 🟢 Thanh toán xong, trả kết quả về Compose
//
//                    val data = Intent().apply {
//                        putExtra("payment_success", true)
//                        putExtra("url", url)
//                    }

                        val partner_code = merchantCode
                        val order_Id = System.currentTimeMillis() / 1000
                        val amount = amount
                        val order_info = "Thanh toán qua ATM"
                        val order_type = "momo_wallet"
                        val trans_id = 2147483647
                        val pay_Type = "napas"
                        val codeCart = (0..9999).random()

                        val momo = momo(
                            partner_code,
                            order_Id,
                            amount.toString(),
                            order_info,
                            order_type,
                            trans_id.toLong(),
                            pay_Type,
                            codeCart
                        )

                        val donhang = phieuxuat(
                            -1, amount.toBigDecimal(),
                            makh = idKh,
                            codeCart = codeCart.toString(),
                            status = 0,
                            payment = "momo",
                            cartShipping = idship,
                            discountCode = if (discountCod == -1) {
                                null
                            } else {
                                discountCod
                            },
                            feeTransport = fee_trans,
                            feeback = "",
                            date = null
                        )

                        val ctpxList = ctpx.map {
                            it.copy(codeCart = codeCart.toString())
                        }
                        val orderItems = ctpx.mapNotNull { ct ->
                            ct.tenSP?.let {
                                OrderItem(
                                    it,
                                    quantity = ct.soLuong,
                                    price = ct.donGia
                                )
                            }
                        }


                        val emailRequest = EmailRequest("", orderItems, amount.toInt())
                        placeOrderViewModel.sendEmail(idKh, emailRequest)

                        Log.d("BBB", momo.toString())
                        Log.d("BBB", donhang.toString())

                        Log.d("BBB", ctpxList.toString())

                        placeOrderViewModel.placeOrder(momo, donhang, ctpxList) { check ->
                            if (check) {
                                placeOrderViewModel.markPaymentSuccess()

                                lifecycleScope.launch {
                                    try {
                                        ctpxList.forEach { ctpx ->
                                            ProductRepository.updateSoLuongChiTuMapPhienBan(
                                                mapOf(
                                                    ctpx.phienBanSanPhamXuatId to ctpx.soLuong
                                                )
                                            )
                                        }

                                        Log.d("isRun", ctpxList.toString())

                                        val resultIntent = Intent()
                                        resultIntent.putExtra("payment_success", true)
                                        setResult(RESULT_OK, resultIntent)
                                        finish()

                                    } catch (e: Exception) {
                                        Log.e(
                                            "onActivityResult",
                                            "Lỗi cập nhật số lượng: ${e.message}"
                                        )
                                    }
                                }

                            }
                        }
//                    setResult(Activity.RESULT_OK, data)
//                    finish()
                        return true
                    }
                    return false
                }
            }

            payUrl?.let { webView.loadUrl(it) }
        }
    }
}
