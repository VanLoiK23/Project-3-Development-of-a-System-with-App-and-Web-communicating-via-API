package com.example.appnotegiuaki.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.quanlybandienthoai.model.remote.repo.UpAnhRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.security.MessageDigest

class UploadRepository {
    suspend fun uploadImage(context: Context, uri: Uri, nameImage: String): String? {
        return withContext(Dispatchers.IO) {

            val file = FileUtils.uriToFile(context, uri, nameImage)

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())


            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            //Chuyển File thành RequestBody, khai báo dữ liệu là ảnh (image/*).

            val response = UpAnhRepo.instance.uploadImage(body, "upload_preset", nameImage)

            if (response.isSuccessful) {
                response.body()?.secure_url
            } else {
                null
            }
        }
    }

    suspend fun deleteImage(publicId: String): Boolean {
        return try {
            val apiKey = "557485463518174"
            val apiSecret = "jcjsPqUPcj5fBoYaqfe90u5QDqw"

            val timestamp = System.currentTimeMillis() / 1000
            //khoa bao mat
            val signature = generateSignature(publicId, timestamp, apiSecret)

            val response = UpAnhRepo.instance.deleteImage(
                publicId = publicId,
                apiKey = apiKey,
                timestamp = timestamp,
                signature = signature
            )

            if (response.isSuccessful) {
                Log.d("Cloudinary", "Xóa ảnh thành công: $publicId")
                true
            } else {
                Log.e("Cloudinary", "Lỗi xóa ảnh: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("Cloudinary", "Lỗi ngoại lệ: ${e.message}")
            false
        }
    }


    fun generateSignature(publicId: String, timestamp: Long, apiSecret: String): String {
        val input = "public_id=$publicId&timestamp=$timestamp$apiSecret"
        return MessageDigest.getInstance("SHA-1")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

}


