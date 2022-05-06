package com.brito.onlineshopping_app.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brito.onlineshopping_app.*
import com.brito.onlineshopping_app.retrofit.ServiceGenerator
import com.brito.onlineshopping_app.utils.MenuDropDowns
import com.brito.onlineshopping_app.utils.currentUserId
import com.brito.onlineshopping_app.utils.listOfUsers
import kotlinx.android.synthetic.main.activity_sign_in_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInPageActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    lateinit var viewModel: SignInActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)

        initViewModel()
        getUserList()

        signIn_btn.setOnClickListener {
            val username = findViewById<EditText>(R.id.username_login_act).text.toString()
            val password = findViewById<EditText>(R.id.password_login_act).text.toString()
            login(username, password)
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

        // Change to Sign Up
        change_signUp_textBtn.setOnClickListener {
            val intent = Intent(this, SignUpPageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SignInActivityViewModel::class.java)
        viewModel.getLoginUserObserver().observe(this, Observer <Token?>{

            if(it  != null)
                Toast.makeText(this@SignInPageActivity, "Login Successfully", Toast.LENGTH_LONG).show()
        })
    }

    private fun checkIfUserExist(password: String, username: String, users: ArrayList<UserResponse>): Boolean {
        for (user in users) {
            if (user.password == password && user.username == username)
                return true
        }
        return false
    }

    fun showCategoriesDropDownMenu(v: View){
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_category)
        popup.show()
    }

    // Options from dropdown menus
    override fun onMenuItemClick(item: MenuItem): Boolean{
        val intent = MenuDropDowns().onItemClick(item, this)
        startActivity(intent)
        return true
    }

    private fun login(username: String, password: String){
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All text boxes must be filled out", Toast.LENGTH_LONG).show()
        }
        else if (checkIfUserExist(password, username, listOfUsers)) {
            val user  = UserLogin(username, password)
            viewModel.loginUser(user)

            for (u in listOfUsers){
                if(u.username == user.username)
                    currentUserId = u.id
            }

            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }, 1500)
        }
        else {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show()
        }
    }

    private fun getUserList(){
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
    }
}