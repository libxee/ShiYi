package com.rair.diary.bean;

import cn.bmob.v3.BmobObject;


public class FeedBack extends BmobObject {

    private String content;
    private String contact;

    public FeedBack() {
    }

    public FeedBack(String content, String contact) {
        this.content = content;
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
