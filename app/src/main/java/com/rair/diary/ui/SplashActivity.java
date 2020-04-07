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
                "获得幸福的秘密，便是与时间坦然相处。--三浦紫苑《假如岁月足够长》",
                "我疯狂收集每一个快乐的瞬间，用他们回击每一个糟糕的日子。 by 珍妮·罗森",
                "美，多少要包含一点偶然。 from 汪曾祺《人间草木》",
                "星河在上，波光在下\\n我在你身边，\\n等着你的回答。 \\n from 张寒寺《不正常人类症候群》",
                "为了不忘昨天，为了憧憬明天，应该写日记。",
                "人生有许多事情，正如船后的波纹，总要过后才觉得美的。 by 余光中",
                "我敞开心扉向它（日记）倾诉，请它为我挽住时间的足迹。",
                "应是天仙狂醉，乱把白云揉碎。 from 李白《清平乐·画堂晨起》",
                "我并不期待人生可以过得很顺利，但我希望碰到人生难关的时候，自己可以是它的对手。 by 加缪"
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
