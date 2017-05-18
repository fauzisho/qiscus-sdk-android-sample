package com.qiscus.sdksample.qiscussamplesdk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.util.PatternsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.qiscus.sdk.Qiscus
import java.util.*

/**
 * Created by zetra. on 9/19/16.
 */
class RoomsActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var adapter: EmailAdapter? = null
    private var sharedPreferences: SharedPreferences? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        sharedPreferences = getSharedPreferences("rooms", Context.MODE_PRIVATE)

        customizeChatUi()

        adapter = EmailAdapter(this, ArrayList(emails))
        adapter!!.setOnClickListener(object : EmailAdapter.OnClickListener {
            override fun onClick(position: Int) {
                openChatWith(adapter!!.emails[position])
            }
        })
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView!!.adapter = adapter
    }

    private fun customizeChatUi() {
        Qiscus.getChatConfig()
                .setStatusBarColor(android.R.color.holo_green_dark)
                .setAppBarColor(android.R.color.holo_green_dark)
                .setTitleColor(android.R.color.white)
                .setLeftBubbleColor(android.R.color.holo_green_dark)
                .setRightBubbleColor(android.R.color.holo_blue_dark)
                .setRightBubbleTextColor(android.R.color.white).rightBubbleTimeColor = android.R.color.white
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.rooms, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure wants to logout?")
                    .setPositiveButton("Logout") { _, _ ->
                        Qiscus.clearUser()
                        sharedPreferences!!.edit().clear().apply()
                        startActivity(Intent(this@RoomsActivity, MainActivity::class.java))
                        finish()
                    }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createNewChat(view : View) {
        val emailField = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        emailField.layoutParams = lp
        emailField.hint = "Email"

        AlertDialog.Builder(this)
                .setTitle("New Chat")
                .setView(emailField)
                .setPositiveButton("Submit") { dialog, _ ->
                    val email = emailField.text.toString()
                    if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailField.error = "Please insert a valid email!"
                    } else {
                        openChatWith(email)
                        dialog.dismiss()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .show()
    }

    private fun openChatWith(email: String) {
        showLoading()
        Qiscus.buildChatWith(email)
                .withTitle(email)
                .build(this, object : Qiscus.ChatActivityBuilderListener {
                    override fun onSuccess(intent: Intent) {
                        saveEmail(email)
                        startActivity(intent)
                        dismissLoading()
                    }

                    override fun onError(throwable: Throwable) {
                        throwable.printStackTrace()
                        showError("Failed to create chatroom, make sure $email is registered!")
                        dismissLoading()
                    }
                })
    }

    private val emails: MutableSet<String>
        get() = sharedPreferences!!.getStringSet("emails", HashSet<String>())

    private fun saveEmail(email: String) {
        val emails = emails
        emails.add(email)
        sharedPreferences!!.edit().putStringSet("emails", emails).apply()
        updateList(email)
    }

    private fun updateList(email: String) {
        if (!adapter!!.emails.contains(email)) {
            adapter!!.emails.add(email)
            adapter!!.notifyDataSetChanged()
        }
    }

    fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    fun showLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage("Please wait...")
        }
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun dismissLoading() {
        progressDialog!!.dismiss()
    }
}
