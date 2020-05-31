package com.mandeep.profileui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.profileui.R;

import java.util.ArrayList;

public class HashTagAdapter extends RecyclerView.Adapter<HashTagAdapter.ImageViewHolder> {

    ArrayList<String> hashTagList;

    public HashTagAdapter(ArrayList<String> hashTagList) {
        this.hashTagList = hashTagList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hash_tag_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.textView.setText(hashTagList.get(position));
        if (holder.getAdapterPosition() == 0) {
            holder.textView.setTextColor(holder.context.getResources().getColor(R.color.pink));
        } else {
            holder.textView.setTextColor(holder.context.getResources().getColor(R.color.gray));
        }


    }

    @Override
    public int getItemCount() {
        return hashTagList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        Context context;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.hashTagTv);
            this.context = itemView.getContext();
        }

    }

}
