package com.example.quanlybandienthoai.model.remote.repo
import com.example.quanlybandienthoai.model.remote.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UpAnhRepo {
    private const val CLOUD_NAME = "deynh1vvv"
    private const val BASE_URL = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/"

    private val client = OkHttpClient.Builder().build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
