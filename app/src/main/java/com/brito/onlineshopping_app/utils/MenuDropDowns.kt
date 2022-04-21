package com.brito.onlineshopping_app.utils

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import com.brito.onlineshopping_app.R
import com.brito.onlineshopping_app.activities.CartActivity
import com.brito.onlineshopping_app.activities.MainActivity
import com.brito.onlineshopping_app.activities.ProductsByCategoryActivity
import com.brito.onlineshopping_app.activities.SignInPageActivity

open class MenuDropDowns {

     fun onItemClick(item: MenuItem, act: Activity): Intent {
        when (item.itemId) {
            //Categories Menu
            (R.id.allproducts_menu) -> {
                return Intent(act, MainActivity::class.java)
            }
            (R.id.electronics_menu) -> {
                val intent = Intent(act, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "electronics")
                return intent
            }
            (R.id.jewelery_menu) -> {
                val intent = Intent(act, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "jewelery")
                return intent
            }
            (R.id.men_clothing_menu) -> {
                val intent = Intent(act, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "men's clothing")
                return intent
            }
            (R.id.women_clothing_menu) -> {
                val intent = Intent(act, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "women's clothing")
                return intent
            }

            //No User Menu
            (R.id.log_in_menu) -> {
                return Intent(act, SignInPageActivity::class.java)
            }
            //User logged in MEnu
            (R.id.log_out_menu) -> {
                return Intent(act, SignInPageActivity::class.java)
            }
            (R.id.user_cart_menu) -> {
                return Intent(act, CartActivity::class.java)
            }
            else -> return Intent(act, act::class.java)
        }
    }
}