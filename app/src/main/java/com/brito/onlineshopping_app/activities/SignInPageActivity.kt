package com.brito.onlineshopping_app.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.utils.MenuDropDowns
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        var listOfUsers: ArrayList<UserResponse> = arrayListOf()

        val serviceGenerator = ServiceGenerator.api.getAllUsers()

        serviceGenerator.enqueue(object : Callback<ArrayList<UserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserResponse>>,
                response: Response<ArrayList<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    listOfUsers = response.body()!!
                    Log.d("success", listOfUsers.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                t.printStackTrace()
                Log.d("error", t.message.toString())
            }
        })


        val loginBtn = findViewById<Button>(R.id.signIn_btn)
        loginBtn.setOnClickListener {

            val username = findViewById<EditText>(R.id.username_signUp_act).text.toString()
            val password = findViewById<EditText>(R.id.password_signUp_act).text.toString()

            if (username.isEmpty() || password.isEmpty())
                Toast.makeText(this, "All text boxes must be filled out", Toast.LENGTH_LONG).show()
            else if (checkIfUserExist(password, username, listOfUsers)) {
                val userLogin = UserLogin(password, username)
                login(userLogin)
            }
            else {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()
            }
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
        val changeToSignUpBtn = findViewById<TextView>(R.id.change_signUp_textBtn)
        changeToSignUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfUserExist(username: String, password: String, users: ArrayList<UserResponse>): Boolean {
        for (user in users) {
            if (user.password == password || user.username == username)
                return true
        }
        return false
    }

    private fun login(userLogin: UserLogin){

        val serviceGenerator = ServiceGenerator.api.login(userLogin)

            serviceGenerator.enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: Response<Token>
                ) {
                    if (response.isSuccessful) {
                        Log.d("success", response.body().toString())
                    }
                    else{
                        Toast.makeText(this@SignInPageActivity, response.body().toString(), Toast.LENGTH_LONG).show()
                        Log.d("success failed", response.body().toString())
                        Log.d("successfully failed", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("error", t.message.toString())
                }
            })

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

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean{
        var intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }
}