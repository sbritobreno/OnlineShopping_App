package com.brito.onlineshopping_app.activities

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
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.utils.MenuDropDowns
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity(), OnCartItemClickListener, PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView = findViewById<RecyclerView>(R.id.cart_recyclerview)

        val serviceGenerator = ServiceGenerator.api.getUserCart(currentUserId!!)

        serviceGenerator.enqueue(object : Callback<ArrayList<Cart>> {
            override fun onResponse(
                call: Call<ArrayList<Cart>>,
                response: Response<ArrayList<Cart>>
            ) {
                if (response.isSuccessful)
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@CartActivity)
                        adapter = CartAdapter(response.body()!!, this@CartActivity)

                        if (currentToken.token!!.isEmpty())
                            Toast.makeText(this@CartActivity, "You are not logged in", Toast.LENGTH_LONG).show()
                        else if(response.body()!!.isNullOrEmpty())
                            Toast.makeText(this@CartActivity, "Cart is empty", Toast.LENGTH_LONG).show()
                    }
            }

            override fun onFailure(call: Call<ArrayList<Cart>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })

        if (currentToken.token!!.isNotEmpty()) {
            noUserIcon.visibility = View.GONE
            userIcon.visibility = View.VISIBLE
        }

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

    override fun onCartItemClick(item: Cart, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductId_mainA", item.products[0].productId)
        startActivity(intent)
    }

    fun showCategoriesDropDownMenu(v: View) {
        var popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View) {
        var popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_user_logged_in)
        popup.show()
    }

    fun showNoUserDropDownMenu(v: View) {
        var popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_no_user)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean {
        var intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }
}