package com.brito.onlineshopping_app.activities
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.adapters.CartAdapter
import com.brito.onlineshopping_app.adapters.OnCartItemClickListener
import com.brito.onlineshopping_app.retrofit.ServiceGenerator
import com.brito.onlineshopping_app.utils.*
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.cartIcon
import kotlinx.android.synthetic.main.activity_main.exitIcon
import kotlinx.android.synthetic.main.activity_main.homeIcon
import kotlinx.android.synthetic.main.activity_main.noUserIcon
import kotlinx.android.synthetic.main.activity_main.userIcon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

            purchasedHistory.add(0, PurchasedCart(currentUserId, formattedDate, arrayListOf(product), finalPrice))
            deleteItemFromCart(item.productId!!)

            var productName = ""
            for (p in productListFromTheApi)
                if(p.id == item.productId)
                    productName = p.title!!

            createNotificationChannel()
            sendNotification(productName)
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

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "General Notifications Channel", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "New Purchase"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(productName: String) {
        val intent = Intent(this, PurchaseHistoryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("New Purchase")
            .setContentText("Product purchased successfully")
            .setSmallIcon(R.drawable.ic_baseline_shopping_cart_24)
            .setStyle(NotificationCompat.BigTextStyle().bigText(productName))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationId = 101
        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
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
        val serviceGenerator = ServiceGenerator.api.getUserCart(currentUserId!!, currentToken.toString())
        serviceGenerator.enqueue(object : Callback<ArrayList<Cart>> {
            override fun onResponse(
                call: Call<ArrayList<Cart>>,
                response: Response<ArrayList<Cart>>
            ) {
                if (listFilledUp.not()) {
                    for (cart in response.body()!!)
                        for (product in cart.products)
                            allProductsInCart.add(product)
                }
                listFilledUp = true
            }

            override fun onFailure(call: Call<ArrayList<Cart>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }

    private fun checkCartStatus() {
        if (currentToken.token!!.isEmpty()){
            allProductsInCart = arrayListOf()
            Toast.makeText(this@CartActivity, "You are not logged in", Toast.LENGTH_LONG).show()
        }
        else if (allProductsInCart.isEmpty()) Toast.makeText(
            this@CartActivity, "Cart is empty", Toast.LENGTH_LONG
        ).show()
    }
}