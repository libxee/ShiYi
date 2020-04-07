package com.rair.diary.ui.one;

import com.rair.diary.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OneDayDetail extends AppCompatActivity {
    @BindView(R.id.one_detail_back)
    ImageView detailIvBack;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_detail);
        bind = ButterKnife.bind(this);

    }

    @OnClick({R.id.one_detail_back,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one_detail_back:
                System.out.println("===mamba out");

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
