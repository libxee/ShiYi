package com.rair.diary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rair.diary.R;
import com.rair.diary.bean.Diary;
import com.rair.diary.bean.User;
import com.rair.diary.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FindXrvAdapter extends BaseQuickAdapter<Diary, BaseViewHolder> {

    private Context context;

    public FindXrvAdapter(Context context, int layoutResId, @Nullable List<Diary> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Diary item) {
        User user = item.getUser();
//        ImageView ivSex = helper.getView(R.id.find_item_iv_sex);
//        CircleImageView civHead = helper.getView(R.id.find_item_civ_head);

        helper.setText(R.id.find_item_tv_name, item.getTitle());
        helper.setText(R.id.find_item_tv_time, item.getDate());
        helper.setText(R.id.find_item_tv_content, item.getContent());
    }

}
