package com.rair.diary.bean;

class Date {

    private String curr;
    private String prev;
    private String next;
    public void setCurr(String curr) {
        this.curr = curr;
    }
    public String getCurr() {
        return curr;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }
    public String getPrev() {
        return prev;
    }

    public void setNext(String next) {
        this.next = next;
    }
    public String getNext() {
        return next;
    }

}
public class OneArticle {

    private Date date;
    private String author;
    private String title;
    private String digest;
    private String content;
    private int wc;
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
    public String getDigest() {
        return digest;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setWc(int wc) {
        this.wc = wc;
    }
    public int getWc() {
        return wc;
    }

}