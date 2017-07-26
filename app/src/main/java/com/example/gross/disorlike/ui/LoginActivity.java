package com.example.gross.disorlike.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gross.disorlike.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    static final String PASSWORD = "light";

    @BindView(R.id.editTextUsername)
    EditText username;
    @BindView(R.id.editTextPassword)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick() {

        String currentUser = username.getText().toString(), pass = password.getText().toString();

        if (pass.equals(PASSWORD)) {
            if (!currentUser.equals("")) {
                Toast.makeText(this, "Hello, " + currentUser, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
                i.putExtra("Username", currentUser);
                startActivity(i);
            } else {
                Toast.makeText(this, "Empty username" + currentUser, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {}

}
