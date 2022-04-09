package com.brito.onlineshopping_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_log_in_page.*

class LogInPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

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

        val signInBtn = findViewById<Button>(R.id.sign_in_btn)
        signInBtn.setOnClickListener {
            onSignInBtnClick()
        }

        var signIn = getIntent().getBooleanExtra("Sign In", false)

        if(signIn)
            onSignInBtnClick()

    }

    fun onSignInBtnClick(){
        email_login_act.visibility = View.VISIBLE
        firstname_login_act.visibility = View.VISIBLE
        lastname_login_act.visibility = View.VISIBLE
        login_btn.visibility = View.GONE
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
            (R.id.home_menu) -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            (R.id.allproducts_menu) -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Category", "allproducts_cat")
                startActivity(intent)
                return true
            }
            (R.id.desktop_menu) -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Category", "desktop_cat")
                startActivity(intent)
                return true
            }
            (R.id.laptop_menu) -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("Category", "laptop_cat")
                startActivity(intent)
                return true
            }
            //No User Menu
            (R.id.sign_in_menu) -> {
                val intent = Intent(this, LogInPageActivity::class.java)
                intent.putExtra("Sign In", true)
                startActivity(intent)
                return true
            }
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