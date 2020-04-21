package com.rair.diary.ui.setting.export;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rair.diary.R;
import com.rair.diary.base.RairApp;
import com.rair.diary.bean.DiaryBean;
import com.rair.diary.constant.Constants;
import com.rair.diary.db.DiaryDao;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.HttpUtils;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ExportActivity extends AppCompatActivity {

    @BindView(R.id.export_iv_back)
    ImageView exportIvBack;
    @BindView(R.id.export_ll_txt)
    LinearLayout exportLlTxt;
    @BindView(R.id.export_ll_json)
    LinearLayout exportJson;
    private Unbinder unbinder;
    private ArrayList<DiaryBean> diarys;
    private Handler handler;
    private File rairPath;
    private ProgressDialog progressDialog;
    private JsonArray diaryJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        unbinder = ButterKnife.bind(this);
        initData();
    }

    private void formatDiaryList(String response) {
        this.diarys.clear();
        JsonObject resObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject dataObject = resObject.getAsJsonObject("data");
        this.diaryJsonArray = dataObject.getAsJsonArray("list");
        for (JsonElement pic : diaryJsonArray) {
            DiaryBean diary = new Gson().fromJson(pic, new TypeToken<DiaryBean>() {
            }.getType());
            diarys.add(diary);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void queryDiaryFromServer() {
        String RequestURL = "http://119.29.235.55:8000/api/v1/all_articles?range=personal";
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String s = HttpUtils.getStringByOkhttp(RequestURL);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null && !s.isEmpty()) {
                    formatDiaryList(s);
                } else {
                    CommonUtils.showSnackar(exportLlTxt, "数据获取失败");
                }
            }
        }.execute();
    }

    private void initData() {
        progressDialog = new ProgressDialog(this, R.style.DialogStyle);
        progressDialog.setMessage("正在导出。。。");
        progressDialog.setCanceledOnTouchOutside(false);
        rairPath = RairApp.getRairApp().getRairPath();
        diarys = new ArrayList<>();
        queryDiaryFromServer();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                if (msg.what == 0) {
                    CommonUtils.showSnackar(exportLlTxt, "导出成功");
                } else {
                    CommonUtils.showSnackar(exportLlTxt, "导出失败");
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doExportTxt();
                } else {
                    CommonUtils.showSnackar(exportLlTxt, "没有授予读写权限，导出失败,请到设置中手动打开");
                }
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doExportJSON();
                } else {
                    CommonUtils.showSnackar(exportLlTxt, "没有授予读写权限，恢复失败,请到设置中手动打开");
                }
                break;
        }
    }

    @OnClick({R.id.export_iv_back, R.id.export_ll_txt, R.id.export_ll_json})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.export_iv_back:
                this.finish();
                break;
            case R.id.export_ll_txt:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        CommonUtils.showSnackar(exportLlTxt, "需要读写权限");
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                } else {
                    doExportTxt();
                }
                break;
            case R.id.export_ll_json:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                        CommonUtils.showSnackar(exportLlTxt, "需要读写权限");
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    doExportJSON();
                }
                break;
        }
    }

    /**
     * 导出Txt操作
     */
    private void doExportTxt() {
        progressDialog.show();
        final String path = new File(rairPath, Constants.TXT_NAME).getAbsolutePath();
        if (diarys.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean success = exportTxt(path, diarys);
                    if (success) {
                        Message message = Message.obtain(handler, 0);
                        handler.sendMessage(message);
                    }
                }
            }).start();
        } else {
            Toast.makeText(ExportActivity.this, "你还没写日记呢", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 导出json操作
     */
    private void doExportJSON() {
        progressDialog.show();
        final String jsonpath = new File(rairPath, Constants.Export_NAME).getAbsolutePath();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = exportJSONMethod(jsonpath);
                if (success) {
                    Message message = Message.obtain(handler, 0);
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private boolean exportJSONMethod(String outputPdfPath) {
        try {
            File outputFile = new File(outputPdfPath);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
            StringBuilder sb = new StringBuilder();
            for (JsonElement pic : diaryJsonArray) {
                sb.append(pic);
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            outStream.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 转成txt导出
     */
    private boolean exportTxt(String outputPdfPath, ArrayList<DiaryBean> diarys) {
        try {
            File outputFile = new File(outputPdfPath);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "utf-8");
            StringBuilder sb = new StringBuilder();
            for (DiaryBean diary : diarys) {
                sb.append(diary.getDate());
                sb.append("\t\t");
                sb.append(diary.getWeek());
                sb.append("\t\t");
                sb.append(diary.getWeather());
                sb.append("\n");
                sb.append(diary.getTitle());
                sb.append("\n");
                sb.append(diary.getContent());
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            outStream.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 复制单个文件
     *
     * @param dbPath String 原文件路径
     * @param sdPath String 复制后路径
     * @return boolean
     */
    public boolean copyFile(String dbPath, String sdPath) {
        try {
            int len;
            File dbFile = new File(dbPath);
            File sdFile = new File(sdPath);
            if (!sdFile.exists()) {
                sdFile.createNewFile();
            }
            if (dbFile.exists()) { // 文件存在时
                FileInputStream fis = new FileInputStream(dbPath); // 读入原文件
                FileOutputStream fos = new FileOutputStream(sdPath);
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fis.close();
                fos.close();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
//    /**
//     * 转成PDF导出
//     *
//     * @param outputPdfPath 输出路径
//     * @param diarys        日记集合
//     * @return 是否成功
//     */
//    private boolean convertToPdf(String outputPdfPath, ArrayList<DiaryBean> diarys) {
//        try {
//            File outputFile = new File(outputPdfPath);
//            //中文
////            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            Font.FontFamily fontFamily = Font.FontFamily.valueOf("sans-serif");
//            Font fontChinese = new Font(fontFamily, 28, Font.NORMAL);
//            int[] screenWH = RairUtils.screenWH(this);
//            Document document = new Document();
//            document.setPageSize(new Rectangle(screenWH[0], screenWH[1]));
//            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
//            document.open();
//            StringBuilder sb = new StringBuilder();
//            for (DiaryBean diary : diarys) {
//                sb.append(diary.getDate());
//                sb.append("     ");
//                sb.append(diary.getWeek());
//                sb.append("     ");
//                sb.append(diary.getWeather());
//                sb.append("\n");
//                sb.append(diary.getTitle());
//                sb.append("\n");
//                sb.append(diary.getContent());
//                sb.append("\n");
//                sb.append("-------------------------------------");
//                sb.append("\n");
//            }
//            document.add(new Paragraph(sb.toString(), fontChinese));
////            document.add(new Paragraph(sb.toString()));
//            document.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
