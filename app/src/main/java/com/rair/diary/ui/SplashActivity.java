package com.rair.diary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rair.diary.R;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_tv_tip)
    TextView splashTvTip;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String[] tips = {
                "让理想生活的样子清晰可见",
                "获得幸福的秘密，便是与时间坦然相处",
                "我疯狂收集每一个快乐的瞬间，用他们回击每一个糟糕的日子",
                "美，多少要包含一点偶然",
                "我敞开心扉向它倾诉，请它为我挽住时间的足迹",
        };
        int random = new Random().nextInt(tips.length);
        splashTvTip.setText(tips[random]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
