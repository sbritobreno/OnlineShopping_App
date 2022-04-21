package com.brito.onlineshopping_app

// Products
data class Products (
    val id:Int? = null,
    val title: String? = null,
    val price: String? = null,
    val category: String? = null,
    val description: String? = null,
    val image: String? = null,
    val quantity: Int? = 0
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
