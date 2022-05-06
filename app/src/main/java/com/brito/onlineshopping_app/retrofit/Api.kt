package com.brito.onlineshopping_app.retrofit
import com.brito.onlineshopping_app.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/{path}")
    fun getPosts(@Path("path") path: String, @Header("Auth_Token") auth: String): Call<ArrayList<Products>>

    @GET("/products/{id}")
    fun getProduct(@Path("id") path: String, @Header("Auth_Token") auth: String): Call<Products>

    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") path: String, @Header("Auth_Token") auth: String): Call<ArrayList<Products>>

    @GET("/users")
    fun getAllUsers(): Call<ArrayList<UserResponse>>

    @GET("/users/{userId}")
    fun getSingleUser(@Path("userId") path: Int, @Header("Auth_Token") auth: String): Call<UserResponse>

    @GET("/carts/user/{userId}")
    fun getUserCart(@Path("userId") path: Int, @Header("Auth_Token") auth: String): Call<ArrayList<Cart>>

    @POST("/users")
    fun postNewUser(@Body userRequest: UserRequest, @Header("Auth_Token") auth: String): Call<UserResponse>

    @POST("/auth/login")
    fun login(@Body userLogin: UserLogin, @Header("Auth_Token") auth: String): Call<Token>

    @PATCH("/carts/{cartId}")
    fun patchCart(@Path("cartId") path: Int, @Body cart: Cart, @Header("Auth_Token") auth: String): Call<Cart>

    @DELETE("/carts/{id}")
    fun deleteCart(@Path("id") path: Int, @Header("Auth_Token") auth: String): Call<Cart>
}

