package com.qiscus.sdksample.qiscussamplesdk

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by zetra. on 9/20/16.
 */

class EmailAdapter(private val context: Context, val emails: MutableList<String>) : RecyclerView.Adapter<EmailAdapter.EmailVH>() {
    private var clickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailVH {
        return EmailVH(LayoutInflater.from(context).inflate(R.layout.item_email, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: EmailVH, position: Int) {
        holder.bind(emails[position])
    }

    override fun getItemCount(): Int {
        return emails.size
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.clickListener = clickListener
    }

    inner class EmailVH(itemView: View, private val clickListener: OnClickListener?) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val emailText: TextView = itemView.findViewById(R.id.email) as TextView

        init {

            itemView.setOnClickListener(this)
        }

        fun bind(email: String) {
            emailText.text = email
        }

        override fun onClick(v: View) {
            clickListener?.onClick(adapterPosition)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
