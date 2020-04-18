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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rair.diary.R;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.User;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;
import com.rair.diary.utils.SPUtils;
import com.rair.diary.view.EditTextWithDel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    @BindView(R.id.register_et_name)
    EditTextWithDel registerEtName;
    @BindView(R.id.register_et_email)
    EditTextWithDel registerEtEmail;
    @BindView(R.id.register_et_pwd)
    EditTextWithDel registerEtPwd;
    @BindView(R.id.register_tv_register)
    TextView registerTvRegister;
    Unbinder unbinder;
    private SPUtils spUtils;
    private User user;

    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.register_tv_register)
    public void onViewClicked() {
        doRegister();
    }

    /**
     * 注册操作
     */
    private void doRegister() {
        String userName = registerEtName.getText().toString();
        String userMail = registerEtEmail.getText().toString();
        String userPwd = registerEtPwd.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            CommonUtils.showSnackar(registerTvRegister, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(userMail)) {
            CommonUtils.showSnackar(registerTvRegister, "请输入邮箱");
            return;
        }
        if (!CommonUtils.isEmail(userMail)) {
            CommonUtils.showSnackar(registerTvRegister, "邮箱格式不正确");
            return;
        }
        if (TextUtils.isEmpty(userPwd)) {
            CommonUtils.showSnackar(registerTvRegister, "请输入密码");
            return;
        }
        user = new User();
        user.setUsername(userName);
        user.setPassword(userPwd);
        Gson gson = new Gson();
        String userJSON = gson.toJson(user);
        registerHttpMethod(userJSON);
//        user.signUp(new SaveListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                    CommonUtils.showSnackar(registerTvRegister, "注册成功，已登录");
//                    getActivity().finish();
//                } else {
//                    switch (e.getErrorCode()) {
//                        case 202:
//                            CommonUtils.showSnackar(registerTvRegister, "注册失败，用户名已经存在");
//                            break;
//                        case 203:
//                            CommonUtils.showSnackar(registerTvRegister, "注册失败，邮箱已经存在");
//                            break;
//                        default:
//                            CommonUtils.showSnackar(registerTvRegister, "注册失败");
//                            break;
//                    }
//                }
//            }
//        });
    }

    @SuppressLint("StaticFieldLeak")
    private void registerHttpMethod(String postData) {
        String RequestURL = "http://119.29.235.55:8000/auth/register";
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
                    formatString2User(s);
                } else {
                    CommonUtils.showSnackar(registerTvRegister, "注册失败");
                }
            }
        }.execute();
    }

    private void formatString2User(String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        String status = jsonObject.get("status").toString();
        if (status.equals("0")) {
            String token = jsonObject.get("data").toString();
            loginSuccess(token, user);
            System.out.println("REGISTER SUCCESS========");
            CommonUtils.showSnackar(registerTvRegister, "注册成功");
            getActivity().finish();
        } else {
            System.out.println("REGISTER FAILED========");
            CommonUtils.showSnackar(registerTvRegister, "注册失败,请重试");
        }
        System.out.println("RESPONSEE========" + status);

    }

    private void loginSuccess(String token, User user) {
        spUtils = RairApp.getRairApp().getSpUtils();
        spUtils.put("hasLogin", true);
        spUtils.put("current_token", token);
        spUtils.put("current_username", user.getUsername());
        spUtils.put("current_password", user.getPassword());
        System.out.println("HASLOGIN==========" + spUtils.getBoolean("hasLogin") + user.getUsername());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
