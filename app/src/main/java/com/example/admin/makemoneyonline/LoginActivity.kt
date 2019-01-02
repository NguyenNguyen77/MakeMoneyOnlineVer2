package com.example.admin.makemoneyonline

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.facebook.GraphRequest
import com.facebook.login.widget.LoginButton
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import com.facebook.AccessToken
import org.json.JSONException


class LoginActivity : AppCompatActivity() {


    private var callbackManager: CallbackManager? = null
    private var loginButton: LoginButton? = null
    private var mFirstName: String = ""
    private var mLastName: String = ""
    private var mName: String = ""
    private var mEmail: String = ""
    private var mBirthday: String = ""
    private var mGender: String = ""
    private var mUserId: String = ""
    private var mViewManager: ViewManager = ViewManager.getInstance()
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewManager.setActivity(this)

        printHashKey(this)
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null) {
            Log.i(TAG, "accessToken: $accessToken")
            getUserData(accessToken)
            val isLoggedIn = accessToken != null && !accessToken!!.isExpired
            Log.i(TAG, "isLoggedIn: $isLoggedIn")
        }
        callbackManager = CallbackManager.Factory.create()

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton!!.setReadPermissions("email")
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null) {
            val token = accessToken.token
            val userID = accessToken.userId
            Log.i(TAG, "Facebook Token: $token")
            Log.i(TAG, "Facebook userID: $userID")
            getUserData(accessToken)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getUserData(accessToken: AccessToken?) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            try {
                if (`object` != null) {
                    mUserId = `object`.getString("id")
                    val url = "https://graph.facebook.com/$mUserId/picture?width=500&height=500"
                    val profilePicture = URL(url)

                    if (`object`.has("first_name"))
                        mFirstName = `object`.getString("first_name")
                    if (`object`.has("name"))
                        mName = `object`.getString("name")
                    if (`object`.has("last_name"))
                        mLastName = `object`.getString("last_name")
                    if (`object`.has("email"))
                        mEmail = `object`.getString("email")
                    if (`object`.has("birthday"))
                        mBirthday = `object`.getString("birthday")
                    if (`object`.has("gender"))
                        mGender = `object`.getString("gender")
                    Log.i(TAG, "Facebook url: $url")
                    Log.i(TAG, "Facebook mUserId: " + mUserId)
                    Log.i(TAG, "Facebook mName: " + mName)
                    Log.i(TAG, "Facebook mFirstName: " + mFirstName)
                    Log.i(TAG, "Facebook mLastName: " + mLastName)
                    Log.i(TAG, "Facebook mEmail: " + mEmail)
                    Log.i(TAG, "Facebook mBirthday: " + mBirthday)
                    Log.i(TAG, "Facebook mGender: " + mGender)

                    reqLogintoServer(mUserId, mName, mEmail, mBirthday, `object`.toString())
                    mViewManager.setView(ViewManager.VIEW_KEY.MAIN_SCREEN)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,birthday,gender,first_name,last_name")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun reqLogintoServer(userid: String, name: String, email: String, birthday: String, token: String) {

    }
}

