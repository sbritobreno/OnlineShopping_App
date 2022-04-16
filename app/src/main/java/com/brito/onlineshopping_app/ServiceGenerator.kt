package com.brito.onlineshopping_app

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

interface ApiService {

    @GET("/{path}")
    fun getPosts(@Path("path") path: String): Call<ArrayList<Products>>

    @GET("/products/{id}")
    fun getProduct(@Path("id") path: String): Call<Products>

    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") path: String): Call<ArrayList<Products>>

    @GET("/users")
    fun getAllUsers(): Call<ArrayList<UserResponse>>

    @POST("/users")
    fun postNewUser(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("//auth/login")
    fun login(@Body userLogin: UserLogin): Call<Token>
}