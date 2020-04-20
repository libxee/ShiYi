package com.rair.diary.ui.setting.remind;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rair.diary.R;
import com.rair.diary.utils.CommonUtils;
import com.rair.diary.utils.SPUtils;
import com.rair.diary.base.RairApp;
import com.rair.diary.constant.Constants;
import com.rair.diary.service.RemindService;
import com.rair.diary.utils.calendar.CalendarEvent;
import com.rair.diary.utils.calendar.CalendarProviderManager;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rair.diary.utils.Utils.getContext;

public class NotifyActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.notify_iv_back)
    ImageView notifyIvBack;
    @BindView(R.id.notify_switch_open)
    Switch notifySwitchOpen;
    @BindView(R.id.notify_tv_set)
    TextView notifyTvSet;
    @BindView(R.id.notify_tv_current_time)
    TextView notifyTvCurrentTime;
    @BindView(R.id.notify_tv_remind_time)
    TextView notifyTvRemindTime;
    @BindView(R.id.notify_tv_tips)
    TextView notifyTvTips;
    @BindView(R.id.btn_main_add)
    Button btnMainAdd;
    @BindView(R.id.btn_main_delete)
    Button btnMainDelete;
    @BindView(R.id.btn_main_update)
    Button btnMainUpdate;
    @BindView(R.id.btn_main_query)
    Button btnMainQuery;
    @BindView(R.id.tv_event)
    TextView tvEvent;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private Unbinder unbinder;
    private Calendar calendar;
    private AlarmManager manager;
    private SPUtils spUtils;
    private Intent mIntent;
    private boolean isSet;
    private long RemindMILLISECOND = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_CALENDAR}, 1);
        }
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        spUtils = RairApp.getRairApp().getSpUtils();
        isSet = spUtils.getBoolean("isSet", false);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String currentTime = "当前时间：" + CommonUtils.format(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + CommonUtils.format(calendar.get(Calendar.MINUTE));
        notifyTvCurrentTime.setText(currentTime);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == 0 && grantResults[1] == 0)/*Calendar读写申请成功*/
//        {
//            insertEvent();
//        }
//    }
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void setRemind() {
//        int requestCode = 100;
//        int checkSelfPermission_calendar_write,checkSelfPermission_calendar_read;
//        try {
//            checkSelfPermission_calendar_write = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR);
//            checkSelfPermission_calendar_read = ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CALENDAR);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return;
//        }
//        // 如果有授权，走正常插入日历逻辑
//        if ((checkSelfPermission_calendar_read == PackageManager.PERMISSION_GRANTED)
//                &&(checkSelfPermission_calendar_write == PackageManager.PERMISSION_GRANTED)){
//            insertEvent();
//            return;
//        } else {
//            // 如果没有授权，就请求用户授权
//            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR,
//                    Manifest.permission.READ_CALENDAR}, requestCode);
//        }
//    }
    private  void addEvent(){
        CalendarEvent calendarEvent = new CalendarEvent(
                "【拾记】回顾下今天做了什么吧~",
                "让理想生活的样子清晰可见",
                "",
                RemindMILLISECOND,
                RemindMILLISECOND + 60000,
                0,
                "FREQ=DAILY;INTERVAL=1"
        );
        // 添加事件
        int result = CalendarProviderManager.addCalendarEvent(this, calendarEvent);
        if (result == 0) {
            Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
        } else if (result == -1) {
            Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
        } else if (result == -2) {
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
        }
    }
    private  void deleteEvent(){
        // 删除事件
        long calID2 = CalendarProviderManager.obtainCalendarAccountID(this);
        List<CalendarEvent> events2 = CalendarProviderManager.queryAccountEvent(this, calID2);
        if (null != events2) {
            if (events2.size() == 0) {
                Toast.makeText(this, "没有事件可以删除", Toast.LENGTH_SHORT).show();
            } else {
                long eventID = events2.get(0).getId();
                int result2 = CalendarProviderManager.deleteCalendarEvent(this, eventID);
                if (result2 == -2) {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void  updateEvent(){
        // 更新事件
        long calID = CalendarProviderManager.obtainCalendarAccountID(this);
        List<CalendarEvent> events = CalendarProviderManager.queryAccountEvent(this, calID);
        if (null != events) {
            if (events.size() == 0) {
                Toast.makeText(this, "没有提醒可以更新", Toast.LENGTH_SHORT).show();
            } else {
                long eventID = events.get(0).getId();
                int result3 = CalendarProviderManager.updateCalendarEventTime(
                        this, eventID, RemindMILLISECOND,
                        RemindMILLISECOND + 60000);
                if (result3 == 1) {
                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void queryEvent(){
        // 查询事件
        long calID4 = CalendarProviderManager.obtainCalendarAccountID(this);
        List<CalendarEvent> events4 = CalendarProviderManager.queryAccountEvent(this, calID4);
        StringBuilder stringBuilder4 = new StringBuilder();
        if (null != events4) {
            for (CalendarEvent event : events4) {
                stringBuilder4.append(events4.toString()).append("\n");
            }
            tvEvent.setText(stringBuilder4.toString());
            Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void  editSystemCal(){
        // 启动系统日历进行编辑事件
        CalendarProviderManager.startCalendarForIntentToInsert(this, System.currentTimeMillis(),
                System.currentTimeMillis() + 60000, "哈", "哈哈哈哈", "蒂埃纳",
                false);

    }
    private void searchEvent(){
        if (CalendarProviderManager.isEventAlreadyExist(this, 1552986006309L,
                155298606609L, "马上吃饭")) {
            Toast.makeText(this, "存在", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "不存在", Toast.LENGTH_SHORT).show();
        }
    }
    @OnClick({R.id.notify_iv_back, R.id.notify_tv_set,R.id.btn_main_add, R.id.btn_main_delete, R.id.btn_edit,
            R.id.btn_main_update, R.id.btn_main_query, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notify_iv_back:
                this.finish();
                break;
            case R.id.notify_tv_set:
                setRemindTime();
                break;
            case R.id.btn_main_add:
                addEvent();
                break;
            case R.id.btn_main_delete:
                deleteEvent();
                break;
            case R.id.btn_main_update:
                updateEvent();
                break;
            case R.id.btn_main_query:
                queryEvent();
                break;

            case R.id.btn_edit:
                editSystemCal();
                break;
            case R.id.btn_search:
                searchEvent();
                break;
            default:
                break;
        }
    }
    /**
     * 设置提醒时间
     */
    private void setRemindTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                RemindMILLISECOND =  calendar.getTimeInMillis();
                spUtils.put("hour", hourOfDay);
                spUtils.put("minute", minute);
                spUtils.put("setTime", CommonUtils.format(hourOfDay) + CommonUtils.format(minute));
                String remindTime = "提醒时间：" + CommonUtils.format(hourOfDay) + ":" + CommonUtils.format(minute);
                notifyTvRemindTime.setText(remindTime);
            }
        }, mHour, mMinute, true).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }
}
