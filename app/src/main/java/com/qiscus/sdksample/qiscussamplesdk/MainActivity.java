package com.qiscus.sdksample.qiscussamplesdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.data.model.QiscusAccount;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Qiscus.with("e3@qiscus.com", "password")
                .login(new Qiscus.LoginListener() {
                    @Override
                    public void onSuccess(QiscusAccount qiscusAccount) {


                    }
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public void goToChat(View v){
        Qiscus.getChatConfig()
                .setStatusBarColor(android.R.color.holo_green_dark)
                .setAppBarColor(android.R.color.holo_green_dark)
                .setTitleColor(android.R.color.white)
                .setLeftBubbleColor(android.R.color.holo_green_dark)
                .setRightBubbleColor(android.R.color.holo_blue_dark)
                .setRightBubbleTextColor(android.R.color.white)
                .setRightBubbleTimeColor(android.R.color.white);

        Qiscus.buildChatWith("e2@qiscus.com")
                .withTitle("Jhon Doe")
                .build(MainActivity.this, new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivity(intent);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }
}
