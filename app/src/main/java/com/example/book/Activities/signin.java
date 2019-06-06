package com.example.book.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Model.RegisterModel.Login;
import com.example.book.Model.RegisterModel.Register;
import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signin extends AppCompatActivity {

    private static final String TAG = "signin";

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

        Login login = new Login(user, pass);

        Call<Register> call = RetrofitClient.getmInstance().getApi().getLogin(login);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Log.d(TAG, "onResponse: started retrofit in page login");

                if (response.isSuccessful()) {
                    try {
                        SharedPreferences sharedPreferences = getSharedPreferences("Get_Token", MODE_PRIVATE);
                        sharedPreferences.edit().putString("Token", response.body().getToken()).apply();
                        Intent intent = new Intent(signin.this, Home.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(signin.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(signin.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(signin.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
