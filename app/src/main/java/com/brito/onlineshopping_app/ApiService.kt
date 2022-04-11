package com.brito.onlineshopping_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiProductsService {
    @GET("/{path}")
    fun getPosts(@Path("path") path: String): Call<ArrayList<Models>>
}