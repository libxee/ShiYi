package com.rair.diary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rair.diary.R;
import com.rair.diary.bean.Comment;
import com.rair.diary.bean.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRvAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private Context context;

    public CommentRvAdapter(Context context, int layoutResId, @Nullable List<Comment> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        User user = item.getUser();
        ImageView sexView = helper.getView(R.id.comment_item_iv_sex);


        helper.setText(R.id.comment_item_tv_name, user.getNickName());
        helper.setText(R.id.comment_item_tv_time, item.getCommentTime());
        helper.setText(R.id.comment_item_tv_content, item.getContent());
    }

}
