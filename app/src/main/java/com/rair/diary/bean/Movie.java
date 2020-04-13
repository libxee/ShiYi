package com.rair.diary.bean;

public class Movie {

    /**
     * created_on : 0
     * modified_on : 0
     * id : 0
     * movie_id : 1
     * name : 肖申克的救赎 / The Shawshank Redemption
     * score : 9.7
     * quote : 希望让人自由。
     * ranking : 1
     * cover_url : https://img3.doubanio.com/view/photo/s_ratio_poster/public/p480747492.jpg
     * subject : 1292052
     * year : 1994
     * country : 美国
     * type : 犯罪剧情
     * product_info : 导演:弗兰克·德拉邦特FrankDarabont   主演:蒂姆·罗宾斯TimRobbins/
     * comment_num : 1973359
     */

    private int created_on;
    private int modified_on;
    private int id;
    private int movie_id;
    private String name;
    private double score;
    private String quote;
    private int ranking;
    private String cover_url;
    private String subject;
    private int year;
    private String country;
    private String type;
    private String product_info;
    private int comment_num;

    public int getCreated_on() {
        return created_on;
    }

    public void setCreated_on(int created_on) {
        this.created_on = created_on;
    }

    public int getModified_on() {
        return modified_on;
    }

    public void setModified_on(int modified_on) {
        this.modified_on = modified_on;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }
}
