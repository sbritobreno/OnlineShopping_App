package com.brito.onlineshopping_app.utils
import com.brito.onlineshopping_app.*

var productListFromTheApi: ArrayList<Products> = arrayListOf()
var currentToken: Token = Token("")
var currentUserId: Int? = 0
var listOfUsers: ArrayList<UserResponse> = arrayListOf()
var purchasedHistory: ArrayList<PurchasedCart> = arrayListOf()
var singlePurchasedHistory: ArrayList<Products> = arrayListOf()
var allProductsInCart = arrayListOf<Product>()
var listFilledUp = false
const val NOTIFICATION_CHANNEL_ID = "General"
