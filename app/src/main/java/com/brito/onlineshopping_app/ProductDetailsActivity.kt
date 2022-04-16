package com.brito.onlineshopping_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        var productId = getIntent().getIntExtra("ProductId_mainA", 0)

        var path = productId.toString()
        val serviceGenerator = ServiceGenerator.api.getProduct(path)

        serviceGenerator.enqueue(object : Callback<Products> {
            override fun onResponse(
                call: Call<Products>,
                response: Response<Products>
            ) {
                if(response.isSuccessful) {
                    product_name_product_details_act.text = response.body()!!.title
                    product_price_product_details_act.text = response.body()!!.price
                    product_description_product_details_act.text = response.body()!!.description
                    val loadImageView = product_img_product_details_act
                    Picasso.get()
                        .load(response.body()!!.image)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(loadImageView)
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })

        //Exit BTN
        val exitBtn = findViewById<ImageButton>(R.id.exitIcon)
        exitBtn.setOnClickListener {
            val eBuilder = AlertDialog.Builder(this)
            eBuilder.setTitle("Exit")
            eBuilder.setIcon(R.drawable.ic_baseline_warning_24)
            eBuilder.setMessage("Do you really want to exit ?")
            eBuilder.setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            eBuilder.setNegativeButton("No") { _, _ ->
            }
            val createBuild = eBuilder.create()
            createBuild.show()
        }

        //Home BTN
        val homeBtn = findViewById<ImageButton>(R.id.homeIcon)
        homeBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cart BTN
        val cartBtn = findViewById<ImageButton>(R.id.cartIcon)
        cartBtn.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    fun showCategoriesDropDownMenu(v: View){
        var popup: PopupMenu = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View){
        var popup: PopupMenu = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_no_user)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            //Categories Menu
            (R.id.allproducts_menu) -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Category", "allproducts_cat")
                startActivity(intent)
                return true
            }
            //No User Menu
            (R.id.log_in_menu) -> {
                val intent = Intent(this, SignInPageActivity::class.java)
                startActivity(intent)
                return true
            }
            //User logged in MEnu
            (R.id.log_out_menu) -> {
                val intent = Intent(this, SignInPageActivity::class.java)
                startActivity(intent)
                return true
            }
            (R.id.user_cart_menu) -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }

}