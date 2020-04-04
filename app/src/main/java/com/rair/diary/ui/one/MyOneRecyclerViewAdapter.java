package com.rair.diary.ui.one;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rair.diary.R;
import com.rair.diary.bean.DayPic;
import com.rair.diary.ui.one.OneFragment.OnListFragmentInteractionListener;
import com.rair.diary.ui.one.dummy.DummyContent.DummyItem;

import java.util.List;

public class MyOneRecyclerViewAdapter extends RecyclerView.Adapter<MyOneRecyclerViewAdapter.ViewHolder> {

    private final List<DayPic> DayPicList;
    private final OnListFragmentInteractionListener mListener;

    public MyOneRecyclerViewAdapter(List<DayPic> items, OnListFragmentInteractionListener listener) {
        DayPicList = items;
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
        System.out.println("+++++++++++" + DayPicList);
        holder.mIdView.setText(DayPicList.get(position).getHp_content());
        holder.mContentView.setText(DayPicList.get(position).getText_authors());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return DayPicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DayPic mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
