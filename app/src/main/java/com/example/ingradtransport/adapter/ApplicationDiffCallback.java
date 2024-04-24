package com.example.ingradtransport.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.ingradtransport.model.Application;

public class ApplicationDiffCallback extends DiffUtil.ItemCallback<Application>{

    @Override
    public boolean areItemsTheSame(@NonNull Application oldItem, @NonNull Application newItem) {
        // Предполагается, что у каждой заявки есть уникальный идентификатор
        return oldItem.getId() == newItem.getId();
    }

    @SuppressLint("DiffUtilEquals")
    @Override
    public boolean areContentsTheSame(@NonNull Application oldItem, @NonNull Application newItem) {
        // Сравниваем содержимое заявки
        return oldItem.equals(newItem);
    }

}
