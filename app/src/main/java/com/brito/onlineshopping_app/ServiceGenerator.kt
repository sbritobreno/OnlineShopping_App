package com.brito.onlineshopping_app

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object ServiceGenerator {
    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}

// Interfaces
interface ApiProductsService {
    @GET("/{path}")
    fun getPosts(@Path("path") path: String): Call<ArrayList<Products>>
}

interface  ApiSingleProductService {
    @GET("/products/{id}")
    fun getProduct(@Path("id") path: String): Call<Products>
}

interface  ApiProductsByCategoryService {
    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") path: String): Call<ArrayList<Products>>
}

interface  ApiAllUsersService {
    @GET("/users")
    fun getAllUsers(): Call<ArrayList<User>>
}