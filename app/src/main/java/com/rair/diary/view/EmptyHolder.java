package com.rair.diary.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rair.diary.R;

public class EmptyHolder extends RecyclerView.ViewHolder {

    public EmptyHolder(ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_one, parent, false));
    }

    private EmptyHolder(View itemView) {
        super(itemView);
    }
}