package com.brito.onlineshopping_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsByCategoryActivity : AppCompatActivity(), OnProductItemClickListener,
    PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_by_category)

        val recyclerView = findViewById<RecyclerView>(R.id.product_by_category_recyclerview)

        var category = getIntent().getStringExtra("Category")

        var path = category.toString()
        val serviceGenerator = ServiceGenerator.buildService(ApiProductsByCategoryService::class.java)
        val call = serviceGenerator.getProductsByCategory(path)

        call.enqueue(object : Callback<ArrayList<Products>> {
            override fun onResponse(
                call: Call<ArrayList<Products>>,
                response: Response<ArrayList<Products>>
            ) {
                if (response.isSuccessful)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@ProductsByCategoryActivity)
                        adapter = PostAdapter(response.body()!!, this@ProductsByCategoryActivity)
                    }
            }

            override fun onFailure(call: Call<ArrayList<Products>>, t: Throwable) {
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
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cart BTN
        val cartBtn = findViewById<ImageButton>(R.id.cartIcon)
        cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(item: Products, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductId_mainA", item.id)
        startActivity(intent)
    }

    fun showCategoriesDropDownMenu(v: View) {
        var popup: PopupMenu = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View) {
        var popup: PopupMenu = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_no_user)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            //Categories Menu
            (R.id.allproducts_menu) -> {
                val intent = Intent(this, ProductsByCategoryActivity::class.java)
                startActivity(intent)
                return true
            }
            (R.id.electronics_menu) -> {
                val intent = Intent(this, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "electronics")
                startActivity(intent)
                return true
            }
            (R.id.jewelery_menu) -> {
                val intent = Intent(this, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "jewelery")
                startActivity(intent)
                return true
            }
            (R.id.men_clothing_menu) -> {
                val intent = Intent(this, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "men's clothing")
                startActivity(intent)
                return true
            }
            (R.id.women_clothing_menu) -> {
                val intent = Intent(this, ProductsByCategoryActivity::class.java)
                intent.putExtra("Category", "women's clothing")
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
                Toast.makeText(this, "My Cart", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return false
        }
    }
}