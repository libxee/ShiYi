package com.rair.diary.constant;


public class Constants {
    //DB
    public static final String DB_NAME = "diary.db";
    public static final int DB_VERSION = 2;
    //RairPath
    public static final String RAIR_PATH = "ShijiDiary";
    //    public static final String PDF_NAME = "我的日记.pdf";
    public static final String TXT_NAME = "我的日记.txt";
    public static final String BACKUP_NAME = "Backup.json";
    public static final String Export_NAME = "我的日记.json";
    public static final String HEAD_IMAGE = "head.jpg";
    public static final String FORMAT = "%s（%s）%s";
    //SharedPreferences
    public static final String SP_NAME = "shiji";
    public static final String RECREATE = "isRecreate";
    public static final String CURRENT_USERNAME = "current_username";
    public static final String CURRENT_TOKEN = "current_token";
    public static final boolean LOGIN_STATUS = false;

    //Activity REQUEST CODE
    public static final int ADD_NEW_DIARY = 1;
    public static final int DETAIL_DIARY = 2;

    //RESULT CODE
    public static final int ADD_NEW_DIARY_SUCCESS = 10;
    public static final int ADD_NEW_DIARY_FAILED = 11;
    public static final int DETAIL_DIARY_EDITED = 20;
    public static final int DETAIL_DIARY_NOT_EDITED = 21;
    public static final int DETAIL_DIARY_EDITED_SUCCESS = 22;
    public static final int DETAIL_DIARY_EDITED_FAILED = 23;

    //设置提醒时间
    public static final String SET_RECEIVER = "com.shiji.set";
    //提醒广播
    public static final String REMIND_RECEIVER = "com.shiji.time";

}
