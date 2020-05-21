package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://db.ygoprodeck.com/";

    private RecyclerView recyclerView;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        List<Card> cardList = getDataFromCache();

        if(cardList != null){
            showList(cardList);
        } else  {
            makeApiCall();
        }
    }

    private List<Card> getDataFromCache() {
       String jsonCard = sharedPreferences.getString(constants.KEY_CARD_LIST, null);

       if(jsonCard == null){
           return null;
       } else {
           Type listType = new TypeToken<List<Card>>(){}.getType();
           return gson.fromJson(jsonCard, listType);
       }
    }

    private void showList(List<Card> cardList) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ListAdapter(cardList);
        recyclerView.setAdapter(mAdapter);
    }


    private void makeApiCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        YugiohApi yugiohApi = retrofit.create(YugiohApi.class);

        Call<RestYugiohResponse> call = yugiohApi.getYugiohResponse();
       call.enqueue(new Callback<RestYugiohResponse>() {
           @Override
           public void onResponse(Call<RestYugiohResponse> call, Response<RestYugiohResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<Card> cardList = response.body().getData();
                    saveList(cardList);
                    showList(cardList);
                } else {
                    showError();
                }
           }

           @Override
           public void onFailure(Call<RestYugiohResponse> call, Throwable t) {
                showError();
           }
       });
    }

    private void saveList(List<Card> cardList) {
        String jsonString = gson.toJson(cardList);

        sharedPreferences
                .edit()
                .putString(constants.KEY_CARD_LIST, jsonString)
                .apply();

        Toast.makeText(getApplicationContext(), "Lsite sauvegardée", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
    }
}
