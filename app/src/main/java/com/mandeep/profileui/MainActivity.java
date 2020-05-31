package com.mandeep.profileui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.profileui.adapter.HashTagAdapter;
import com.mandeep.profileui.adapter.ImageAdapter;
import com.mandeep.profileui.model.Image;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ImageAdapter.ItemClickListener {

    private static final int FILE_PICK = 101;
    private static final int STORAGE_PERMISSION = 102;
    RecyclerView imageRv, hashTagRv;
    ArrayList<Image> imageList = new ArrayList<>();
    public static Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        imageRv = findViewById(R.id.imageRv);
        hashTagRv = findViewById(R.id.hashTagRv);
        ImageButton cameraBtn = findViewById(R.id.cameraBtn);

        // set toolBar
        setSupportActionBar(toolbar);

        /*init recyclerView and set adapter*/
        initRecyclerViews();

        // click on camera button to select media file
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

    }

    private void pickData() {
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("*/*");
        String[] mimeTypes = {"image/*", "video/*"}; // is optional
        cameraIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(cameraIntent, FILE_PICK); // request for select file with requestCode @params FILE_PICK
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        } else {
            pickData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == STORAGE_PERMISSION) {
                pickData();
            }
        } else {
            Toast.makeText(this, "Storage Permission is required for Fetching video", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_PICK) {
                if (data != null) {

                    imageRv.setVisibility(View.VISIBLE);// make visible rv

                    Uri uri = data.getData(); // get uri of selected data

                    String fileType = checkFileType(uri); // check file that it is image or video

                    String path = getRealPathFromURI(uri); // get realPathFromUri as String to display by Glide

                    boolean isVideo = fileType.toLowerCase().contains("video") || fileType.toLowerCase().contains("mp4");
                    if (path != null) {
                        imageList.add(new Image(null, path, true, isVideo)); // set data in model class
                        Objects.requireNonNull(imageRv.getAdapter()).notifyDataSetChanged(); // notify adapter to make change in list
                    } else {
                        try {
                            final Uri imageUri = data.getData();
                            assert imageUri != null;

                            // if cursor with query is not able to give real path then try with bitmap
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            if (selectedImage != null) {
                                imageList.add(new Image(selectedImage, null, true, isVideo));
                                Objects.requireNonNull(imageRv.getAdapter()).notifyDataSetChanged();
                            }

                            if (selectedImage == null) {
                                // this may happen if we are not able to get realPath of file
                                Toast.makeText(this, "Try to pick file by different method, original path has issue",
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    private String checkFileType(Uri uri) {
        if (uri == null) {
            return "null";
        }
        ContentResolver cR = getContentResolver();
        return cR.getType(uri);
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private void initRecyclerViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        imageRv.setLayoutManager(linearLayoutManager);
        hashTagRv.setLayoutManager(linearLayoutManager2);

        ArrayList<String> hashTagList = new ArrayList<>();
        hashTagList.add("#One");
        hashTagList.add("#OneHash");
        hashTagList.add("#OneFree");
        hashTagList.add("#OneTwo");
        hashTagList.add("#OneNight");
        hashTagList.add("#OneGlass");
        hashTagList.add("#OneCoffee");
        hashTagList.add("#OneCat");

        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        HashTagAdapter hashTagAdapter = new HashTagAdapter(hashTagList);

        imageRv.setAdapter(imageAdapter);
        imageAdapter.setItemClickListener(this);
        hashTagRv.setAdapter(hashTagAdapter);

    }

    // to display menu icon in Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // any item is clicked in recyclerView
    @Override
    public void itemClicked(Image image1) {
        if (image1 != null) {
            image = image1;
            Intent intent = new Intent(MainActivity.this, DataActivity.class);
            startActivity(intent);
        }
    }
}