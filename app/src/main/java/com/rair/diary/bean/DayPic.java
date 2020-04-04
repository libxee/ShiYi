package com.rair.diary.bean;

import android.support.annotation.NonNull;

class Wx {
    private String title;

    private String desc;

    private String link;

    private String imgUrl;

    private String audio;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }
}

class Wx_timeline {
    private String title;

    private String desc;

    private String link;

    private String imgUrl;

    private String audio;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }
}

class Weibo {
    private String title;

    private String desc;

    private String link;

    private String imgUrl;

    private String audio;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }
}

class Qq {
    private String title;

    private String desc;

    private String link;

    private String imgUrl;

    private String audio;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }
}

class Share_list {
    private Wx wx;

    private Wx_timeline wx_timeline;

    private Weibo weibo;

    private Qq qq;

    public void setWx(Wx wx) {
        this.wx = wx;
    }

    public Wx getWx() {
        return this.wx;
    }

    public void setWx_timeline(Wx_timeline wx_timeline) {
        this.wx_timeline = wx_timeline;
    }

    public Wx_timeline getWx_timeline() {
        return this.wx_timeline;
    }

    public void setWeibo(Weibo weibo) {
        this.weibo = weibo;
    }

    public Weibo getWeibo() {
        return this.weibo;
    }

    public void setQq(Qq qq) {
        this.qq = qq;
    }

    public Qq getQq() {
        return this.qq;
    }
}

public class DayPic<DateTime> {
    private String hpcontent_id;

    private String hp_title;

    private String author_id;

    private String hp_img_url;

    private String hp_img_original_url;

    private String hp_author;

    private String ipad_url;

    private String hp_content;

    private DateTime hp_makettime;

    private DateTime last_update_date;

    private String web_url;

    private String wb_img_url;

    private String image_authors;

    private String text_authors;

    private String image_from;

    private String text_from;

    private String content_bgcolor;

    private String template_category;

    private DateTime maketime;

    private Share_list share_list;

    private int praisenum;

    private int sharenum;

    private int commentnum;

    public void setHpcontent_id(String hpcontent_id) {
        this.hpcontent_id = hpcontent_id;
    }

    public String getHpcontent_id() {
        return this.hpcontent_id;
    }

    public void setHp_title(String hp_title) {
        this.hp_title = hp_title;
    }

    public String getHp_title() {
        return this.hp_title;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_id() {
        return this.author_id;
    }

    public void setHp_img_url(String hp_img_url) {
        this.hp_img_url = hp_img_url;
    }

    public String getHp_img_url() {
        return this.hp_img_url;
    }

    public void setHp_img_original_url(String hp_img_original_url) {
        this.hp_img_original_url = hp_img_original_url;
    }

    public String getHp_img_original_url() {
        return this.hp_img_original_url;
    }

    public void setHp_author(String hp_author) {
        this.hp_author = hp_author;
    }

    public String getHp_author() {
        return this.hp_author;
    }

    public void setIpad_url(String ipad_url) {
        this.ipad_url = ipad_url;
    }

    public String getIpad_url() {
        return this.ipad_url;
    }

    public void setHp_content(String hp_content) {
        this.hp_content = hp_content;
    }

    public String getHp_content() {
        return this.hp_content;
    }

    public void setHp_makettime(DateTime hp_makettime) {
        this.hp_makettime = hp_makettime;
    }

    public DateTime getHp_makettime() {
        return this.hp_makettime;
    }

    public void setLast_update_date(DateTime last_update_date) {
        this.last_update_date = last_update_date;
    }

    public DateTime getLast_update_date() {
        return this.last_update_date;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getWeb_url() {
        return this.web_url;
    }

    public void setWb_img_url(String wb_img_url) {
        this.wb_img_url = wb_img_url;
    }

    public String getWb_img_url() {
        return this.wb_img_url;
    }

    public void setImage_authors(String image_authors) {
        this.image_authors = image_authors;
    }

    public String getImage_authors() {
        return this.image_authors;
    }

    public void setText_authors(String text_authors) {
        this.text_authors = text_authors;
    }

    public String getText_authors() {
        return this.text_authors;
    }

    public void setImage_from(String image_from) {
        this.image_from = image_from;
    }

    public String getImage_from() {
        return this.image_from;
    }

    public void setText_from(String text_from) {
        this.text_from = text_from;
    }

    public String getText_from() {
        return this.text_from;
    }

    public void setContent_bgcolor(String content_bgcolor) {
        this.content_bgcolor = content_bgcolor;
    }

    public String getContent_bgcolor() {
        return this.content_bgcolor;
    }

    public void setTemplate_category(String template_category) {
        this.template_category = template_category;
    }

    public String getTemplate_category() {
        return this.template_category;
    }

    public void setMaketime(DateTime maketime) {
        this.maketime = maketime;
    }

    public DateTime getMaketime() {
        return this.maketime;
    }

    public void setShare_list(Share_list share_list) {
        this.share_list = share_list;
    }

    public Share_list getShare_list() {
        return this.share_list;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getPraisenum() {
        return this.praisenum;
    }

    public void setSharenum(int sharenum) {
        this.sharenum = sharenum;
    }

    public int getSharenum() {
        return this.sharenum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public int getCommentnum() {
        return this.commentnum;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getText_authors() + this.getHp_content();
    }
}
