package com.brito.onlineshopping_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.utils.MenuDropDowns
import com.brito.onlineshopping_app.utils.currentToken
import com.brito.onlineshopping_app.utils.productListFromTheApi
import com.brito.onlineshopping_app.utils.singlePurchasedHistory
import kotlinx.android.synthetic.main.activity_main.cartIcon
import kotlinx.android.synthetic.main.activity_main.exitIcon
import kotlinx.android.synthetic.main.activity_main.homeIcon
import kotlinx.android.synthetic.main.activity_main.noUserIcon
import kotlinx.android.synthetic.main.activity_main.userIcon
import kotlinx.android.synthetic.main.activity_purchase_history_details.*

class PurchaseHistoryDetailsActivity : AppCompatActivity(), OnHistoryDetailsItemClickListener, PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history_details)

        //fix
        history_details_recyclerview.adapter = HistoryDetailsAdapter(singlePurchasedHistory, this)
        history_details_recyclerview.layoutManager = LinearLayoutManager(this)

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

    override fun onItemClick(item: Products, position: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("ProductId", item.id)
        startActivity(intent)
    }
}