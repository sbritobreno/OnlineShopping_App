package com.brito.onlineshopping_app

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}