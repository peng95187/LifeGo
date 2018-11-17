package com.lifegofood.jt3282.lifego.weather.newweather;

/**
 * Created by user on 2018/8/30.
 */

public class TodayWeather {
    private String time;
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
         this.time = time;
        }

    private String detail;
         public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

    private String morning;
        public String getMorning() {
            return morning;
        }

        public void setMorning(String morning) {
            this.morning = morning;
        }
    private String night;
        public String getNight() {
            return night;
        }

        public void setNight(String night) {
            this.night = night;
        }






    @Override
    public String toString() {
        return detail;
    }
    public String toString_Morning() {
        return morning;
    }
    public String toString_Night() {
        return night;
    }
    public String toString_Time() {
        return time;
    }
}
