package com.example.book.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Api.Api;
import com.example.book.Model.Login;
import com.example.book.Model.User;
import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signin extends AppCompatActivity {

    private static final String TAG = "signin";
    private static  String token;

    TextView create, signin;
    Typeface fonts1;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        Log.d(TAG, "onCreate: started");

        create = (TextView) findViewById(R.id.create);
        signin = findViewById(R.id.signin1);

        username = findViewById(R.id.email_s);
        password = findViewById(R.id.password_s);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signin.this, signup.class);
                startActivity(it);
            }
        });

        fonts1 = Typeface.createFromAsset(signin.this.getAssets(),
                "fonts/Lato-Regular.ttf");

        TextView t4 = (TextView) findViewById(R.id.create);
        t4.setTypeface(fonts1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }

    private void userLogin() {
        Log.d(TAG, "userLogin: start login");

        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty()) {
            username.setError("Username is required !");
            username.requestFocus();
        }

        if (user.length() < 6) {
            username.setError("Username should at least 6 character long !");
            username.requestFocus();
        }

        if (pass.isEmpty()) {
            password.setError("Password required !");
            password.requestFocus();
        }

        if (pass.length() < 6) {
            password.setError("Password should at least 6 character long !");
            password.requestFocus();
        }

        Login login = new Login(user, pass);
        Call<User> call = RetrofitClient.getmInstance().getApi().login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        token =response.body().getToken();
                        startActivity(new Intent(signin.this, Home.class));
                    } else {
                        token =response.errorBody().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(signin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSecret() {
      Call<ResponseBody> call =  RetrofitClient.getmInstance().getApi().getSecret(token);

      call.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              if (response.isSuccessful()) {
              } else {
                  Toast.makeText(signin.this, response.message(), Toast.LENGTH_SHORT).show();
              }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
              Toast.makeText(signin.this, t.getMessage(), Toast.LENGTH_SHORT).show();
          }
      });
    }
}
