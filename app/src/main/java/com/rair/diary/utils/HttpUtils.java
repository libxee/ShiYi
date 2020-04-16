package com.rair.diary.utils;

import com.rair.diary.base.RairApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtils {
    static SPUtils spUtils = RairApp.getRairApp().getSpUtils();
    public static String getStringByOkhttp(String path) {
        final String useAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
        String current_token= spUtils.getString("current_token", "");
        if (!current_token.equals("")){
            current_token= current_token.replace("\"", "");
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(path).addHeader("User-Agent", useAgent).addHeader("jwtToken",current_token ).build();
        System.out.println(request.toString() + request.headers().toString());
        try {
            Response response = client.newCall(request).execute();
            System.out.println("RESPONSE======:" + response.toString() + response.headers().toString());
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String PostHttp(String path,String postJSON) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String current_token= spUtils.getString("current_token", "");
        if (!current_token.equals("")){
            current_token= current_token.replace("\"", "");
        }
        //创建RequestBody对象，将参数按照指定的MediaType封装
        RequestBody requestBody = RequestBody.create(mediaType, postJSON);
        Request request = new Request
                .Builder().url(path).addHeader("jwtToken",current_token)
                .post(requestBody)//Post请求的参数传递
                .build();
        System.out.println(request.toString() + requestBody);
        try {
            Response response = client.newCall(request).execute();
            System.out.println("RESPONSE======:" + response.toString() + response.headers().toString());
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}