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
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.adapters.OnProductItemClickListener
import com.brito.onlineshopping_app.adapters.PostAdapter
import com.brito.onlineshopping_app.retrofit.ServiceGenerator
import com.brito.onlineshopping_app.utils.MenuDropDowns
import com.brito.onlineshopping_app.utils.currentToken
import kotlinx.android.synthetic.main.activity_main.noUserIcon
import kotlinx.android.synthetic.main.activity_main.userIcon
import kotlinx.android.synthetic.main.activity_products_by_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsByCategoryActivity : AppCompatActivity(), OnProductItemClickListener, PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_by_category)

        val category = intent.getStringExtra("Category")
        val path = category.toString()
        getProductsByCategory(path)

        if(currentToken.token!!.isNotEmpty()){
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
        homeIcon.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cart BTN
        cartIcon.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(item: Products, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductId", item.id)
        startActivity(intent)
    }
    override fun onAddCartClick(item: Products, position: Int) {
        if(currentToken.token!!.isNotEmpty()) {
            Toast.makeText(this, "Product added to cart", Toast.LENGTH_LONG).show()
            CartActivity().addToCart(item.id!!)
        }else{
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_LONG).show()
        }
    }

    fun showCategoriesDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_user_logged_in)
        popup.show()
    }

    fun showNoUserDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_no_user)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean{
        val intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }

    private fun getProductsByCategory(path: String){
        val serviceGenerator = ServiceGenerator.api.getProductsByCategory(path, currentToken.toString())

        serviceGenerator.enqueue(object : Callback<ArrayList<Products>> {
            override fun onResponse(
                call: Call<ArrayList<Products>>,
                response: Response<ArrayList<Products>>
            ) {
                if (response.isSuccessful) {
                        product_by_category_recyclerview.layoutManager = LinearLayoutManager(this@ProductsByCategoryActivity)
                        product_by_category_recyclerview.adapter = PostAdapter(response.body()!!, this@ProductsByCategoryActivity)
                    }
            }
            override fun onFailure(call: Call<ArrayList<Products>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }
}