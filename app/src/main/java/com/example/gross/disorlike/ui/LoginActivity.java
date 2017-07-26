package com.example.gross.disorlike.ui;

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

        String user = username.getText().toString(), pass = password.getText().toString();

        if (pass.equals(PASSWORD)) {
            if (!user.equals("")) {
                Toast.makeText(this, "Hello, " + user, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Empty username" + user, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show();
        }
    }

}
