package com.example.book.Api;

import com.example.book.Model.Ads;
import com.example.book.Model.Login;
import com.example.book.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("accounts/signup")
    Call<ResponseBody> createUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("accounts/signin")
    Call<User> login(@Body Login login);

    @GET("secretinfo")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken );

    @GET("ads")
    Call<Ads> getItem();
}
