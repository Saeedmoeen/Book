package com.example.book.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.book.Adapter.RecyclerViewAdapter;
import com.example.book.Model.ShowPostModel.Post;
import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started");

        progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        getPost();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("create new post").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(Home.this, CreatePost.class ));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getPost() {
        Log.d(TAG, "getPost: started");
        SharedPreferences preferences = getSharedPreferences("Get_Token", MODE_PRIVATE);
        final String token = preferences.getString("Token", null);

        Call<List<Post>> call = RetrofitClient.getmInstance().getApi().getItem("token " + token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: started retrofit");
                try {
                    generatePostList(response.body());
                } catch (Exception e) {
                    Toast.makeText(Home.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(Home.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePostList(List<Post> postList) {
        Log.d(TAG, "genratePostLis: started");
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerViewAdapter(this, postList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Home.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
