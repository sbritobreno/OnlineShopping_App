package com.brito.onlineshopping_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_log_in_page.*
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnProductItemClickListener, PopupMenu.OnMenuItemClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.product_recyclerview)

        val serviceGenerator = ServiceGenerator.buildService(ApiProductsService::class.java)
        val call = serviceGenerator.getPosts()

        call.enqueue(object : Callback<ArrayList<Models>> {
            override fun onResponse(
                call: retrofit2.Call<ArrayList<Models>>,
                response: Response<ArrayList<Models>>) {
                if(response.isSuccessful)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = PostAdapter(response.body()!!, this@MainActivity)
                    }
            }

            override fun onFailure(call: retrofit2.Call<ArrayList<Models>>, t: Throwable) {
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
            Toast.makeText(this, "My Cart", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, CartActivity::class.java)
            //startActivity(intent)
        }
    }

    override fun onItemClick(item: Models, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductName_mainA", item.title)
        intent.putExtra("ProductPrice_mainA", item.price)
        intent.putExtra("ProductDescription_mainA", item.description)
        startActivity(intent)
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
                val intent = Intent(this, LogInPageActivity::class.java)
                startActivity(intent)
                return true
            }
            //User logged in MEnu
            (R.id.log_out_menu) -> {
                val intent = Intent(this, LogInPageActivity::class.java)
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