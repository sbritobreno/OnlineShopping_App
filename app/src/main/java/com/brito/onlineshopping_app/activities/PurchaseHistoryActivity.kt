package com.brito.onlineshopping_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.adapters.HistoryAdapter
import com.brito.onlineshopping_app.adapters.OnHistoryItemClickListener
import com.brito.onlineshopping_app.utils.*
import kotlinx.android.synthetic.main.activity_main.cartIcon
import kotlinx.android.synthetic.main.activity_main.exitIcon
import kotlinx.android.synthetic.main.activity_main.homeIcon
import kotlinx.android.synthetic.main.activity_main.noUserIcon
import kotlinx.android.synthetic.main.activity_main.userIcon
import kotlinx.android.synthetic.main.activity_purchase_history.*

class PurchaseHistoryActivity : AppCompatActivity(), OnHistoryItemClickListener,
    PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        checkHistoryStatus()

        history_recyclerview.adapter = HistoryAdapter(purchasedHistory, this)
        history_recyclerview.layoutManager = LinearLayoutManager(this)


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

    override fun onItemClick(item: PurchasedCart, position: Int) {
        val intent = Intent(this, PurchaseHistoryDetailsActivity::class.java)
        singlePurchasedHistory = item.products
        startActivity(intent)
    }

    private fun checkHistoryStatus() {
        if (purchasedHistory.isEmpty()) Toast.makeText(
            this@PurchaseHistoryActivity, "You have not bought anything", Toast.LENGTH_LONG
        ).show()
    }
}