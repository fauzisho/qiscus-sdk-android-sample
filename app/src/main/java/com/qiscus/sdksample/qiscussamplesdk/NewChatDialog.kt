package com.qiscus.sdksample.qiscussamplesdk

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.util.PatternsCompat
import android.view.*
import android.widget.TextView

class NewChatDialog : DialogFragment(), View.OnClickListener {

    private var nameField: TextView? = null
    private var emailField: TextView? = null
    private var submitButton: View? = null
    private var cancelButton: View? = null

    private var listener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_new_chat, container, false)
        nameField = view.findViewById(R.id.name) as TextView
        emailField = view.findViewById(R.id.email) as TextView
        submitButton = view.findViewById(R.id.tv_submit)
        cancelButton = view.findViewById(R.id.tv_cancel)

        submitButton!!.setOnClickListener(this)
        cancelButton!!.setOnClickListener(this)

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_submit -> {
                val name = nameField!!.text.toString()
                val email = emailField!!.text.toString()
                if (name.isEmpty()) {
                    nameField!!.error = "Please insert name!"
                    nameField!!.requestFocus()
                } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField!!.error = "Please insert a valid email!"
                    emailField!!.requestFocus()
                } else {
                    if (listener != null) {
                        listener!!.onSubmit(name, email)
                    }
                    dismiss()
                }
            }
            R.id.tv_cancel -> dismiss()
        }
    }


    interface Listener {
        fun onSubmit(name: String, email: String)
    }

    companion object {

        fun newInstance(listener: Listener): NewChatDialog {
            val dialog = NewChatDialog()
            dialog.listener = listener
            dialog.isCancelable = false
            return dialog
        }
    }
}
