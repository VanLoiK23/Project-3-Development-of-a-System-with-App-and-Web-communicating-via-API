package com.example.quanlybandienthoai.model.remote.api

import com.example.quanlybandienthoai.model.remote.entity.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface ApiService {
    @Multipart//Báo Retrofit biết rằng API này sẽ gửi dữ liệu dạng multipart/form-data.
    @POST("image/upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Query("upload_preset") uploadPreset: String = "YOUR_UPLOAD_PRESET",
        @Query("public_id") publicId: String,
    ): Response<UploadResponse>

    @FormUrlEncoded
    @POST("image/destroy")
    suspend fun deleteImage(
        @Field("public_id") publicId: String,
        @Field("api_key") apiKey: String,
        @Field("timestamp") timestamp: Long,
        @Field("signature") signature: String
    ): Response<UploadResponse>

}
