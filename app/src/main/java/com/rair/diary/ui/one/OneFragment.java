package com.rair.diary.ui.one;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rair.diary.R;
import com.rair.diary.adapter.DiaryRvAdapter;
import com.rair.diary.bean.DayPic;
import com.rair.diary.bean.DiaryBean;
import com.rair.diary.db.DiaryDao;
import com.rair.diary.ui.one.dummy.DummyContent;
import com.rair.diary.ui.one.dummy.DummyContent.DummyItem;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.RairUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OneFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<DayPic> DayPicList;
    private MyOneRecyclerViewAdapter oneRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OneFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OneFragment newInstance(int columnCount) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_list, container, false);
        DayPicList = new ArrayList<DayPic>();
        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(1000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                sendRequestWithOkHttp();
                refreshlayout.finishLoadmore(2000);
            }
        });
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            oneRecyclerViewAdapter = new MyOneRecyclerViewAdapter(DayPicList, mListener, OneFragment.this);
            recyclerView.setAdapter(oneRecyclerViewAdapter);
            this.sendRequestWithOkHttp();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private List<DayPic> formatDayPicList(String response) {
        //拿到数组
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("data");
        //循环遍历数组
        for (JsonElement pic : jsonArray) {
            DayPic dayPic = new Gson().fromJson(pic, new TypeToken<DayPic>() {
            }.getType());
            DayPicList.add(dayPic);
        }
        return DayPicList;
    }

    @SuppressLint("StaticFieldLeak")
    private void sendRequestWithOkHttp() {
        String RequestURL = "http://v3.wufazhuce.com:8000/api/hp/bymonth/2020-03";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
//                执行网络请求
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    DayPicList = formatDayPicList(s);
                    System.out.println("---------" + DayPicList);
                    oneRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        }.execute();

    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DayPic item);
    }
}
