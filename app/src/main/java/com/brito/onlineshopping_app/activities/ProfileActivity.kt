package com.brito.onlineshopping_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.retrofit.ServiceGenerator
import com.brito.onlineshopping_app.utils.MenuDropDowns
import com.brito.onlineshopping_app.utils.currentToken
import com.brito.onlineshopping_app.utils.currentUserId
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val path = currentUserId!!
        getUserProfile(path)

        //Log out BTN
        user_logout_btn_profileAct.setOnClickListener {
            val intent = Intent(this, SignInPageActivity::class.java)
            val eBuilder = AlertDialog.Builder(this)
            eBuilder.setTitle("Log Out")
            eBuilder.setIcon(R.drawable.ic_baseline_warning_24)
            eBuilder.setMessage("Do you really want to log out ?")
            eBuilder.setPositiveButton("Yes") { _, _ ->
                currentToken.token = ""
                currentUserId = 0
                finishAffinity()
                startActivity(intent)
            }
            eBuilder.setNegativeButton("No") { _, _ ->
            }
            val createBuild = eBuilder.create()
            createBuild.show()

        }

        //About the app BTN
        aboutTheApp_btn_profileAct.setOnClickListener {
            val intent = Intent(this, AboutThisAppActivity::class.java)
            startActivity(intent)
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

    private fun getUserProfile(path: Int){
        val serviceGenerator = ServiceGenerator.api.getSingleUser(path, currentToken.toString())

        serviceGenerator.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val fullName = response.body()!!.name?.firstname + " " + response.body()!!.name?.lastname
                    user_fullName.text = fullName
                    user_username.text = response.body()!!.username
                    user_email.text = response.body()!!.email

                    val loadImageView = user_photo
                    val dpUrl = "https://thispersondoesnotexist.com/image"
                    Picasso.get()
                        .load(dpUrl)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(loadImageView)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })
    }
}