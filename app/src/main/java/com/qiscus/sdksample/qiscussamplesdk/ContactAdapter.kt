package com.qiscus.sdksample.qiscussamplesdk

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class ContactAdapter(private val context: Context, contacts: MutableList<Contact>?) : RecyclerView.Adapter<ContactAdapter.ContactVH>() {
    var contacts: MutableList<Contact>? = null
        private set
    private var clickListener: OnClickListener? = null

    init {
        if (contacts != null) {
            this.contacts = contacts
        } else {
            this.contacts = ArrayList<Contact>()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        return ContactVH(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) {
        holder.bind(contacts!![position])
    }

    override fun getItemCount(): Int {
        return contacts!!.size
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.clickListener = clickListener
    }

    inner class ContactVH(itemView: View, private val clickListener: OnClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val nameText: TextView = itemView.findViewById(R.id.name) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(contact: Contact) {
            nameText.text = contact.name
        }

        override fun onClick(v: View) {
            clickListener?.onClick(adapterPosition)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
