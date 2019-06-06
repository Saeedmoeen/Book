package com.example.book.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Model.ShowPostModel.Post;
import com.example.book.R;
import com.example.book.Retrofit.RetrofitClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePost extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreatePost";
    private static final int STORAGE_PERMISSION_CODE = 24;
    private static final int PICK_IMAGE_REQUEST = 1;


    private EditText post_title, post_category, post_price, post_address, post_description, post_phone;

    private RelativeLayout submit, browseFile;

    private TextView select;

    private ImageView imageView;

    private Uri filePath;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Log.d(TAG, "onCreate: started");

        requestStoragePermission();

        post_title = findViewById(R.id.post_title);
        post_description = findViewById(R.id.post_description);
        post_phone = findViewById(R.id.post_phone);
        post_price = findViewById(R.id.post_price);
        post_category = findViewById(R.id.post_category);
        post_address = findViewById(R.id.post_address);

        imageView = findViewById(R.id.imageView);

        submit = findViewById(R.id.submit);
        browseFile = findViewById(R.id.layout_browsefile);

        submit.setOnClickListener(this);
        browseFile.setOnClickListener(this);

    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions not Granted", Toast.LENGTH_SHORT).show();
            }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
        }
    }

    private String getPath(Uri uri) {
        Log.d(TAG, "getPath: started");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ?",
                new String[]{document_id},
                null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadPost() {
        Log.d(TAG, "uploadPost: started");

        String path = getPath(filePath);
        File originalFile  = FileUtils.getFile(path);

        RequestBody Title = RequestBody.create(MultipartBody.FORM, post_title.getText().toString());
        RequestBody Description = RequestBody.create(MultipartBody.FORM, post_description.getText().toString());
        RequestBody Phone = RequestBody.create(MultipartBody.FORM, post_phone.getText().toString());
        RequestBody Address = RequestBody.create(MultipartBody.FORM, post_address.getText().toString());
        RequestBody Price = RequestBody.create(MultipartBody.FORM, post_price.getText().toString());
        RequestBody Category = RequestBody.create(MultipartBody.FORM, post_category.getText().toString());

        RequestBody filePart = (RequestBody) RequestBody.create(MediaType.parse(getContentResolver().getType(filePath)),
                originalFile);
        MultipartBody.Part file = MultipartBody.Part.createFormData("images/", originalFile.getName(), filePart);

        SharedPreferences preferences = getSharedPreferences("Get_Token", MODE_PRIVATE);
        final String token = preferences.getString("Token", null);

        Call<Post> call = RetrofitClient.getmInstance().getApi().getCreatePost("token "+token,
                Title, Description , Phone, Price, Category, file, Address );
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d(TAG, "onResponse: started");
                Toast.makeText(CreatePost.this, "ok", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(CreatePost.this, "nokey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == browseFile) {
            showFileChooser();
        }

        if (v == submit) {
            uploadPost();
        }
    }
}
