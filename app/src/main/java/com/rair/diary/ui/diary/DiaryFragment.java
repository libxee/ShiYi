package com.rair.diary.ui.diary;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rair.diary.R;
import com.rair.diary.adapter.DiaryRvAdapter;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.Diary;
import com.rair.diary.bean.DiaryBean;
import com.rair.diary.bean.User;
import com.rair.diary.constant.Constants;
import com.rair.diary.db.DiaryDao;
import com.rair.diary.ui.diary.add.AddDiaryActivity;
import com.rair.diary.ui.diary.detail.DiaryDetailActivity;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.SPUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class DiaryFragment extends Fragment implements TextWatcher, DiaryRvAdapter.OnRvItemClickListener {

    @BindView(R.id.diary_et_search)
    EditText diaryEtSearch;
    @BindView(R.id.diary_xrv_list)
    RecyclerView diaryXRvList;
    @BindView(R.id.diary_fab_add)
    FloatingActionButton diaryFabAdd;
    @BindView(R.id.diary_iv_delete)
    ImageView diaryIvDelete;
    @BindView(R.id.diary_front_no_data)
    ImageView diaryNoData;
    Unbinder unbinder;
    private ArrayList<DiaryBean> datas;
    private DiaryDao diaryDao;
    private DiaryRvAdapter rvAdapter;
    private int currentPage;
    private SPUtils spUtils;
    private String lastId;
    private boolean hasLogin;
    private DiaryBean viewDetailDiary;

    public static DiaryFragment newInstance() {
        DiaryFragment diaryFragment = new DiaryFragment();
        return diaryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefreshLayout refreshLayout = view.findViewById(R.id.diaryRefreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage = 1;
                queryDatas();
                refreshlayout.finishRefresh(500);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (hasLogin) {
                    queryDiaryFromServer();
                    rvAdapter.notifyDataSetChanged();
                    refreshlayout.finishLoadmore(500);
                } else {
                    refreshLayout.finishLoadmoreWithNoMoreData();
                }
            }
        });
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("=========RESUME============");
        checkCoverShow();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        登录成功后,重新获取内容
        hasLogin = spUtils.getBoolean("hasLogin", false);
        checkCoverShow();
        if (!hasLogin) {
            datas.clear();
            currentPage = 1;
            rvAdapter.notifyDataSetChanged();
        } else if (hasLogin && currentPage == 1) {
            queryDatas();
        }
    }

    private void checkCoverShow() {
        hasLogin = spUtils.getBoolean("hasLogin", false);
        if (hasLogin) {
            if (datas.size() > 0) {
                diaryNoData.setVisibility(View.GONE);
            } else {
                diaryNoData.setVisibility(View.VISIBLE);
            }
        } else {
            diaryNoData.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.ADD_NEW_DIARY:
                if (resultCode == Constants.ADD_NEW_DIARY_SUCCESS) {
                    DiaryBean diary = new DiaryBean();
                    String JsonData = data.getStringExtra("diary");
                    diary = new Gson().fromJson(JsonData, DiaryBean.class);
                    datas.add(0, diary);
                    rvAdapter.notifyItemInserted(0);
                    diaryXRvList.getLayoutManager().scrollToPosition(0);
                }
                break;
            case Constants.DETAIL_DIARY:
                if (resultCode == Constants.DETAIL_DIARY_EDITED_SUCCESS) {
//                        编辑成功，需要更新列表内容
                    DiaryBean diaryEdited = new DiaryBean();
                    String JsonData = data.getStringExtra("diary");
                    diaryEdited = new Gson().fromJson(JsonData, DiaryBean.class);
                    System.out.println(diaryEdited.toString());
                    int actPosition = datas.indexOf(viewDetailDiary);
                    int diaryId = (int) diaryEdited.getId();
                    DiaryBean diaryInList = datas.get(actPosition);
                    datas.get(actPosition).setContent(diaryEdited.getContent());
                    datas.get(actPosition).setTitle(diaryEdited.getTitle());
                    rvAdapter.notifyItemChanged(actPosition);
                    System.out.println("diaryId=======" + diaryId);
                }
                break;
        }

    }

    private void initView() {
        diaryDao = new DiaryDao(getContext());
        currentPage = 1;
        lastId = "";
        spUtils = RairApp.getRairApp().getSpUtils();
        datas = new ArrayList<>();
        diaryXRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapter = new DiaryRvAdapter(getContext(), datas);
        diaryXRvList.setAdapter(rvAdapter);
        rvAdapter.setOnRvItemClickListener(this);
        diaryEtSearch.addTextChangedListener(this);
        queryDatas();
    }


    @Override
    public void OnItemClick(int position, DiaryBean diaryBean) {
        viewDetailDiary = diaryBean;
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        intent.putExtra("title", diaryBean.getTitle());
        intent.putExtra("content", diaryBean.getContent());
        intent.putExtra("image", diaryBean.getImage());
        intent.putExtra("date", diaryBean.getDate());
        intent.putExtra("week", diaryBean.getWeek());
        intent.putExtra("weather", diaryBean.getWeather());
        intent.putExtra("id", diaryBean.getId());
        intent.putExtra("hasAuth", true);
        startActivityForResult(intent, Constants.DETAIL_DIARY);
    }

    @Override
    public void OnOptionClick(final int position, DiaryBean diaryBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogStyle);
        builder.setTitle(R.string.option);
        builder.setItems(new String[]{getString(R.string.share), getString(R.string.publish),
                        getString(R.string.delete), getString(R.string.export)},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                doShare(dialog, position);
                                break;
                            case 1:
                                doPublish(diaryBean);
                                break;
                            case 2:
                                doDelete(dialog, position, diaryBean);
                                break;
                            case 3:
                                doExport(dialog, position);
                                break;

                        }

                    }
                });
        builder.show();
    }

    /**
     * 分享操作
     *
     * @param dialog
     * @param position
     */
    private void doShare(DialogInterface dialog, int position) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TITLE, datas.get(position).getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, datas.get(position).getContent());
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(Intent.createChooser(intent, getString(R.string.share)));
        dialog.dismiss();
    }

    // 发布文章到广场。
    @SuppressLint("StaticFieldLeak")
    private void publishDiary2Server(int diaryId) {
        String RequestURL = "http://119.29.235.55:8000/api/v1/publish/" + diaryId + "?state=1";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    boolean success = formatString2Res(s);
                    if (success) {
                        Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void formatDiaryList(String response) {

        JsonObject resObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = resObject.getAsJsonObject("data");
        JsonArray jsonArray = dataObject.getAsJsonArray("list");
        for (JsonElement pic : jsonArray) {
            DiaryBean diary = new Gson().fromJson(pic, new TypeToken<DiaryBean>() {
            }.getType());
            datas.add(diary);
        }
        if (jsonArray.size() > 0) {
            currentPage++;
            diaryNoData.setVisibility(View.GONE);
            long minId = datas.get(datas.size() - 1).getId();
            lastId = Long.toString(minId);
        }
        rvAdapter.notifyDataSetChanged();

    }

    @SuppressLint("StaticFieldLeak")
    private void queryDiaryFromServer() {
        if (currentPage == 1) {
            lastId = "";
        }
        String RequestURL = "http://119.29.235.55:8000/api/v1/articles?range=personal&page=" + currentPage + "&lastId=" + lastId;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    formatDiaryList(s);
                } else {
                    Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteDiary(int diaryId, int actPosition) {
        String RequestURL = "http://119.29.235.55:8000/api/v1/del_article/" + diaryId;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                System.out.println("CONTENT=========" + s);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    boolean success = formatString2Res(s);
                    if (success) {
                        datas.remove(actPosition);
                        if (actPosition >= 0) {
                            rvAdapter.notifyItemRemoved(actPosition);
                        }
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private boolean formatString2Res(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        String status = jsonObject.get("status").toString();
        if (status.equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    private void doPublish(DiaryBean diaryBean) {
        int id = (int) diaryBean.getId();
        this.publishDiary2Server(id);
    }

    /**
     * 删除操作
     */
    private void doDelete(DialogInterface dialog, final int position, DiaryBean diaryBean) {
        dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogStyle);
        builder.setMessage(getString(R.string.delete_sure));
        builder.setTitle(getString(R.string.delete));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                int actPosition = datas.indexOf(diaryBean);
                int diaryId = (int) diaryBean.getId();
                System.out.println("diaryId=======" + diaryId);
                deleteDiary(diaryId, actPosition);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 导出
     *
     * @param dialog
     * @param position
     */
    private void doExport(DialogInterface dialog, final int position) {
        dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogStyle);
        builder.setTitle(getString(R.string.export));
        builder.setMessage(getString(R.string.is_export));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String fileName = CommonUtils.getTime();
                File rairPath = RairApp.getRairApp().getRairPath();
                String path = new File(rairPath, fileName + ".txt").getAbsolutePath();
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                        CommonUtils.showSnackar(diaryXRvList, "需要读写权限");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                } else {
                    if (exportTxt(path, datas.get(position))) {
                        CommonUtils.showSnackar(diaryEtSearch, getString(R.string.save_success));
                    } else {
                        CommonUtils.showSnackar(diaryEtSearch, getString(R.string.save_failed));
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @OnClick({R.id.diary_fab_add, R.id.diary_iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.diary_fab_add:
                if (hasLogin) {
                    Intent intent = new Intent(getContext(), AddDiaryActivity.class);
                    startActivityForResult(intent, Constants.ADD_NEW_DIARY);
                } else {
                    Toast.makeText(getContext(), "请先登录~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.diary_iv_delete:
                diaryEtSearch.setText("");
                CommonUtils.hideInput(getContext());
                break;
        }
    }

    /**
     * 转成txt导出
     *
     * @param outputPdfPath 导出路径
     * @param diary         日记
     * @return 是否成功
     */
    private boolean exportTxt(String outputPdfPath, DiaryBean diary) {
        try {
            File outputFile = new File(outputPdfPath);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
            StringBuilder sb = new StringBuilder();
            sb.append(diary.getDate());
            sb.append("\t\t");
            sb.append(diary.getWeek());
            sb.append("\t\t");
            sb.append(diary.getWeather());
            sb.append("\n");
            sb.append(diary.getTitle());
            sb.append("\n");
            sb.append(diary.getContent());
            sb.append("\n");
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            outStream.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 查询数据
     */
    private void queryDatas() {
        //判断用户是否已经登录，如果已经登录，从后端接口查数据，如果未登录，从端上DB查离线化文章.
        hasLogin = spUtils.getBoolean("hasLogin", false);
        datas.clear();
        if (hasLogin) {
            queryDiaryFromServer();
        } else {
//            未登录,加载本地离线日记
//            diaryDao.query(datas);
        }
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchText = diaryEtSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchText)) {
            diaryDao.dimSearch(searchText, datas);
            rvAdapter.notifyDataSetChanged();
        } else {
            datas.clear();
            queryDatas();
            CommonUtils.hideInput(getContext());
        }
        if (s.length() > 0) {
            diaryIvDelete.setVisibility(View.VISIBLE);
        } else {
            diaryIvDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
