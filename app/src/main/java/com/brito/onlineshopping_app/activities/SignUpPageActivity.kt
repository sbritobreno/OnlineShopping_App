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
import com.brito.onlineshopping_app.utils.listOfUsers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        val signUpBtn = findViewById<Button>(R.id.signUp_btn)
        signUpBtn.setOnClickListener {

            val email = findViewById<EditText>(R.id.email_signUp_act).text.toString()
            val username = findViewById<EditText>(R.id.username_signUp_act).text.toString()
            val firstName = findViewById<EditText>(R.id.firstname_signUp_act).text.toString()
            val lastName = findViewById<EditText>(R.id.lastname_signUp_act).text.toString()
            val password = findViewById<EditText>(R.id.password_signUp_act).text.toString()

            if (email.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty())
                Toast.makeText(this, "All text boxes must be filled out", Toast.LENGTH_LONG).show()
            else if (checkIfUserExist(email, username, listOfUsers))
                Toast.makeText(
                    this@SignUpPageActivity,
                    "This username or email is already been used, Try another one!!",
                    Toast.LENGTH_LONG
                ).show()
            else {
                val newUser = createUser(email, username, firstName, lastName, password)
                updateApiListOfUsers(newUser)
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
        val changeToSignInBtn = findViewById<TextView>(R.id.change_signIn_textBtn)
        changeToSignInBtn.setOnClickListener {
            val intent = Intent(this, SignInPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfUserExist(email: String, username: String, users: ArrayList<UserResponse>): Boolean {
        for (user in users) {
            if (user.email == email || user.username == username)
                return true
        }
        return false
    }

    private fun createUser(
        email: String,
        username: String,
        firstName: String,
        lastName: String,
        password: String
    ): UserRequest {
        return UserRequest(email, Name(firstName, lastName), password, username)
    }

    private fun updateApiListOfUsers(userRequest: UserRequest) {

        val serviceGenerator = ServiceGenerator.api.postNewUser(userRequest)

        serviceGenerator.enqueue(
            object : Callback<UserResponse> {

                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {

                    if (response.isSuccessful) {
                        Log.d("successfully", response.body().toString())
                    } else
                        Log.d("successfully failed", response.errorBody().toString())
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("error", t.message.toString())
                }
            }
        )

    }

    fun showCategoriesDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean{
        var intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }
}

