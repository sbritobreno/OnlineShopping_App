package com.brito.onlineshopping_app.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brito.onlineshopping_app.retrofit.ServiceGenerator
import com.brito.onlineshopping_app.Token
import com.brito.onlineshopping_app.UserLogin
import com.brito.onlineshopping_app.utils.currentToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivityViewModel: ViewModel() {

    var loginUserLiveData: MutableLiveData<Token?> = MutableLiveData()

    fun getLoginUserObserver(): MutableLiveData<Token?> {
        return loginUserLiveData
    }

    fun loginUser(user: UserLogin) {

        val serviceGenerator = ServiceGenerator.api.login(user, currentToken.toString())

        serviceGenerator.enqueue(object: Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                loginUserLiveData.postValue(null)
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if(response.isSuccessful) {
                    currentToken = response.body()!!
                    Log.d("success", currentToken.token.toString())
                    loginUserLiveData.postValue(response.body())
                } else {
                    Log.d("failed", response.body().toString())
                    loginUserLiveData.postValue(null)
                }
            }
        })
    }
}