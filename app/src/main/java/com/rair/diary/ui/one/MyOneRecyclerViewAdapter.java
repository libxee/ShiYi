package com.rair.diary.ui.one;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rair.diary.R;
import com.rair.diary.bean.DayPic;
import com.rair.diary.ui.one.OneFragment.OnListFragmentInteractionListener;
import com.rair.diary.ui.one.dummy.DummyContent.DummyItem;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.provider.Settings.System.getString;

public class MyOneRecyclerViewAdapter extends RecyclerView.Adapter<MyOneRecyclerViewAdapter.ViewHolder> {

    private final List<DayPic> DayPicList;
    private final OnListFragmentInteractionListener mListener;
    Fragment OneListFragment;
    private OnDayPicItemClickListener onDayPicItemClickListener;

    public interface OnDayPicItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnRvItemClickListener(OnDayPicItemClickListener onDayPicItemClickListener) {
        this.onDayPicItemClickListener = onDayPicItemClickListener;
    }

    public MyOneRecyclerViewAdapter(List<DayPic> items, OnListFragmentInteractionListener listener, Fragment fragment) {
        DayPicList = items;
        OneListFragment = fragment;
        System.out.println("================" + items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_one, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = DayPicList.get(position);
        holder.OneDate.setText(DayPicList.get(position).getHp_makettime().toString());
        holder.HpTitle.setText(DayPicList.get(position).getHp_title());
        holder.DayPicAuthor.setText(DayPicList.get(position).getHp_author());
        holder.HpContent.setText(DayPicList.get(position).getHp_content());
        holder.HpContentAuthor.setText(DayPicList.get(position).getText_authors());
        final int mPosition = position;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDayPicItemClickListener != null)
                    onDayPicItemClickListener.OnItemClick(mPosition);
            }
        });
        String imgUrl = DayPicList.get(position).getHp_img_url();
        Glide.with(OneListFragment)
                .load(imgUrl)
                .into(holder.HpImg);

    }

    @SuppressLint("SimpleDateFormat")
    private String formatString(Date date) {
        DateFormat formatter;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    @Override
    public int getItemCount() {
        return DayPicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView OneDate;
        public final TextView HpTitle;
        public final TextView DayPicAuthor;
        public final TextView HpContent;
        public final TextView HpContentAuthor;
        public final ImageView HpImg;
        public DayPic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            HpTitle = (TextView) view.findViewById(R.id.one_item_hp_title);
            HpContent = (TextView) view.findViewById(R.id.one_item_hp_content);
            HpContentAuthor = (TextView) view.findViewById(R.id.one_item_hp_content_author);
            OneDate = (TextView) view.findViewById(R.id.one_item_date);
            DayPicAuthor = (TextView) view.findViewById(R.id.one_item_hp_img_author);
            HpImg = (ImageView) view.findViewById(R.id.one_item_hp_img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + HpContentAuthor.getText() + "'";
        }
    }
}
