package com.example.gross.disorlike.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gross.disorlike.R;
import com.example.gross.disorlike.controller.EndlessScrollListener;
import com.example.gross.disorlike.controller.RedditAdapter;
import com.example.gross.disorlike.controller.RestManager;
import com.example.gross.disorlike.model.RedditGson;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gross.disorlike.ui.LoginActivity.MODHASH;
import static com.example.gross.disorlike.ui.LoginActivity.REDDIT_SESSION;
import static com.example.gross.disorlike.ui.LoginActivity.USERNAME;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = "OverviewActivity";

    @BindView(R.id.recViewReddit)
    RecyclerView recViewReddit;
    private Intent fromLoginIntent;
    private RedditAdapter redditAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fromLoginIntent = getIntent();
        toolbar.setSubtitle("Logged, as " + fromLoginIntent.getStringExtra("Username"));

        redditAdapter = new RedditAdapter(getApplicationContext());
        recViewReddit.setHasFixedSize(true);
        recViewReddit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recViewReddit.setAdapter(redditAdapter);

        recViewReddit.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recViewReddit.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadList();

            }
        });

        loadList();

    }


    private void loadList() {

        RestManager restManager = new RestManager();
        Call<RedditGson> call = restManager.getApiService().getAwwJson(redditAdapter.getlastLoadedItem());
        call.enqueue(new Callback<RedditGson>() {
            @Override
            public void onResponse(@NonNull Call<RedditGson> call, @NonNull Response<RedditGson> response) {

                try{
                    for (int i = 0; i < response.body().getData().getChildren().size(); i++) {
                        redditAdapter.addItem(response.body().getData().getChildren().get(i).getData());
                    }
                    redditAdapter.setlastLoadedItem(response.body().getData().getAfter());
                    redditAdapter.notifyDataSetChanged();
                }catch (NullPointerException e){
                    Log.e(TAG, "onResponse: NullPointerException" + e.getMessage());
                }


            }

            @Override
            public void onFailure(@NonNull Call<RedditGson> call, @NonNull Throwable t) {
                Toast.makeText(OverviewActivity.this, "An Error Call", Toast.LENGTH_LONG).show();
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
        if (item.getItemId() == R.id.menu_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("You logged as " + fromLoginIntent.getStringExtra("Username") + ". Do you really wont to logout?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences(REDDIT_SESSION,MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(USERNAME, "");
                            editor.putString(MODHASH, "");
                            editor.apply();

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}
