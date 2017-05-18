package com.qiscus.sdksample.qiscussamplesdk

import android.content.Intent
import android.os.Bundle
import android.support.v4.util.PatternsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.qiscus.sdk.Qiscus
import com.qiscus.sdk.data.model.QiscusAccount

class MainActivity : AppCompatActivity() {

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
        emailField = findViewById(R.id.email) as EditText
        userKeyField = findViewById(R.id.key) as EditText
        progressBar = findViewById(R.id.progress) as ProgressBar
    }

    fun setupUser(view : View) {
        val email = emailField!!.text.toString()
        val key = userKeyField!!.text.toString()
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField!!.error = "Please insert a valid email!"
        } else if (key.isEmpty()) {
            userKeyField!!.error = "Please insert your user key!"
        } else {
            progressBar!!.visibility = View.VISIBLE
            Qiscus.setUser(email, key)
                    .save(object : Qiscus.SetUserListener {
                        override fun onSuccess(qiscusAccount: QiscusAccount) {
                            startActivity(Intent(this@MainActivity, RoomsActivity::class.java))
                            progressBar!!.visibility = View.GONE
                            finish()
                        }

                        override fun onError(throwable: Throwable) {
                            throwable.printStackTrace()
                            Toast.makeText(this@MainActivity, throwable.message, Toast.LENGTH_SHORT).show()
                            progressBar!!.visibility = View.GONE
                        }
                    })
        }
    }
}
