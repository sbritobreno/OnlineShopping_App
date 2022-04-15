package com.brito.onlineshopping_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownServiceException

class SignUpPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        var listOfUsers: ArrayList<User> = arrayListOf()

        val serviceGenerator = ServiceGenerator.buildService(ApiAllUsersService::class.java)
        val call = serviceGenerator.getAllUsers()

        call.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if(response.isSuccessful) {
                    listOfUsers = response.body()!!
                    Log.e("success", listOfUsers.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }
        })

        val signUpBtn = findViewById<Button>(R.id.signUp_btn)
        signUpBtn.setOnClickListener{

            val email = findViewById<EditText>(R.id.email_signUp_act).text.toString()
            val username = findViewById<EditText>(R.id.username_signUp_act).text.toString()
            val firstName = findViewById<EditText>(R.id.firstname_signUp_act).text.toString()
            val lastName = findViewById<EditText>(R.id.lastname_signUp_act).text.toString()
            val password = findViewById<EditText>(R.id.password_signUp_act).text.toString()
            val empty = ""

            if(email == empty || username == empty || firstName == empty || lastName == empty || password == empty)
                Toast.makeText(this, "All text boxes must be filled out", Toast.LENGTH_LONG).show()
            else
                createUser(email, username, firstName, lastName, password, listOfUsers)
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
        homeBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Cart BTN
        val cartBtn = findViewById<ImageButton>(R.id.cartIcon)
        cartBtn.setOnClickListener{
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Change to Sign Up
        val changeToSignInBtn = findViewById<TextView>(R.id.change_signIn_textBtn)
        changeToSignInBtn.setOnClickListener {
            val intent = Intent(this, SignInPageActivity::class.java)
            startActivity(intent)
        }
    }

    fun createUser(email: String, username: String, firstName: String, lastName: String, password: String, users: ArrayList<User>){

        for(user in users){
            if(user.email == email || user.username == username)
                Toast.makeText(this@SignUpPageActivity, "This username or email is already been used, Try another one!!", Toast.LENGTH_LONG).show()
        }

    }

    fun showCategoriesDropDownMenu(v: View){
        var popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    fun showUserDropDownMenu(v: View){
        var popup = PopupMenu(this, v)
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
                val intent = Intent(this, SignInPageActivity::class.java)
                intent.putExtra("Login In", true)
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