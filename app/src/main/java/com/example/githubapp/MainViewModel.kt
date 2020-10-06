package com.example.githubapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUser(users: String) {
        // request API
        val listItems = ArrayList<UserItems>()
        val url = "https://api.github.com/search/users?q={username}"
        val client = AsyncHttpClient()

        client.addHeader("Authorization","token 86d33dcec21a0c5e1be8b03041cbe5b99334ac37")
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val photo = item.getString("avatar_url")
                        val userItems = UserItems()
                        userItems.username=username
                        userItems.photo=photo
                        listItems.add(userItems)
                    }

                    listUsers.postValue(listItems)

                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }
    fun getUser(): LiveData<ArrayList<UserItems>> {
        return listUsers
    }
}