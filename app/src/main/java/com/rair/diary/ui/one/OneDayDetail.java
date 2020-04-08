package com.rair.diary.ui.one;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rair.diary.R;
import com.rair.diary.bean.DayPic;
import com.rair.diary.bean.OneArticle;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OneDayDetail extends AppCompatActivity {
    @BindView(R.id.one_detail_back)
    ImageView detailIvBack;
    @BindView(R.id.one_detail_title)
    TextView detailTitle;
    @BindView(R.id.one_detail_article_content)
    TextView articleContent;
    @BindView(R.id.one_detail_article_author)
    TextView articleAuthor;
    @BindView(R.id.one_detail_article_title)
    TextView articleTitle;
    @BindView(R.id.one_detail_article_nodata)
    TextView articleNoData;
    private Unbinder bind;
    private  String currentDateStr = "20200407";
    private OneArticle oneArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_detail);
        bind = ButterKnife.bind(this);
        initView();
    }
    private  void initView(){
        Intent intent = getIntent();
        currentDateStr = intent.getStringExtra("currentDate");
        detailTitle.setText("每日精选");
        System.out.println(currentDateStr);
        getArticleByDate(currentDateStr);
    }
    private OneArticle formatArticle(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        oneArticle =  new Gson().fromJson(dataObject, new TypeToken<OneArticle>(){}.getType());
        return oneArticle;
    }
    private  void  setArticleView(Boolean hasData){
        if (hasData){
            articleTitle.setText(oneArticle.getTitle());
            articleAuthor.setText(oneArticle.getAuthor());
            articleContent.setText(Html.fromHtml(oneArticle.getContent()));
        }
        else {
            articleNoData.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("StaticFieldLeak")
    private void getArticleByDate(String date){
        String dateFormatted = "20170216";
        try {
            dateFormatted = CommonUtils.formatOneArticleDate(date);
            System.out.println("dateFormatted==="+dateFormatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String RequestURL = "https://interface.meiriyiwen.com/article/day?dev=1&date=" + dateFormatted;
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
                    oneArticle = formatArticle(s);
                    setArticleView(true);
                }
                else {
                    setArticleView(false);
                }
            }
        }.execute();
    }
    @OnClick({R.id.one_detail_back,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_detail_back:
                this.finish();
                break;
            case R.id.about_iv_back:
                System.out.println("ss");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}

