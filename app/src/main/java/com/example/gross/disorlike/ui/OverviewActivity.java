package com.example.gross.disorlike.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gross.disorlike.R;
import com.example.gross.disorlike.controller.RestManager;
import com.example.gross.disorlike.model.Child;
import com.example.gross.disorlike.model.RedditGson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverviewActivity extends AppCompatActivity {
    Intent i;
    private List<Child> listChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = getIntent();
        toolbar.setSubtitle("Logged, as " + i.getStringExtra("Username"));
        loadList();

    }

    private void loadList() {

        RestManager restManager = new RestManager();
        final Call<RedditGson> listCall = restManager.getApiService().getAwwJson();
        listCall.enqueue(new Callback<RedditGson>() {
            @Override
            public void onResponse(@NonNull Call<RedditGson> call, @NonNull Response<RedditGson> response) {
                listChild = response.body().getData().getChildren();
                /*for (int i = 0; i<listChild.size(); i++) {
                    Log.i("listChild", listChild.get(i).getData().getTitle());
                    Log.i("listChild", listChild.get(i).getData().getUrl());
                    Log.i("listChild", listChild.get(i).getData().getSubreddit());
                    Log.i("listChild", listChild.get(i).getData().getThumbnail());
                }*/

            }

            @Override
            public void onFailure(@NonNull Call<RedditGson> call, @NonNull Throwable t) {
                Log.d("RESPONSE","fail");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overwiew, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_logout){
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("You logged as " + i.getStringExtra("Username") + ". Do you really wont to logout?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {}
}
