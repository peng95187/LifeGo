package com.lifegofood.jt3282.lifego.traffic.voice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.bus.BusRoute;
import com.lifegofood.jt3282.lifego.traffic.bus.BusSelectSearch;
import com.lifegofood.jt3282.lifego.traffic.bus.BusStop;
import com.lifegofood.jt3282.lifego.traffic.bus.CountySelect;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRPrice;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRPriceSearch;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRQSearch;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRQSearchDetail;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRSearch;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRSelectSearch;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRStation;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRStationSearch;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusPrice;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusSearch;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusSelect;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusStop;
import com.lifegofood.jt3282.lifego.traffic.minelike.Like_route;
import com.lifegofood.jt3282.lifego.traffic.minelike.Like_stop;
import com.lifegofood.jt3282.lifego.traffic.minelike.MineLike_select;
import com.lifegofood.jt3282.lifego.traffic.notify.Notification;
import com.lifegofood.jt3282.lifego.traffic.notify.Notification_add;
import com.lifegofood.jt3282.lifego.traffic.railway.Railway;
import com.lifegofood.jt3282.lifego.traffic.railway.TRAPrice;
import com.lifegofood.jt3282.lifego.traffic.railway.TRAQsearch;
import com.lifegofood.jt3282.lifego.traffic.railway.TRASelectSearch;
import com.lifegofood.jt3282.lifego.traffic.railway.Timeboard;
import com.lifegofood.jt3282.lifego.traffic.railway.TrainSearch;
import com.lifegofood.jt3282.lifego.traffic.transfer.Transfer_Select;

import java.util.ArrayList;
import java.util.Calendar;

public class TrafficVoice extends Activity {

    private String all = "";
    Intent start = new Intent();
    String [] HSR = {"南港", "雲林", "彰化", "台北", "板橋", "桃園", "新竹", "台中", "嘉義", "台南", "左營", "苗栗"};
    String [] HSRID = {"0990", "1047", "1043", "1000", "1010", "1020", "1030", "1040", "1050", "1060", "1070", "1035"};

