package com.example.book.Activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;
import com.example.book.customfonts.MyEditText;
import com.example.book.customfonts.MyTextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signup extends AppCompatActivity {
    private static final String TAG = "signup";

    private TextView signinhere;
    private Typeface fonts1;

    private MyEditText et_username, et_email, et_password, et_c_password;
    private MyTextView sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Log.d(TAG, "onCreate: started.");

        signinhere = (TextView)findViewById(R.id.signinhere);

        signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(signup.this, signin.class);
                startActivity(it);
            }
        });

        fonts1 =  Typeface.createFromAsset(signup.this.getAssets(),
                "fonts/Lato-Regular.ttf");

        TextView t1 =(TextView)findViewById(R.id.signinhere);
        t1.setTypeface(fonts1);

        et_username = findViewById(R.id.username);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        et_c_password = findViewById(R.id.confirm_password);

        sign_up = findViewById(R.id.signup1);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });

    }

    private void userSignUp() {
        Log.d(TAG, "userSignUp: create username");

        String username = et_username.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String c_password = et_c_password.getText().toString().trim();

        ifForField(username, email, password, c_password);

        Call<ResponseBody> call = RetrofitClient.getmInstance().getApi().createUser(username, password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(signup.this, "error : " +response.message() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void ifForField(String username, String email, String password, String c_password) {
        Log.d(TAG, "ifForField: check information");
        if (username.isEmpty()) {
            Toast.makeText(this, "Enter your username.", Toast.LENGTH_SHORT).show();
            et_username.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your email.", Toast.LENGTH_SHORT).show();
            et_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Enter your password.", Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.requestFocus();
            return;
        }

        if (password.length() < 6 ) {
            Toast.makeText(this, "Password should be at least 6 character long.", Toast.LENGTH_SHORT).show();
            et_password.requestFocus();
            return;
        }

        if (!c_password.equals(password)) {
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
                et_c_password.requestFocus();
        }
    }
}
