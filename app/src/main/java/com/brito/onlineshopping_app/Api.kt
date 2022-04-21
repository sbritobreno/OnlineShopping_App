package com.brito.onlineshopping_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/auth/login")
    fun login(@Body userLogin: UserLogin): Call<Token>
}

