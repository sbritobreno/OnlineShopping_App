package com.brito.onlineshopping_app.utils

import com.brito.onlineshopping_app.*

var productListFromTheApi: ArrayList<Products> = arrayListOf()
var currentToken: Token = Token("j")
var currentUserId: Int? = 1

var allProductsInCart = arrayListOf<Product>()
var listFilledUp = true

var listOfUsers: ArrayList<UserResponse> = arrayListOf()

var purchasedHistory: ArrayList<PurchasedCart> = arrayListOf()
var singlePurchasedHistory: ArrayList<Products> = arrayListOf()
