package com.lifegofood.jt3282.lifego.weather;

/**
 * Created by Blue_bell on 2018/3/20.
 */

public class TodayWeather {
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle_2() {
        return title_2;
    }

    public void setTitle_2(String title_2) {
        this.title_2 = title_2;
    }

    private String title_2;

    private String notice;

    public String getNotice(){ return notice; }

    public void setNotice(String title){ this.notice = title; }


    @Override
    public String toString() {
        return title;
    }
    public String toString_2(){
        return title_2;
    }
    public String toString_3(){
        return notice;
    }


}
