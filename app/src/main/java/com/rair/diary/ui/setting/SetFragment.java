package com.rair.diary.ui.setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rair.diary.R;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.User;
import com.rair.diary.ui.setting.export.ExportActivity;
import com.rair.diary.ui.setting.recover.RecoverActivity;
import com.rair.diary.ui.setting.remind.NotifyActivity;
import com.rair.diary.ui.setting.secret.SecretActivity;
import com.rair.diary.ui.setting.user.LoginActivity;
import com.rair.diary.ui.setting.user.UserActivity;
import com.rair.diary.utils.SPUtils;
import com.rair.diary.view.CircleImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {

    @BindView(R.id.set_civ_head)
    CircleImageView setCivHead;
    @BindView(R.id.set_tv_name)
    TextView setTvName;
    @BindView(R.id.set_ll_user)
    LinearLayout setLlUser;
    @BindView(R.id.set_ll_notify)
    LinearLayout setLlNotify;
    //    @BindView(R.id.set_ll_recover)
//    LinearLayout setLlRecover;
    @BindView(R.id.set_ll_secret)
    LinearLayout setLlSecret;
    @BindView(R.id.set_ll_export)
    LinearLayout setLlExport;
    LinearLayout setLlFeedback;
    Unbinder unbinder;
    private SPUtils spUtils;

    public static SetFragment newInstance() {
        SetFragment setFragment = new SetFragment();
        return setFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        spUtils = RairApp.getRairApp().getSpUtils();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasLogin = spUtils.getBoolean("hasLogin", false);
//        MobclickAgent.onPageStart("SetScreen_set_list"); //统计页面("MainScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(getContext());          //统计时长
        if (hasLogin) {
            System.out.println("USERNAME=====" + spUtils.getString("username"));
            setTvName.setText(spUtils.getString("current_username"));
            setCivHead.setVisibility(View.VISIBLE);
//            loadHead();
        } else {
            setTvName.setText("未登录");
            setCivHead.setVisibility(View.GONE);
        }
    }

    //    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("SetScreen_set_list");
//        MobclickAgent.onPause(getContext());
//    }
    private void loadHead() {
        Picasso.with(getContext()).load(R.mipmap.ic_head).into(setCivHead);
    }

    @OnClick({R.id.set_ll_user, R.id.set_ll_notify, R.id.set_ll_secret, R.id.set_ll_export})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_ll_user:
                if (!spUtils.getBoolean("hasLogin", false)) {
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent userIntent = new Intent(getContext(), UserActivity.class);
                    startActivity(userIntent);
                }
                break;
            case R.id.set_ll_notify:
                Intent notifyIntent = new Intent(getContext(), NotifyActivity.class);
                startActivity(notifyIntent);
                break;
            case R.id.set_ll_secret:
                Intent secretIntent = new Intent(getContext(), SecretActivity.class);
                startActivity(secretIntent);
                break;
            case R.id.set_ll_export:
                if (spUtils.getBoolean("hasLogin", false)) {
                    Intent exportIntent = new Intent(getContext(), ExportActivity.class);
                    startActivity(exportIntent);
                } else {
                    Toast.makeText(getContext(), "请先登录~", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
