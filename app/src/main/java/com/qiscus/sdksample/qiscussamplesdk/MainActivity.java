package com.qiscus.sdksample.qiscussamplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.PatternsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusAccount;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText userKeyField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Qiscus.hasSetupUser()) {
            startActivity(new Intent(this, RoomsActivity.class));
            finish();
            return;
        }
        emailField = (EditText) findViewById(R.id.email);
        userKeyField = (EditText) findViewById(R.id.key);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public void setupUser(View view) {
        String email = emailField.getText().toString();
        String key = userKeyField.getText().toString();
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please insert a valid email!");
        } else if (key.isEmpty()) {
            userKeyField.setError("Please insert your user key!");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Qiscus.setUser(email, key)
                    .save(new Qiscus.SetUserListener() {
                        @Override
                        public void onSuccess(QiscusAccount qiscusAccount) {
                            startActivity(new Intent(MainActivity.this, RoomsActivity.class));
                            progressBar.setVisibility(View.GONE);
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
