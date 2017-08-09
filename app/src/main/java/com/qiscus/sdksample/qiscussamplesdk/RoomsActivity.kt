package com.qiscus.sdksample.qiscussamplesdk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qiscus.sdk.Qiscus
import retrofit2.HttpException
import java.io.IOException

class RoomsActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var adapter: ContactAdapter? = null
    private var sharedPreferences: SharedPreferences? = null
    private var progressDialog: ProgressDialog? = null

    private var gson: Gson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        gson = Gson()

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        sharedPreferences = getSharedPreferences("rooms", Context.MODE_PRIVATE)

        adapter = ContactAdapter(this, contacts)
        adapter!!.setOnClickListener(object : ContactAdapter.OnClickListener {
            override fun onClick(position: Int) {
                openChatWith(adapter!!.contacts!![position])
            }
        })
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = adapter
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

    fun createNewChat(view: View) {
        NewChatDialog.newInstance(object : NewChatDialog.Listener {
            override fun onSubmit(name: String, email: String) {
                openChatWith(Contact(email, name))
            }
        }).show(supportFragmentManager, TAG)
    }

    private fun openChatWith(contact: Contact) {
        showLoading()
        Qiscus.buildChatWith(contact.email)
                .withTitle(contact.name)
                .build(this, object : Qiscus.ChatActivityBuilderListener {
                    override fun onSuccess(intent: Intent) {
                        saveContact(contact)
                        startActivity(intent)
                        dismissLoading()
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
                        throwable.printStackTrace()
                        dismissLoading()
                    }
                })
    }

    private val contacts: MutableList<Contact>
        get() {
            val json = sharedPreferences!!.getString("contacts", "")
            if (json.isEmpty()) {
                return ArrayList()
            }
            return gson!!.fromJson<List<Contact>>(json, object : TypeToken<List<Contact>>() {}.type) as MutableList<Contact>
        }

    private fun saveContact(contact: Contact) {
        var contacts: MutableList<Contact>? = contacts
        if (contacts == null) {
            contacts = ArrayList<Contact>()
        }

        if (!contacts.contains(contact)) {
            contacts.add(contact)
            sharedPreferences!!.edit()
                    .putString("contacts", gson!!.toJson(contacts))
                    .apply()
            updateList(contact)
        }
    }

    private fun updateList(contact: Contact) {
        if (!adapter!!.contacts!!.contains(contact)) {
            adapter!!.contacts!!.add(contact)
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

    companion object {
        private val TAG = RoomsActivity::class.java.simpleName
    }
}
