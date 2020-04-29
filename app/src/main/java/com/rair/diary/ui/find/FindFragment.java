package com.rair.diary.ui.find;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rair.diary.R;
import com.rair.diary.adapter.FindXrvAdapter;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.DayPic;
import com.rair.diary.bean.Diary;
import com.rair.diary.bean.User;
import com.rair.diary.ui.diary.detail.DiaryDetailActivity;
import com.rair.diary.ui.setting.export.ExportActivity;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.find_rv_list)
    RecyclerView findrvList;
    @BindView(R.id.find_front_no_data)
    ImageView findNoData;
    Unbinder unbinder;
    //    @BindView(R.id.find_tv_tip)
//    TextView findTvTip;
    private ArrayList<Diary> datas;
    //当前页
    private int pageNum = 1;
    private FindXrvAdapter findXrvAdapter;
    private SPUtils spUtils;
    private boolean hasLogin;
    private String lastId;
    FindFragment findFragment;

    public static FindFragment newInstance() {
        FindFragment findFragment = new FindFragment();
        return findFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshLayout refreshLayout = view.findViewById(R.id.findefreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                datas.clear();
                pageNum = 1;
                getArticlesByPage();
                refreshlayout.finishRefresh(500);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getArticlesByPage();
                refreshlayout.finishLoadmore(500);
            }
        });
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        lastId = "";
        spUtils = RairApp.getRairApp().getSpUtils();
        datas = new ArrayList<>();
        findrvList.setLayoutManager(new LinearLayoutManager(getContext()));
        findXrvAdapter = new FindXrvAdapter(getContext(), R.layout.view_find_item, datas);
        findrvList.setAdapter(findXrvAdapter);
        findXrvAdapter.setOnItemClickListener(this);
        hasLogin = spUtils.getBoolean("hasLogin", false);
        if (hasLogin) {
            loadDiary();
        } else {
            Toast.makeText(getContext(), "登录即可查看广场发布内容~", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 加载日记
     */
    private void loadDiary() {
        getArticlesByPage();
    }

    private List<Diary> formatDiaryList(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject().getAsJsonObject("data");
        JsonArray jsonArray = jsonObject.getAsJsonArray("list");

        for (JsonElement pic : jsonArray) {
            Diary diary = new Gson().fromJson(pic, new TypeToken<Diary>() {
            }.getType());
            datas.add(diary);
        }
        int minId = datas.get(datas.size() - 1).getId();
        lastId = Integer.toString(minId);
        return datas;
    }

    @SuppressLint("StaticFieldLeak")
    private void getArticlesByPage() {
        if (pageNum == 1) {
            lastId = "";
        }
        String RequestURL = "http://119.29.235.55:8000/api/v1/articles?state=1&lastId=" + lastId;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    ArrayList<Diary> arr = (ArrayList<Diary>) formatDiaryList(s);
                    if (arr.size() > 0) {
                        findNoData.setVisibility(View.GONE);
                        pageNum++;
                    } else {
                        CommonUtils.showSnackar(findrvList, "已经到底了~");
                    }
                    findXrvAdapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showSnackar(findrvList, "something wrong");
                }
            }
        }.execute();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent diaryDetailIntent = new Intent(getContext(), DiaryDetailActivity.class);
        diaryDetailIntent.putExtra("title", datas.get(position).getTitle());
        diaryDetailIntent.putExtra("content", datas.get(position).getContent());
        diaryDetailIntent.putExtra("image", datas.get(position).getImage());
        diaryDetailIntent.putExtra("date", datas.get(position).getDate());
        diaryDetailIntent.putExtra("week", datas.get(position).getWeek());
        diaryDetailIntent.putExtra("weather", datas.get(position).getWeather());
        diaryDetailIntent.putExtra("hasAuth", false);
        startActivity(diaryDetailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkCoverShow();
    }
    private void checkCoverShow() {
        hasLogin = spUtils.getBoolean("hasLogin", false);
        System.out.println("=========FIND RESUME============");
        if (hasLogin) {
            if (datas.size() > 0) {
                findNoData.setVisibility(View.GONE);
            } else {
                findNoData.setVisibility(View.VISIBLE);
            }
        } else {
            findNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        退出登录后,清空已经展示的社区内容;
//        登录成功后,重新获取内容
        hasLogin = spUtils.getBoolean("hasLogin", false);
        checkCoverShow();
        if (!hasLogin) {
            datas.clear();
            pageNum = 1;
            findXrvAdapter.notifyDataSetChanged();
        } else if (hasLogin && pageNum == 1) {
            loadDiary();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
