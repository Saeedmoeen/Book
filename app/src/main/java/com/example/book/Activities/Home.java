package com.example.book.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.book.Adapter.AdapterItem;
import com.example.book.Model.Ads;
import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";

    private ArrayList<Ads> item = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");

        initItem();

    }

    private void initItem() {
        Log.d(TAG, "initItem: init item");
        Call<Ads> call = RetrofitClient.getmInstance().getApi().getItem();

        call.enqueue(new Callback<Ads>() {
            @Override
            public void onResponse(Call<Ads> call, Response<Ads> response) {
                if (response.isSuccessful()) {
                    init();
                }
            }

            @Override
            public void onFailure(Call<Ads> call, Throwable t) {
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        Log.d(TAG, "init: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        AdapterItem adapter = new AdapterItem(this, item);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }
}
