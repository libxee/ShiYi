package com.rair.diary.bean;

public class Book {
    /**
     * name : 红楼梦
     * score : 9.6
     * quote : 都云作者痴，谁解其中味？
     * numberOfComments : 274524
     * author : [清] 曹雪芹 著
     * press : 人民文学出版社
     * price : 59.70元
     * year : 1996
     * subject : 1007305
     * image : https://img1.doubanio.com/view/subject/s/public/s1070959.jpg
     */

    private String name;
    private double score;
    private String quote;
    private int comment_num;
    private String author;
    private String press;
    private String price;
    private int year;
    private String subject;
    private String image;


    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
