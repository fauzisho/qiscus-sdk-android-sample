package com.qiscus.sdksample.qiscussamplesdk

import android.content.Intent
import android.os.Bundle
import android.support.v4.util.PatternsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.qiscus.sdk.Qiscus
import com.qiscus.sdk.data.model.QiscusAccount
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var nameField: EditText? = null
    private var emailField: EditText? = null
    private var userKeyField: EditText? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Qiscus.hasSetupUser()) {
            startActivity(Intent(this, RoomsActivity::class.java))
            finish()
            return
        }
        nameField = findViewById(R.id.name) as EditText
        emailField = findViewById(R.id.email) as EditText
        userKeyField = findViewById(R.id.key) as EditText
        progressBar = findViewById(R.id.progress) as ProgressBar
    }

    fun setupUser(view: View) {
        val name = nameField!!.text.toString()
        val email = emailField!!.text.toString()
        val key = userKeyField!!.text.toString()
        if (name.isEmpty()) {
            nameField!!.error = "Please insert your name!"
            nameField!!.requestFocus()
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField!!.error = "Please insert a valid email!"
            emailField!!.requestFocus()
        } else if (key.isEmpty()) {
            userKeyField!!.error = "Please insert your user key!"
            userKeyField!!.requestFocus()
        } else {
            progressBar!!.visibility = View.VISIBLE
            Qiscus.setUser(email, key)
                    .withUsername(name)
                    .save(object : Qiscus.SetUserListener {
                        override fun onSuccess(qiscusAccount: QiscusAccount) {
                            startActivity(Intent(this@MainActivity, RoomsActivity::class.java))
                            progressBar!!.visibility = View.GONE
                            finish()
                        }

                        override fun onError(throwable: Throwable) {
                            if (throwable is HttpException) { //Error response from server
                                try {
                                    val errorMessage = throwable.response().errorBody()!!.string()
                                    Log.e(TAG, errorMessage)
                                    showError(errorMessage)
                                } catch (e1: IOException) {
                                    e1.printStackTrace()
                                }

                            } else if (throwable is IOException) { //Error from network
                                showError("Can not connect to qiscus server!")
                            } else { //Unknown error
                                showError("Unexpected error!")
                            }
                            progressBar!!.visibility = View.GONE
                        }
                    })
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