    String [] County = {"臺北市", "新北市", "桃園市", "臺中市", "臺南市", "高雄市", "基隆市",
            "新竹市", "新竹縣", "苗栗縣", "彰化縣", "南投縣", "雲林縣",
            "嘉義縣", "嘉義市", "屏東縣", "宜蘭縣", "花蓮縣", "臺東縣",
            "金門縣", "澎湖縣", "連江縣"};
    String [] CountyEn =  {"Taipei", "NewTaipei", "Taoyuan", "Taichung", "Tainan", "Kaohsiung", "Keelung", "Hsinchu", "HsinchuCounty", "MiaoliCounty",
            "ChanghuaCounty", "NantouCounty", "YunlinCounty", "ChiayiCounty", "Chiayi", "PingtungCounty,", "YilanCounty", "HualienCounty",
            "TaitungCounty", "KinmenCounty", "PenghuCounty", "LienchiangCounty"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_voice);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //把所有辨識的可能結果印出來看一看，第一筆是最 match 的。

                ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                all = "";
                for (Object r : result) {
                    all = all + r + "\n";
                }
                chooseCommands(all);
            }
        }
        finish();
    }
    private void chooseCommands(String command){
        if(command.contains("公車")){
            BusCommand(command);
        }
        else if(command.contains("客運")){
            IBusCommand(command);
        }
        else if(command.contains("高鐵")){
            HSRCommand(command);
        }
        else if(command.contains("台鐵")){
            TRACommand(command);
        }
        else if(command.contains("轉乘")){
            Transfer();
        }
        else if(command.contains("最愛")){
            MinePre(command);
        }
        else if(command.contains("提醒")){
            Notify(command);
        }else{
            Toast.makeText(this, "聽不懂 ˇ ˇ", Toast.LENGTH_SHORT).show();
        }
    }
    //台鐵指令
    private void TRACommand(String command){
        if(command.contains("時刻表")){
            start.setClass(TrafficVoice.this, Timeboard.class);
            startActivity(start);
        }else if(command.contains("到站")){
            start.setClass(TrafficVoice.this, Railway.class);
            startActivity(start);
        }else if(command.contains("快速")){
            start.setClass(TrafficVoice.this, TRAQsearch.class);
            startActivity(start);
        }else if(command.contains("票價")){
            start.setClass(TrafficVoice.this, TRAPrice.class);
            startActivity(start);
        }else if(command.contains("車次")){
            start.setClass(TrafficVoice.this, TrainSearch.class);
            startActivity(start);
        }else{
            start.setClass(TrafficVoice.this, TRASelectSearch.class);
            startActivity(start);
        }
    }

    //高鐵指令
    private void HSRCommand(String command){
        int count = 0;
        for(int i = 0;i < 12;i++){
            if(command.contains(HSR[i])){
                int count2 = 0;
                for(int j = 0;j < 12;j++){
                    if(command.contains(HSR[j])&&!(HSR[i].equals(HSR[i]))){
                        if(command.contains("票價")){
                            start.setClass(TrafficVoice.this, HSRPriceSearch.class);
                            start.putExtra("stationID",HSRID[i]);
                            start.putExtra("stationID2",HSRID[j]);
                            start.putExtra("station",HSR[i]);
                            start.putExtra("station2",HSR[j]);
                            startActivity(start);
                            break;
                        }
                        else if(command.contains("時刻表")){
                            Calendar mCal = Calendar.getInstance();
                            CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
                            start.setClass(TrafficVoice.this, HSRQSearchDetail.class);
                            start.putExtra("stationID",HSRID[i]);
                            start.putExtra("stationID2",HSRID[j]);
                            start.putExtra("saveTime",""+s);
                            startActivity(start);
                            break;
                        }
                    }else{
                        count2++;
                    }
                }
                if(count2==12){
                    if(command.contains("時刻表")){
                        Calendar mCal = Calendar.getInstance();
                        CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
                        start.setClass(TrafficVoice.this, HSRStationSearch.class);
                        start.putExtra("station",HSR[i]);
                        start.putExtra("stationID",HSRID[i]);
                        start.putExtra("saveTime",""+s);
                        startActivity(start);
                        break;
                    }
                }
            }else{
                count++;
            }
        }
        if(count==12){
            if(command.contains("時刻表")){
                start.setClass(TrafficVoice.this, HSRStation.class);
                startActivity(start);
            }else if(command.contains("快速")){
                start.setClass(TrafficVoice.this, HSRQSearch.class);
                startActivity(start);
            }else if(command.contains("票價")){
                start.setClass(TrafficVoice.this, HSRPrice.class);
                startActivity(start);
            }else if(command.contains("車次")){
                start.setClass(TrafficVoice.this, HSRSearch.class);
                startActivity(start);
            }else{
                start.setClass(TrafficVoice.this, HSRSelectSearch.class);
                startActivity(start);
            }
        }
    }

    //公車指令
    private void BusCommand(String command){
        int count = 0;
        for(int i = 0;i < 22;i++){
            if(command.contains(County[i])){
                if(command.contains("路線")){
                    start.setClass(TrafficVoice.this, BusRoute.class);
                    start.putExtra("location",CountyEn[i]);
                    startActivity(start);
                    break;
                }else if(command.contains("站牌")){
                    start.setClass(TrafficVoice.this, BusStop.class);
                    start.putExtra("location",CountyEn[i]);
                    startActivity(start);
                    break;
                }else{
                    start.setClass(TrafficVoice.this, BusSelectSearch.class);
                    startActivity(start);
                    break;
                }
            }else{
                count++;
            }
        }
        if(count == 22){
            start.setClass(TrafficVoice.this, CountySelect.class);
            startActivity(start);
        }
    }

    //公路客運指令
    private void IBusCommand(String command){
        if(command.contains("站牌")){
            start.setClass(TrafficVoice.this, IBusStop.class);
            startActivity(start);
        }else if(command.contains("路線")){
            start.setClass(TrafficVoice.this, IBusSearch.class);
            startActivity(start);
        }else if(command.contains("票價")){
            start.setClass(TrafficVoice.this, IBusPrice.class);
            startActivity(start);
        }else{
            start.setClass(TrafficVoice.this, IBusSelect.class);
            startActivity(start);
        }
    }

    //轉乘指令
    private void Transfer(){
        start.setClass(TrafficVoice.this, Transfer_Select.class);
        startActivity(start);
    }

    //我的最愛指令
    private void MinePre(String command){
        if(command.contains("站牌")){
            start.setClass(TrafficVoice.this, Like_stop.class);
            startActivity(start);
        }else if(command.contains("路線")){
            start.setClass(TrafficVoice.this, Like_route.class);
            startActivity(start);
        }else {
            start.setClass(TrafficVoice.this, MineLike_select.class);
            startActivity(start);
        }
    }

    //提醒指令
    private void Notify(String command){
        if(command.contains("新增")||command.contains("添加")) {
            start.setClass(TrafficVoice.this, Notification_add.class);
            startActivity(start);
        }else{
            start.setClass(TrafficVoice.this, Notification.class);
            startActivity(start);
        }
    }
}


