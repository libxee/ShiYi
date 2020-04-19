package com.rair.diary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rair.diary.R;
import com.rair.diary.bean.DiaryBean;
import com.rair.diary.constant.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class DiaryRvAdapter extends RecyclerView.Adapter<DiaryRvAdapter.DiaryHolder> {

    private Context context;
    private ArrayList<DiaryBean> datas;
    private OnRvItemClickListener onRvItemClickListener;

    public interface OnRvItemClickListener {
        void OnItemClick(int position, DiaryBean diaryBean);
        void OnOptionClick(int position, DiaryBean diaryBean);
    }

    public void setOnRvItemClickListener(OnRvItemClickListener onRvItemClickListener) {
        this.onRvItemClickListener = onRvItemClickListener;
    }

    public DiaryRvAdapter(Context context, ArrayList<DiaryBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_diary_item, parent, false);
        return new DiaryHolder(view);
    }

    @Override
    public void onBindViewHolder(DiaryHolder holder, int position) {
//        setColor(holder);
        DiaryBean diaryBean = datas.get(position);
        holder.tvTitle.setText(diaryBean.getTitle());
        holder.tvContent.setText(diaryBean.getContent());
        String image = diaryBean.getImage();
        if (!image.equals("")) {
            Glide.with(context)
                    .load(image)
                    .into(holder.ivShow);
            holder.ivShow.setVisibility(View.VISIBLE);
        }else{
            holder.ivShow.setVisibility(View.GONE);
        }
        holder.tvDate.setText(String.format(Constants.FORMAT, diaryBean.getDate(), diaryBean.getWeek(), diaryBean.getWeather()));
        final int mPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRvItemClickListener != null)
                    onRvItemClickListener.OnItemClick(mPosition, diaryBean);
            }
        });
        holder.ivOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRvItemClickListener != null)
                    onRvItemClickListener.OnOptionClick(mPosition, diaryBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class DiaryHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvContent, tvDate;
        private View viewColor;
        private ImageView ivOption, ivShow;

        DiaryHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.diary_item_tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.diary_item_tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.diary_item_tv_date);
            ivOption = (ImageView) itemView.findViewById(R.id.diary_item_iv_option);
            ivShow = (ImageView) itemView.findViewById(R.id.diary_item_iv_show);
        }
    }
}
