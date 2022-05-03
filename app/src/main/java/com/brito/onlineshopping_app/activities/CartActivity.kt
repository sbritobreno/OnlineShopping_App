package com.brito.onlineshopping_app.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.utils.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.cartIcon
import kotlinx.android.synthetic.main.activity_main.exitIcon
import kotlinx.android.synthetic.main.activity_main.homeIcon
import kotlinx.android.synthetic.main.activity_main.noUserIcon
import kotlinx.android.synthetic.main.activity_main.userIcon
import kotlinx.android.synthetic.main.cart_recycler_view_template.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class CartActivity : AppCompatActivity(), OnCartItemClickListener, PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        checkCartStatus()

        cart_recyclerview.adapter = CartAdapter(allProductsInCart, this)
        cart_recyclerview.layoutManager = LinearLayoutManager(this)

        if (currentToken.token!!.isNotEmpty()) {
            noUserIcon.visibility = View.GONE
            userIcon.visibility = View.VISIBLE
        }

        //Exit BTN
        exitIcon.setOnClickListener {
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
        homeIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cart BTN
        cartIcon.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    fun addToCart(itemId: Int) {
        var productAlreadyInList = false
        var productAlreadyInListIndex = -1

        for (p in allProductsInCart)
            if (p.productId == itemId) {
                productAlreadyInList = true
                productAlreadyInListIndex = allProductsInCart.indexOf(p)
            }

        if (productAlreadyInList)
            allProductsInCart[productAlreadyInListIndex].quantity =
                allProductsInCart[productAlreadyInListIndex].quantity!! + 1
        else
            allProductsInCart.add(0, Product(itemId, 1))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBuyItemClick(item: Product, position: Int) {
        val eBuilder = AlertDialog.Builder(this)
        eBuilder.setTitle("Buy Products")
        eBuilder.setIcon(R.drawable.ic_baseline_warning_24)
        eBuilder.setMessage("Do you really want to continue with this purchase ?")
        eBuilder.setNegativeButton("No") { _, _ ->
        }
        eBuilder.setPositiveButton("Yes") { _, _ ->
            var product = Products()
            var price = 0.00
            for (p in productListFromTheApi)
                if (p.id == item.productId) {
                    product = p
                    price = p.price!!
                }

            val date = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDate = date.format(formatter)
            var finalPrice = price * item.quantity!!

            product.price = finalPrice
            product.quantity = item.quantity

            purchasedHistory.add(PurchasedCart(currentUserId, formattedDate, arrayListOf(product), finalPrice))
            deleteItemFromCart(item.productId!!)

            Toast.makeText(this, "Purchased Done...", Toast.LENGTH_LONG).show()
        }
        val createBuild = eBuilder.create()
        createBuild.show()
    }

    private fun deleteItemFromCart(productId: Int){
        var productToBeRemove = Product(null,null)
        for (p in allProductsInCart){
            if(p.productId == productId){
                productToBeRemove = p
            }
        }
        allProductsInCart.remove(productToBeRemove)
        cart_recyclerview.adapter!!.notifyDataSetChanged()
    }

    override fun onCartItemClick(item: Product, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductId", item.productId)
        startActivity(intent)
    }

    fun showCategoriesDropDownMenu(v: View) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_user_logged_in)
        popup.show()
    }

    fun showNoUserDropDownMenu(v: View) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_no_user)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean {
        val intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }

    fun getUserCartList() {
        val serviceGenerator = ServiceGenerator.api.getUserCart(2)
        serviceGenerator.enqueue(object : Callback<ArrayList<Cart>> {
            override fun onResponse(
                call: Call<ArrayList<Cart>>,
                response: Response<ArrayList<Cart>>
            ) {
                if (listFilledUp) {
                    for (cart in response.body()!!)
                        for (product in cart.products)
                            allProductsInCart.add(product)
                }
                listFilledUp = false
            }

            override fun onFailure(call: Call<ArrayList<Cart>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }

    private fun checkCartStatus() {
        if (currentToken.token!!.isEmpty()) Toast.makeText(
            this@CartActivity, "You are not logged in", Toast.LENGTH_LONG
        ).show()
        else if (allProductsInCart.isEmpty()) Toast.makeText(
            this@CartActivity, "Cart is empty", Toast.LENGTH_LONG
        ).show()
    }
}