package com.rair.diary.ui.diary.detail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rair.diary.R;
import com.rair.diary.bean.Diary;
import com.rair.diary.constant.Constants;
import com.rair.diary.db.DiaryDao;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.DrawImage;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.view.LinedEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DiaryDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_iv_back)
    ImageView detailIvBack;
    @BindView(R.id.detail_iv_options)
    ImageView detailIvOptions;
    @BindView(R.id.detail_et_title)
    EditText detailEtTitle;
    @BindView(R.id.detail_et_content)
    LinedEditText detailEtContent;
    @BindView(R.id.detail_fab_edit)
    FloatingActionButton detailFabEdit;
    @BindView(R.id.detail_fab_delete)
    FloatingActionButton detailFabDelete;
    @BindView(R.id.detail_ll_option)
    LinearLayout detailLlOption;
    @BindView(R.id.detail_iv_show)
    ImageView detailIvShow;
    @BindView(R.id.detail_tv_tite)
    TextView detailTvTite;
    private Unbinder bind;
    private  Diary editedDiary;
    private DiaryDao diaryDao;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        diaryDao = new DiaryDao(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String image = intent.getStringExtra("image");
        String date = intent.getStringExtra("date");
        String week = intent.getStringExtra("week");
        String weather = intent.getStringExtra("weather");
        boolean hasAuth = intent.getBooleanExtra("hasAuth", false);
        id = intent.getLongExtra("id", 0);
        detailEtTitle.setText(title);
        detailEtContent.setText(content);
        detailTvTite.setText(String.format(Constants.FORMAT, date, week, weather));
        if (hasAuth) {
            detailIvOptions.setVisibility(View.VISIBLE);
        } else {
            detailIvOptions.setVisibility(View.GONE);
        }
        if (image != null || !image.equals("") || !TextUtils.isEmpty(image)) {
            Glide.with(this)
                    .load(image)
                    .into(detailIvShow);
            detailIvShow.setVisibility(View.VISIBLE);
        }else{
            detailIvShow.setVisibility(View.GONE);
        }
    }
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            DrawImage.viewSaveToImage(detailEtContent);
        }
    };

    public void onOptionClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle(R.string.option);
        builder.setItems(new String[]{"编辑", "保存"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (which == 0) {
                            detailEtTitle.setEnabled(true);
                            detailEtContent.setEnabled(true);
                        } else if (which == 1) {
                            doSave();
                        }
                    }
                });
        builder.show();
    }

    @OnClick({R.id.detail_iv_back, R.id.detail_iv_options,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail_iv_back:
                this.finish();
                break;
            case R.id.detail_iv_options:
                onOptionClicked();
                break;
        }
    }


    /**
     * 保存
     */
    private void doSave() {
        String title = detailEtTitle.getText().toString().trim();
        String content = detailEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            title = "无题";
        }
        if (TextUtils.isEmpty(content)) {
//            CommonUtils.showSnackar(detailEtContent, "还没写东西");
            Toast.makeText(this, "内容不能为空哦，写点东西吧~", Toast.LENGTH_SHORT).show();
        } else {
            editedDiary = new Diary();
            editedDiary.setContent(content);
            editedDiary.setId((int) id);
            editedDiary.setTitle(title);
            Gson gson = new Gson();
            String diaryJson = gson.toJson(editedDiary);
            saveEditDiaryMethod(diaryJson);

        }
    }
    public  void setIntentResult(Diary diary) {
        Intent intent = new Intent();
        intent.putExtra("diary",new Gson().toJson(diary));
        this.setResult(Constants.DETAIL_DIARY_EDITED_SUCCESS,intent);
    }
    private void formatString2Res(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        String status = jsonObject.get("status").toString();
        if (status.equals("0")) {
            this.setIntentResult(editedDiary);
                Toast.makeText(this, "编辑内容保存成功", Toast.LENGTH_SHORT).show();
                CommonUtils.hideInput(this);
            this.finish();
            } else {
            Toast.makeText(this, "编辑内容保存失败", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void saveEditDiaryMethod(String postData) {
        String RequestURL = "http://119.29.235.55:8000/api/v1/articles";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.PutHttp(RequestURL, postData);
                System.out.println("CONTENT=========" + s);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    formatString2Res(s);
                } else {
//                    CommonUtils.showSnackar(loginTvLogin, "登陆失败");
                }
            }
        }.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
