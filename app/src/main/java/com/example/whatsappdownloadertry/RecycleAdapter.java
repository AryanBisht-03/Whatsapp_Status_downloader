package com.example.whatsappdownloadertry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappdownloadertry.databinding.RecyclerViewItemsBinding;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ItemsViewHolder> {

    Context context;
    ArrayList<ItemModel> items;

    public RecycleAdapter(Context context, ArrayList<ItemModel> items) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_items, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {


        Glide.with(context).load(items.get(position).uri).centerCrop().into(holder.binding.mainImage);
        if(items.get(position).image)
            holder.binding.playButton.setVisibility(View.INVISIBLE);
        else
            holder.binding.playButton.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aryan","Button is clicked");
                Intent intent = new Intent(context,showImageActivity.class);
                Log.d("Aryan","Values are " + items.get(position).uri);
                intent.putExtra("Data",items.get(position).uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        RecyclerViewItemsBinding binding;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerViewItemsBinding.bind(itemView);
        }

    }
}
