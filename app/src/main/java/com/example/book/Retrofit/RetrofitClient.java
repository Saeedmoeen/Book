package com.example.book.Retrofit;

import android.util.Log;

import com.example.book.Api.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";

    private static final String BASE_URL = "http://kavehkm.pythonanywhere.com/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        Log.d(TAG, "RetrofitClient: started.");
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getmInstance() {
        Log.d(TAG, "getmInstance: mInstance Started.");
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi() {
        Log.d(TAG, "getApi: Api Started.");
        return retrofit.create(Api.class);
    }
}
