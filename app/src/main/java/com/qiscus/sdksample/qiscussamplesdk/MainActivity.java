package com.qiscus.sdksample.qiscussamplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.PatternsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qiscus.sdk.chat.core.Qiscus;
import com.qiscus.sdk.chat.core.QiscusUseCaseFactory;
import com.qiscus.sdk.chat.domain.interactor.Action;
import com.qiscus.sdk.chat.domain.interactor.account.AuthenticateWithKey;
import com.qiscus.sdk.chat.domain.model.Account;

import java.io.IOException;

import retrofit2.HttpException;

import static com.qiscus.sdksample.qiscussamplesdk.R.id.email;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private QiscusUseCaseFactory useCaseFactory = Qiscus.Companion.getInstance().getUseCaseFactory();

    private EditText nameField;
    private EditText emailField;
    private EditText userKeyField;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Qiscus.Companion.getInstance().getComponent().getDataComponent().getAccountLocal().isAuthenticate()) {
            startActivity(new Intent(this, RoomsActivity.class));
            finish();
            return;
        }
        nameField = (EditText) findViewById(R.id.name);
        emailField = (EditText) findViewById(email);
        userKeyField = (EditText) findViewById(R.id.key);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public void setupUser(View view) {
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String key = userKeyField.getText().toString();
        if (name.isEmpty()) {
            nameField.setError("Please insert your name!");
            nameField.requestFocus();
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please insert a valid email!");
            emailField.requestFocus();
        } else if (key.isEmpty()) {
            userKeyField.setError("Please insert your user key!");
            userKeyField.requestFocus();
        } else {
            auth(email, name, key);
        }
    }

    private void auth(String email, String name, String key) {
        progressBar.setVisibility(View.VISIBLE);
        AuthenticateWithKey authenticate = useCaseFactory.authenticateWithKey();
        authenticate.execute(new AuthenticateWithKey.Params(email, key, name), new Action<Account>() {
            @Override
            public void call(Account account) {
                startActivity(new Intent(MainActivity.this, RoomsActivity.class));
                progressBar.setVisibility(View.GONE);
                finish();
            }
        }, new Action<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable instanceof HttpException) { //Error response from server
                    HttpException e = (HttpException) throwable;
                    try {
                        String errorMessage = e.response().errorBody().string();
                        Log.e(TAG, errorMessage);
                        showError(errorMessage);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else if (throwable instanceof IOException) { //Error from network
                    showError("Can not connect to qiscus server!");
                } else { //Unknown error
                    showError("Unexpected error!");
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
