package com.example.ingradtransport.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ingradtransport.R;
import com.example.ingradtransport.model.News;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private List<News> newsList;
    private Context context;
    BottomSheetDialog dialog;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        // Предполагается, что image является именем ресурса изображения
        //int imageResId = context.getResources().getIdentifier(news.getImage(), "drawable", context.getPackageName());
        Picasso.get()
                .load(news.getImage()) // Предполагается, что getImage() возвращает URL изображения
                .placeholder(R.drawable.bg61)
                .error(R.drawable.bg511)
                .into(holder.imageView);
        holder.textView.setText(news.getHeader());
        holder.itemView.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.news_window, null);

            ImageView image_item_wind = dialogView.findViewById(R.id.image_item_wind);
            TextView news_item_header = dialogView.findViewById(R.id.news_item_header);
            TextView news_item_body = dialogView.findViewById(R.id.news_item_body);

            // Установка изображения и текста в BottomSheetDialog
            Picasso.get()
                    .load(news.getImage()) // Предполагается, что getImage() возвращает URL изображения
                    .placeholder(R.drawable.bg61)
                    .error(R.drawable.bg511)
                    .into(image_item_wind);
            news_item_header.setText(news.getHeader());
            news_item_body.setText(news.getBody());

            bottomSheetDialog.setContentView(dialogView);

            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
            textView = itemView.findViewById(R.id.header_text);
        }
    }

}
