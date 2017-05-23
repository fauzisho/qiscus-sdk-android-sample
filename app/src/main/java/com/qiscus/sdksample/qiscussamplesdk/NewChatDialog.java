package com.qiscus.sdksample.qiscussamplesdk;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.PatternsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class NewChatDialog extends DialogFragment implements View.OnClickListener {

    private TextView nameField;
    private TextView emailField;
    private View submitButton;
    private View cancelButton;

    private Listener listener;

    public static NewChatDialog newInstance(Listener listener) {
        NewChatDialog dialog = new NewChatDialog();
        dialog.listener = listener;
        dialog.setCancelable(false);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_chat, container, false);
        nameField = (TextView) view.findViewById(R.id.name);
        emailField = (TextView) view.findViewById(R.id.email);
        submitButton = view.findViewById(R.id.tv_submit);
        cancelButton = view.findViewById(R.id.tv_cancel);

        submitButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                if (name.isEmpty()) {
                    nameField.setError("Please insert name!");
                    nameField.requestFocus();
                } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Please insert a valid email!");
                    emailField.requestFocus();
                } else {
                    if (listener != null) {
                        listener.onSubmit(name, email);
                    }
                    dismiss();
                }
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }


    public interface Listener {
        void onSubmit(String name, String email);
    }
}
