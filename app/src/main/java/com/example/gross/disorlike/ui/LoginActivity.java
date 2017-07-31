package com.example.gross.disorlike.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gross.disorlike.R;
import com.example.gross.disorlike.controller.RestManager;
import com.example.gross.disorlike.model.LoginResponseGson.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    public static final String REDDIT_SESSION = "Session";
    public static final String USERNAME = "Username";
    public static final String MODHASH = "Modhash";
    public static final String COOKIE = "Cookie";

    private SharedPreferences preferences;

    @BindView(R.id.input_username)
    EditText username;
    @BindView(R.id.input_password)
    EditText password;
    @BindView(R.id.progres_login)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);
        preferences = getSharedPreferences(REDDIT_SESSION,MODE_PRIVATE);
        if (preferences.contains(MODHASH)){
            if(!preferences.getString(MODHASH,"").equals("")){
                Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
                i.putExtra("Username", preferences.getString(USERNAME,""));
                startActivity(i);
            }
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {

        final String user = username.getText().toString(), pass = password.getText().toString();

        if (!user.equals("") && !pass.equals("")){
            progressBar.setVisibility(View.VISIBLE);
            RestManager restManager = new RestManager();
            Call<LoginResponse> call = restManager.getApiService().signIn(user, user, pass, "json");
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    try {
                        String modhash = response.body().getJson().getData().getModhash();
                        String cookie = response.body().getJson().getData().getCookie();
                        if (!modhash.equals("")){

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(USERNAME, user);
                            editor.putString(MODHASH, modhash);
                            editor.putString(COOKIE, cookie);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getApplicationContext(), OverviewActivity.class);
                            startActivity(i);
                        }
                    }catch(NullPointerException e){
                        Log.e(TAG, "onResponse: NullPointerException" + e.getMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(LoginActivity.this, "An Error Call", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            Toast.makeText(this, "Wrong Input", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {}

}
