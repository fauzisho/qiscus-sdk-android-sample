package com.qiscus.sdksample.qiscussamplesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusAccount;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private Button startChatButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        startChatButton = (Button) findViewById(R.id.start_chat_button);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        customizeChatUi();

        if (Qiscus.hasSetupUser()) {
            info.setText(String.format("Login as %s", Qiscus.getQiscusAccount().getUsername()));
            info.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            startChatButton.setEnabled(false);
            Qiscus.setUser("e3@qiscus.com", "password")
                    .withUsername("Evan Purnama")
                    .save(new Qiscus.SetUserListener() {
                        @Override
                        public void onSuccess(QiscusAccount qiscusAccount) {
                            info.setText(String.format("Login as %s", qiscusAccount.getUsername()));
                            info.setVisibility(View.VISIBLE);
                            startChatButton.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
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

    private void customizeChatUi() {
        Qiscus.getChatConfig()
                .setStatusBarColor(android.R.color.holo_green_dark)
                .setAppBarColor(android.R.color.holo_green_dark)
                .setTitleColor(android.R.color.white)
                .setLeftBubbleColor(android.R.color.holo_green_dark)
                .setRightBubbleColor(android.R.color.holo_blue_dark)
                .setRightBubbleTextColor(android.R.color.white)
                .setRightBubbleTimeColor(android.R.color.white);
    }

    public void goToChat(View v) {
        progressBar.setVisibility(View.VISIBLE);
        Qiscus.buildChatWith("e2@qiscus.com")
                .withTitle("Jhon Doe")
                .build(this, new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
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
