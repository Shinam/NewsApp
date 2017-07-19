package com.example.android.newsapp;

/**
 * Created by Shinam on 18/07/2017.
 */

public class News {

    public String title;
    public String section;
    public String web;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection() {
        return section;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public News(String title, String section, String web) {
        this.title = title;
        this.section = section;
        this.web = web;
    }
}
