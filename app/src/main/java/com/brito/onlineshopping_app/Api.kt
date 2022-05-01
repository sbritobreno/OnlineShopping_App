package com.brito.onlineshopping_app

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/{path}")
    fun getPosts(@Path("path") path: String): Call<ArrayList<Products>>

    @GET("/products/{id}")
    fun getProduct(@Path("id") path: String): Call<Products>

    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") path: String): Call<ArrayList<Products>>

    @GET("/users")
    fun getAllUsers(): Call<ArrayList<UserResponse>>

    @GET("/users/{userId}")
    fun getSingleUser(@Path("userId") path: Int): Call<UserResponse>

    @GET("/carts/user/{userId}")
    fun getUserCart(@Path("userId") path: Int): Call<ArrayList<Cart>>

    @POST("/users")
    fun postNewUser(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("/auth/login")
    fun login(@Body userLogin: UserLogin): Call<Token>

    @PATCH("/carts/{cartId}")
    fun patchCart(@Path("cartId") path: Int, @Body cart: Cart): Call<Cart>

    @DELETE("/carts/{id}")
    fun deleteCart(@Path("id") path: Int): Call<Cart>
}

