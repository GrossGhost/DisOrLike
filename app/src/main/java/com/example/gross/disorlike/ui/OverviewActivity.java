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
import com.example.gross.disorlike.model.SubredditResponseGson.SubredditResponse;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.gross.disorlike.ui.LoginActivity.COOKIE;
import static com.example.gross.disorlike.ui.LoginActivity.MODHASH;
import static com.example.gross.disorlike.ui.LoginActivity.REDDIT_SESSION;
import static com.example.gross.disorlike.ui.LoginActivity.USERNAME;

public class OverviewActivity extends AppCompatActivity {

    private static final String TAG = "OverviewActivity";
    private static final String AWW_LIST = "Subreddit AWW";
    private static final String APPROVED_LIST = "Approved List";
    private static final String DISAPPROVED_LIST = "Disapproved List";

    @BindView(R.id.recViewReddit)
    RecyclerView recViewReddit;

    private RedditAdapter redditAdapter;
    private String currentUsername = "";
    private SharedPreferences preferences;
    private RestManager restManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(AWW_LIST);
        setSupportActionBar(toolbar);


        preferences = getSharedPreferences(REDDIT_SESSION,MODE_PRIVATE);

        if (preferences.contains(USERNAME)) {
            currentUsername = preferences.getString(USERNAME,"");
            }
        toolbar.setSubtitle("Logged, as " + currentUsername);

        restManager = new RestManager();
        redditAdapter = new RedditAdapter(getApplicationContext());
        recViewReddit.setHasFixedSize(true);
        recViewReddit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recViewReddit.setAdapter(redditAdapter);

        recViewReddit.addOnScrollListener(new EndlessScrollListener((LinearLayoutManager) recViewReddit.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (redditAdapter.getNameOfNextItem() != null) {
                    loadList();
                }
            }
        });
        loadList();
    }

    private void loadList() {
        String modhash = "";
        String cookie = "";
        if (preferences.contains(MODHASH)){
            modhash = preferences.getString(MODHASH,"");
            cookie = preferences.getString(COOKIE,"");
        }
        HashMap<String,String> headerMap = new HashMap<>();
        headerMap.put("X-Modhash", modhash);
        headerMap.put("User-Agent", currentUsername);
        headerMap.put("cookie", "reddit_session=" + cookie);

        Call<SubredditResponse> call = restManager.getApiService().getAwwJson(headerMap, redditAdapter.getNameOfNextItem());
        if (getSupportActionBar() != null) {
            switch (getSupportActionBar().getTitle()+"") {
                case APPROVED_LIST :
                    call = restManager.getApiService().getUpvotedJson(
                            headerMap,
                            currentUsername,
                            redditAdapter.getNameOfNextItem()
                    );
                    break;
                case DISAPPROVED_LIST :
                    call = restManager.getApiService().getDownvotedJson(
                            headerMap,
                            currentUsername,
                            redditAdapter.getNameOfNextItem()
                    );
                    break;
            }
        }

        call.enqueue(new Callback<SubredditResponse>() {
            @Override
            public void onResponse(@NonNull Call<SubredditResponse> call, @NonNull Response<SubredditResponse> response) {

                try{
                    for (int i = 0; i < response.body().getData().getChildren().size(); i++) {
                        redditAdapter.addItem(response.body().getData().getChildren().get(i).getData());
                    }
                    redditAdapter.setNameOfNextItem(response.body().getData().getAfter());
                    redditAdapter.notifyDataSetChanged();
                }catch (NullPointerException e){
                    Log.e(TAG, "onResponse: NullPointerException" + e.getMessage());
                }

            }

            @Override
            public void onFailure(@NonNull Call<SubredditResponse> call, @NonNull Throwable t) {
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getSupportActionBar() != null) {
            switch (getSupportActionBar().getTitle()+"") {
                case AWW_LIST :
                    menu.findItem(R.id.menu_show_aww).setVisible(false);
                    menu.findItem(R.id.menu_aprove_list).setVisible(true);
                    menu.findItem(R.id.menu_disprove_list).setVisible(true);
                    break;
                case APPROVED_LIST :
                    menu.findItem(R.id.menu_show_aww).setVisible(true);
                    menu.findItem(R.id.menu_aprove_list).setVisible(false);
                    menu.findItem(R.id.menu_disprove_list).setVisible(true);
                    break;
                case DISAPPROVED_LIST :
                    menu.findItem(R.id.menu_show_aww).setVisible(true);
                    menu.findItem(R.id.menu_aprove_list).setVisible(true);
                    menu.findItem(R.id.menu_disprove_list).setVisible(false);
                    break;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.menu_show_aww :
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(AWW_LIST);
                }
                break;
            case R.id.menu_aprove_list :
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(APPROVED_LIST);
                }
                break;
            case R.id.menu_disprove_list :
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(DISAPPROVED_LIST);
                }
                break;
            case R.id.menu_logout :
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("You logged as " + currentUsername + ". Do you really wont to logout?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(USERNAME, "");
                                editor.putString(MODHASH, "");
                                editor.apply();

                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            }
                        }).create().show();
                break;
        }

        if (id == R.id.menu_show_aww || id == R.id.menu_aprove_list || id == R.id.menu_disprove_list){
            redditAdapter = new RedditAdapter(getApplicationContext());
            recViewReddit.setAdapter(redditAdapter);
            loadList();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}
