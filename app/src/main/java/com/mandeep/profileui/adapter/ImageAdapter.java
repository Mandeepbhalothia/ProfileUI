package com.mandeep.profileui.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mandeep.profileui.R;
import com.mandeep.profileui.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    ArrayList<Image> imageList;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClicked(Image image);
    }

    public ImageAdapter(ArrayList<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final Image image = imageList.get(position);
        holder.setImage(image);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setNew(false);
                holder.setImage(image);
                itemClickListener.itemClicked(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView, checkedImageView, newImageView;
        Group group;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            checkedImageView = itemView.findViewById(R.id.circleIv);
            newImageView = itemView.findViewById(R.id.circleNewIv);
            group = itemView.findViewById(R.id.group);

        }

        public void setImage(Image image) {

            if (image.isNew()) {
                checkedImageView.setVisibility(View.GONE);
                group.setVisibility(View.VISIBLE);
                imageView = newImageView;
            } else {
                group.setVisibility(View.GONE);
                checkedImageView.setVisibility(View.VISIBLE);
                imageView = checkedImageView;
            }

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground);

            if (image.getImageUrl() != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.getImageUrl().compress(Bitmap.CompressFormat.PNG, 100, stream);

                Glide.with(itemView.getContext())
                        .applyDefaultRequestOptions(requestOptions)
                        .asBitmap()
                        .load(stream.toByteArray())
                        /*.load(Uri.fromFile(new File(getRealPathFromURI(image.getImageUrl()))))*/
                        .into(imageView);
            } else if (image.getUri() != null) {
                Glide.with(itemView.getContext())
                        .applyDefaultRequestOptions(requestOptions)
                        .asBitmap()
                        .load(Uri.fromFile(new File(image.getUri())))
                        .into(imageView);
            }

        }

    }

}
