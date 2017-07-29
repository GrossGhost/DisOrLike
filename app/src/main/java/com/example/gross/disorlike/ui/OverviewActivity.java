package com.example.gross.disorlike.ui;

import android.content.DialogInterface;
import android.content.Intent;
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

public class OverviewActivity extends AppCompatActivity {

    @BindView(R.id.recViewReddit)
    RecyclerView recViewReddit;
    private Intent fromLoginIntent;
    private RedditAdapter redditAdapter;

    public OverviewActivity() {
    }

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
        Call<RedditGson> listCall = restManager.getApiService().getAwwJson(redditAdapter.getlastLoadedItem());
        listCall.enqueue(new Callback<RedditGson>() {
            @Override
            public void onResponse(@NonNull Call<RedditGson> call, @NonNull Response<RedditGson> response) {

                for (int i = 0; i < response.body().getData().getChildren().size(); i++) {
                    redditAdapter.addItem(response.body().getData().getChildren().get(i).getData());
                }
                redditAdapter.setlastLoadedItem(response.body().getData().getAfter());
                redditAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<RedditGson> call, @NonNull Throwable t) {
                Log.d("RESPONSE", "fail");
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
