package com.example.book.Api;

import android.text.Editable;

import com.example.book.Model.CategoreisModel.Category;
import com.example.book.Model.CreatePostModel.AddNewPost;
import com.example.book.Model.RegisterModel.Login;
import com.example.book.Model.ShowPostModel.Post;
import com.example.book.Model.RegisterModel.Register;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @FormUrlEncoded
    @POST("accounts/signup")
    Call<Register> createUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("accounts/signin")
    Call<Register> getLogin(@Body Login login);


    @GET("ads")
    Call<List<Post>> getItem(@Header("Authorization") String token);

    @GET("categories")
    Call<List<Category>> getCategory(@Header("Authorization") String token);

    @Multipart
    @POST("ads")
    Call<Post> getCreatePost(@Header("Authorization") String token,
                             @Part("title") RequestBody title,
                             @Part("description") RequestBody description,
                             @Part("tell") RequestBody tell,
                             @Part("price") RequestBody price,
                             @Part("category") RequestBody category,
                             @Part MultipartBody.Part image,
                             @Part("address") RequestBody address);

}
