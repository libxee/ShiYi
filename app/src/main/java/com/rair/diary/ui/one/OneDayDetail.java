package com.rair.diary.ui.one;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rair.diary.R;
import com.rair.diary.bean.Book;
import com.rair.diary.bean.DayPic;
import com.rair.diary.bean.Movie;
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

import java.lang.reflect.Type;
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
//    文章
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
//    书籍
    @BindView(R.id.one_detail_book_author)
    TextView bookAuthor;
    @BindView(R.id.one_detail_book_title)
    TextView bookTitle;
    @BindView(R.id.one_detail_book_commentnum)
    TextView bookCommentNum;
    @BindView(R.id.one_detail_book_press)
    TextView bookPress;
    @BindView(R.id.one_detail_book_score)
    TextView bookScore;
    @BindView(R.id.one_detail_book_quote)
    TextView bookQuote;
    @BindView(R.id.one_detail_book_cover)
    ImageView bookCover;

//   电影R.id
    @BindView(R.id.one_detail_movie_title)
    TextView movieTitle;
    @BindView(R.id.one_detail_movie_product_info)
    TextView movieProductInfo;
    @BindView(R.id.one_detail_movie_score)
    TextView movieScore;
    @BindView(R.id.one_detail_movie_type)
    TextView movieType;
    @BindView(R.id.one_detail_movie_quote)
    TextView movieQuote;
    @BindView(R.id.one_detail_movie_commentnum)
    TextView movieComment;
    @BindView(R.id.one_detail_movie_cover)
    ImageView movieCover;

    private Unbinder bind;
    private String currentDateStr = "20200407";
    private OneArticle oneArticle;
    private Movie movie;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_detail);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        currentDateStr = intent.getStringExtra("currentDate");
        detailTitle.setText("每日精选");
        getMovieById(currentDateStr);
        getBookById(currentDateStr);
        getArticleByDate(currentDateStr);
    }

    private OneArticle formatArticle(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        oneArticle = new Gson().fromJson(dataObject, new TypeToken<OneArticle>() {
        }.getType());
        return oneArticle;
    }

    private Movie formatMovie(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        return new Gson().fromJson(dataObject, new TypeToken<Movie>() {
        }.getType());
    }

    private Book formatBook(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        return new Gson().fromJson(dataObject, new TypeToken<Book>() {
        }.getType());
    }

    private void setArticleView(Boolean hasData) {
        if (hasData) {
            articleTitle.setText(oneArticle.getTitle());
            articleAuthor.setText(oneArticle.getAuthor());
            articleContent.setText(Html.fromHtml(oneArticle.getContent()));
        } else {
            articleNoData.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void  setMovieView(Boolean hasData){
        if (hasData){
            movieTitle.setText(movie.getName());
            movieScore.setText(movie.getScore() + "分");
            movieComment.setText(movie.getComment_num() + "人评价");
            movieProductInfo.setText(movie.getProduct_info());
            movieType.setText(movie.getCountry() + "   " +movie.getType());
            movieQuote.setText(movie.getQuote());
            Glide.with(this).load(movie.getCover_url()).into(movieCover);
        }
    }
    @SuppressLint("SetTextI18n")
    private void setBookView(Boolean hasData){
        if (hasData){
            bookTitle.setText(book.getName());
            bookAuthor.setText(book.getAuthor());
            bookCommentNum.setText(Integer.toString(book.getComment_num()) + "人评价");
            bookPress.setText(book.getPress());
            bookQuote.setText(book.getQuote());
            bookScore.setText(Double.toString(book.getScore()) + "分");
            Glide.with(this).load(book.getImage()).into(bookCover);

        }
    }
    private  int getIdByDate(String date){
        int curId = 1;
        try {
            curId = CommonUtils.formatDate2Id(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curId;
    }
    @SuppressLint("StaticFieldLeak")
    private void getArticleByDate(String date) {
        String dateFormatted = "20170216";
        try {
            dateFormatted = CommonUtils.formatOneArticleDate(date);
            System.out.println("dateFormatted===" + dateFormatted);
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
                } else {
                    setArticleView(false);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void getMovieById(String date) {
        int id = getIdByDate(date);
        String RequestURL = "http://119.29.235.55:8000/api/browse/movies/" + id;
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
                    movie = formatMovie(s);
                    setMovieView(true);
                } else {
                    setBookView(false);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void getBookById(String date) {
        int id = getIdByDate(date);
        String RequestURL = "http://119.29.235.55:8000/api/browse/books/" + id;
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
                    book = formatBook(s);
                    setBookView(true);
                } else {
                    setBookView(false);
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}

