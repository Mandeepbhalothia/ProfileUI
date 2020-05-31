package com.mandeep.profileui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandeep.profileui.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.mandeep.profileui.MainActivity.image;

public class DataActivity extends AppCompatActivity {

    Image clickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ImageView imageView = findViewById(R.id.imageViewData);
        VideoView videoView = findViewById(R.id.videoViewData);

        clickedImage = image;
        Log.d("TAG", "onCreate: " + clickedImage.toString());

        if (clickedImage.isVideo()){
            loadVideo(videoView);
        } else {
            loadImage(imageView);
        }

    }

    private void loadVideo(VideoView videoView) {
        videoView.setVisibility(View.VISIBLE);
        if (clickedImage.getUri()!=null) {
            videoView.setVideoPath(clickedImage.getUri());
            videoView.start();
            Toast.makeText(this, "Video is playing", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "We have to get uri from bitmap for playing this video", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground);

        if (clickedImage.getImageUrl() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            clickedImage.getImageUrl().compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(stream.toByteArray())
                    .into(imageView);
        } else if (clickedImage.getUri() != null) {
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(Uri.fromFile(new File(clickedImage.getUri())))
                    .into(imageView);
        }
    }
}