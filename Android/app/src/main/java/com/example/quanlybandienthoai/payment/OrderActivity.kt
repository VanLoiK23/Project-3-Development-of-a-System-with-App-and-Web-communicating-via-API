package com.example.quanlybandienthoai.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.quanlybandienthoai.model.remote.entity.chitietphieuxuat
import com.example.quanlybandienthoai.model.remote.entity.momo
import com.example.quanlybandienthoai.model.remote.entity.phieuxuat
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.viewmodel.PlaceOrderViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib


class OrderActivity : AppCompatActivity() {

    private lateinit var placeOrderViewModel: PlaceOrderViewModel

    private var amount = 10000.0
    private var idship = -1
    private var fee_trans = -1
    private var discountCod = -1
    private var idKh = -1
    private var ctpx: List<chitietphieuxuat> = emptyList()


    private val fee = "0"
    var environment: Int = 0 //developer default
    private val merchantName = "Demo SDK"
    private val merchantCode = "MOMOBKUN20180529"
    private val merchantNameLabel = "Quản lý điện thoại"
    private val description = "Thanh toán dịch vụ kinh doanh điện thoại"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)

        placeOrderViewModel = ViewModelProvider(this)[PlaceOrderViewModel::class.java]

        val am = intent.getDoubleExtra("amount", 0.0)
        val idShipping = intent.getIntExtra("id_shipping", -1)
        val feeTransport = intent.getIntExtra("fee_transport", 0)
        val discountCode = intent.getIntExtra("discount_code", -1) // Nếu là Int
        val makh = intent.getIntExtra("makh", -1)

        val ctpxList = intent.getSerializableExtra("ctpxList") as? ArrayList<*>

//        amount = am
        idship = idShipping
        fee_trans = feeTransport
        discountCod = discountCode
        idKh = makh
        ctpx = ctpxList
            ?.filterIsInstance<chitietphieuxuat>()
            ?: emptyList()

        Log.d("Test", "Amount: $am")
        Log.d("Test", "ID Shipping: $idShipping")
        Log.d("Test", "Fee Transport: $feeTransport")
        Log.d("Test", "Discount Code: $discountCode")
        Log.d("Test", "MaKH: $makh")
        Log.d("Test", "CTPX List: $ctpx")

        requestPayment()
    }

    //Get token through MoMo app
    private fun requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
        /* if (edAmount.getText().toString() != null && edAmount.getText().toString().trim()
                 .length() !== 0
         )*/
//            amount = edAmount.getText().toString().trim()

        val eventValue: MutableMap<String, Any> = HashMap()
        //client Required
        eventValue["merchantname"] =
            merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue["merchantcode"] =
            merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue["amount"] = amount //Kiểu integer
        eventValue["orderId"] = System.currentTimeMillis() / 1000
        eventValue["orderLabel"] = "Mã đơn hàng" //gán nhãn


        //client Optional - bill info
        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
        eventValue["fee"] = fee
        //total_fee //Kiểu integer
        eventValue["description"] = description //mô tả đơn hàng - short description

        //client extra data
        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
        eventValue["partnerCode"] = merchantCode
        //Example extra data
        val objExtraData = JSONObject()
        try {
            objExtraData.put("partner_code", merchantCode)
            objExtraData.put("order_Id", System.currentTimeMillis() / 1000)
            objExtraData.put("amount", amount)
            objExtraData.put("order_info", "Thanh toán qua ATM")
            objExtraData.put("order_type", "2D")
            objExtraData.put("trans_id", 2147483647)
            objExtraData.put("pay_Type", "napas")
            objExtraData.put("codeCart", (0..9999).random())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        eventValue["extraData"] = objExtraData.toString()

        eventValue["extra"] = ""
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
    }

    //Get token callback from MoMo app an submit to server side
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("token", data.getStringExtra("message").toString())
                    val token = data.getStringExtra("data") //Token response
                    Log.d("token", data.getStringExtra("data").toString())
                    val phoneNumber = data.getStringExtra("phonenumber")
                    val env = data.getStringExtra("env") ?: "app"

                    val partner_code = merchantCode
                    val order_Id = System.currentTimeMillis() / 1000
                    val amount = amount
                    val order_info = "Thanh toán qua ATM"
                    val order_type = "momo_wallet"
                    val trans_id = 2147483647
                    val pay_Type = "napas"
                    val codeCart = (0..9999).random()

                    Log.d(
                        "token",
                        partner_code + order_Id + amount + order_info + order_type + trans_id + pay_Type + codeCart
                    )


                    if (token != null && token != "") {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order

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
                            payment = token,
                            cartShipping = idship,
                            discountCode = if (discountCod == -1) {
                                null
                            } else {
                                discountCod
                            },
                            feeTransport = fee_trans,
                            feeback = ""
                        )

                        val ctpxList = ctpx.map {
                            it.copy(codeCart = codeCart.toString())
                        }

                        Log.d("BBB", momo.toString())
                        Log.d("BBB", donhang.toString())
                        Log.d("BBB", ctpxList.toString())

                        placeOrderViewModel.placeOrder(momo, donhang, ctpxList) { check ->
                            if (check) {
                                placeOrderViewModel.markPaymentSuccess()

                                lifecycleScope.launch {
                                    try {
                                        ctpxList.forEach { ctpx->
                                            ProductRepository.updateSoLuongChiTuMapPhienBan(mapOf(ctpx.phienBanSanPhamXuatId to ctpx.soLuong))
                                        }
                                    } catch (e: Exception) {
                                        Log.e("onActivityResult", "Lỗi cập nhật số lượng: ${e.message}")
                                    }
                                }

                                val resultIntent = Intent()
                                resultIntent.putExtra("payment_success", true)
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()

                            }
                        }


                    } else {
                        Log.d("token", "Failed")
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    val message =
                        if (data.getStringExtra("message") != null) data.getStringExtra("message") else "Thất bại"
                    Log.d("token", "Failed")
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("token", "Failed")
                } else {
                    //TOKEN FAIL
                    Log.d("token", "Failed")
                }
            } else {
                Log.d("token", "Failed")
            }
        } else {
            Log.d("token", "Failed")
        }
    }
}

