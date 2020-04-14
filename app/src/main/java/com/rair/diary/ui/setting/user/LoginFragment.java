package com.rair.diary.ui.setting.user;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rair.diary.R;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.User;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.SPUtils;
import com.rair.diary.view.EditTextWithDel;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.login_et_name)
    EditTextWithDel loginEtName;
    @BindView(R.id.login_et_pwd)
    EditTextWithDel loginEtPwd;
    @BindView(R.id.login_tv_login)
    TextView loginTvLogin;
    @BindView(R.id.login_tv_forget)
    TextView loginTvForget;
    Unbinder unbinder;
    private SPUtils spUtils;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        spUtils = RairApp.getRairApp().getSpUtils();
    }

    @OnClick({R.id.login_tv_login, R.id.login_tv_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_login:
                doLogin();
                break;
            case R.id.login_tv_forget:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void doLogin() {
        String userName = loginEtName.getText().toString();
        String userPwd = loginEtPwd.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            CommonUtils.showSnackar(loginTvLogin, "请输入用户名/邮箱");
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            CommonUtils.showSnackar(loginTvLogin, "请输入密码");
            return;
        }
        User user = new User();
        user.setUsername(userName);
        user.setNickName(userName);
        user.setPassword(userPwd);
        Gson gson = new Gson();
        String userJSON = gson.toJson(user);
        loginHttpMethod(userJSON);
    }

    @SuppressLint("StaticFieldLeak")
    private void loginHttpMethod(String postData) {
        String RequestURL = "http://119.29.235.55:8000/auth/login";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.PostHttp(RequestURL, postData);
                System.out.println("CONTENT=========" + s);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
//                    TODO 确定本地存储登录信息逻辑
                    CommonUtils.showSnackar(loginTvLogin, "登陆成功");
                    getActivity().finish();
                } else {
                    CommonUtils.showSnackar(loginTvLogin, "登陆失败");
                }
            }
        }.execute();
    }
    private HashMap<String, String> formatString2User(String str){

        HashMap<String, String > infoMap = new HashMap<String, String>(){{
            put("status", String.valueOf(0));
            put("token", "tokeen");
        }};
        return infoMap;
    }
    private  void loginSuccess(String token, User user){
        spUtils = RairApp.getRairApp().getSpUtils();
        spUtils.put("hasLogin",true);
        spUtils.put("current_token","hiiamtoken");
        spUtils.put("current_username", "user");
        spUtils.put("current_password","password");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
