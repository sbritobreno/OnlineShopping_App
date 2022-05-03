package com.brito.onlineshopping_app

import java.util.*
import kotlin.collections.ArrayList

//Purchased Cart
data class PurchasedCart(
    val userId: Int?,
    val date: String?,
    var products: ArrayList<Products>,
    val finalPrice: Double? = null
)

// Products
data class Products (
    val id:Int? = null,
    val title: String? = null,
    var price: Double? = null,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    var quantity: Int? = 0
)

// Token
data class Token(
    var token: String?
)

// Users
data class UserLogin(
    val username: String?,
    val password: String?
)

data class UserRequest(
    val email: String?,
    val name: Name?,
    val password: String?,
    val username: String?
)

data class UserResponse(
    val address: Address?,
    val email: String?,
    val id: Int?,
    val name: Name?,
    val password: String?,
    val phone: String?,
    val username: String?
)

data class Name(
    val firstname: String?,
    val lastname: String?
)

data class Address(
    val city: String?,
    val geolocation: Geolocation?,
    val number: Int?,
    val street: String?,
    val zipcode: String?
)

data class Geolocation(
    val lat: String?,
    val long: String?
)

data class Cart(
    val id: Int?,
    val userId: Int?,
    val date: Date?,
    val products: ArrayList<Product>
)

data class Product(
    var productId: Int?,
    var quantity: Int?
)
