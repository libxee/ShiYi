package com.rair.diary.bean;


public class FeedBack {

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
