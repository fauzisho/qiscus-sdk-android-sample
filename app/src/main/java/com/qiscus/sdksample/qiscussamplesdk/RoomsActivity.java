package com.qiscus.sdksample.qiscussamplesdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.PatternsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qiscus.sdk.Qiscus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zetra. on 9/19/16.
 */
public class RoomsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmailAdapter adapter;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sharedPreferences = getSharedPreferences("rooms", Context.MODE_PRIVATE);

        customizeChatUi();

        adapter = new EmailAdapter(this, new ArrayList<>(getEmails()));
        adapter.setOnClickListener(new EmailAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                openChatWith(adapter.getEmails().get(position));
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.rooms, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure wants to logout?")
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Qiscus.clearUser();
                            sharedPreferences.edit().clear().apply();
                            startActivity(new Intent(RoomsActivity.this, MainActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void createNewChat(View view) {
        final EditText emailField = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        emailField.setLayoutParams(lp);
        emailField.setHint("Email");

        new AlertDialog.Builder(this)
                .setTitle("New Chat")
                .setView(emailField)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = emailField.getText().toString();
                        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailField.setError("Please insert a valid email!");
                        } else {
                            openChatWith(email);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openChatWith(final String email) {
        showLoading();
        Qiscus.buildChatWith(email)
                .build(this, new Qiscus.ChatActivityBuilderListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        saveEmail(email);
                        startActivity(intent);
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        showError(throwable.getMessage());
                        dismissLoading();
                    }
                });
    }

    private Set<String> getEmails() {
        return sharedPreferences.getStringSet("emails", new HashSet<String>());
    }

    private void saveEmail(String email) {
        Set<String> emails = getEmails();
        emails.add(email);
        sharedPreferences.edit().putStringSet("emails", emails).apply();
        updateList(email);
    }

    private void updateList(String email) {
        if (!adapter.getEmails().contains(email)) {
            adapter.getEmails().add(email);
            adapter.notifyDataSetChanged();
        }
    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissLoading() {
        progressDialog.dismiss();
    }
}
