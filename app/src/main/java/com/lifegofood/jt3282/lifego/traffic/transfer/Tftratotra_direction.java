package com.lifegofood.jt3282.lifego.traffic.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import java.util.Calendar;

public class Tftratotra_direction extends Activity {

    Spinner sp1,sp2,sp3,sp4,sp5,sp6,sp7,sp8,timesp,datesp;
    Button button;
    RadioButton rb1,rb2,rb3;
    String start = "",mid = "",mid2 = "",end = "",autoMid = "",initMid = "",dateselect = "",timeselect = "";

    String [] time = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00"
            ,"10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00","24:00"};

    String [] init = {"請選擇"};

    String [] Taipei = {"福隆","貢寮","雙溪","牡丹","三貂嶺","猴硐","瑞芳","四腳亭",
            "暖暖","基隆","三坑","八堵","七堵","百福","五堵","汐止","汐科","南港","松山","台北",
            "萬華","板橋","浮州","樹林","南樹林","山佳","鶯歌"};
    String [] TaipeiID = {"1810","1809","1808","1807","1806","1805","1804","1803",
            "1802","1001","1029","1002","1003","1030","1004","1005","1031","1006","1007","1008",
            "1009","1011","1032","1012","1034","1013","1014"};

    String [] Taouyuan ={"桃園","內壢","中壢","埔心","楊梅","富岡","新富"};
    String [] TaouyuanID ={"1015","1016","1017","1018","1019","1020","1036"};

    String [] Hsinchu = {"北湖","湖口","新豐","竹北","北新竹","新竹","三姓橋","香山"};
    String [] HsinchuID = {"1033","1021","1022","1023","1024","1025","1035","1026"};

    String [] Miaoli = {"崎頂","竹南","談文","大山","後龍","龍港","白沙屯","新埔","通宵","苑裡","造橋","豐富","苗栗","南勢","銅鑼","三義"};
    String [] MiaoliID = {"1027","1028","1102","1104","1105","1106","1107","1108","1109","1110","1302","1304","1305","1307","1308","1310"};

    String [] Taichung = {"日南","大甲","臺中港","清水","沙鹿","龍井","大肚","追分","泰安","后里","豐原","潭子","太原","臺中","大慶","烏日","新烏日","成功"};
    String [] TaichungID = {"1111","1112","1113","1114","1115","1116","1117","1118","1314",
            "1315","1317","1318","1323","1319","1322","1320","1324","1321"};

    String [] Changhua = {"彰化","花壇","大村","員林","永靖","社頭","田中","二水","源泉"};
    String [] ChanghuaID = {"1120","1202","1240","1203","1204","1205","1206","1207","2702"};

    String [] Nantou = {"濁水","龍泉","集集","水里","車埕"};
    String [] NantouID = {"2703","2704","2705","2706","2707"};

    String [] Yunlin = {"林內","石榴","斗六","斗南","石龜"};
    String [] YunlinID = {"1208","1209","1210","1211","1212"};

    String [] Chiayi = {"大林","民雄","嘉北","嘉義","水上","南靖"};
    String [] ChiayiID = {"1213","1214","1241","1215","1217","1218"};

    String [] Tainan = {"後壁","新營","柳營","林鳳營","隆田","拔林","善化","南科","新市","永康","大橋","台南","保安","仁德","中洲","長榮大學","沙崙"};
    String [] TainanID = {"1219","1220","1221","1222","1223","1224","1225","1244","1226","1227","1239","1228",
            "1229","1243","1230","5101","5102"};

    String [] Kaohsiung = {"大湖","路竹","岡山","橋頭","楠梓","新左營","左營","高雄","鳳山","後庄","九曲堂"};
    String [] KaohsiungID = {"1231","1232","1233","1234","1235","1242","1236","1238","1402","1403","1404"};

    String [] Pintung = {"六塊厝","屏東","歸來","麟洛","西勢","竹田","潮州","崁頂","南州","鎮安","林邊","佳冬","東海","枋寮","加祿","內獅","枋山"};
    String [] PintungID = {"1405","1406","1407","1408","1409","1410","1411","1412","1413","1414","1415","1416",
            "1417","1418","1502","1503","1504"};

    String [] Taitung = {"大武","瀧溪","金崙","太麻里","知本","康樂","台東","山里","鹿野","瑞源","瑞和","關山","海端","池上"};
    String [] TaitungID = {"1508","1510","1512","1514","1516","1517","1632","1631","1630","1629","1628","1626", "1625","1624"};

    String [] Hualien = {"富里","東竹","東里","玉里","三民","瑞穗","富源","大富","光復","萬榮","鳳林","南平","豐田","壽豐","平和","志學","吉安",
            "花蓮","北埔","景美"};
    String [] HualienID = {"1623","1622","1621","1619","1617","1616","1614","1613","1612","1611","1610","1609",
            "1607","1606","1605","1604","1602","1715","1714","1713"};

    String [] Ilan = {"漢本","武塔","南澳","東澳","永樂","蘇澳","蘇澳新","新馬","冬山","羅東","中里","二結","宜蘭","四城","礁溪","頂埔","頭城",
            "外澳","龜山","大溪","大里","石城"};
    String [] IlanID = {"1708","1706","1705","1704","1703","1827","1826","1825","1824","1823","1822","1821","1820","1819","1818","1817",
            "1816","1815","1814","1813","1812","1811"};

    String [] PingCreek = {"瑞芳","猴硐","三貂嶺","菁桐","平溪","嶺腳","望古","十分","大華","海科館","八斗子"};
    String [] PingCreekID = {"1804","1805","1806","1908","1907","1906","1905","1904","1903","6103","2003"};

    String [] InnerBay = {"新竹","北新竹","千甲","新莊","竹中","六家","上員","榮華","竹東","橫山","九讚頭","合興","富貴","內灣"};
    String [] InnerBayID = {"1025","1024","2212","2213","2203","2214","2204","2211","2205","2206","2207","2208","2209","2210"};

    String [] GiGi = {"二水","源泉","濁水","龍泉","集集","水里","車埕"};
    String [] GiGiID = {"1207","2702","2703","2704","2705","2706","2707"};

    String [] Saren = {"中洲","長榮大學","沙崙"};
    String [] SarenID = {"1230","5101","5102"};

    String [] Classes = {"請選擇","台北/基隆地區","桃園地區","新竹地區","苗栗地區","台中地區","彰化地區","南投地區","雲林地區","嘉義地區","台南地區","高雄地區",
            "屏東地區","台東地區","花蓮地區","宜蘭地區","平溪/深澳線","內灣/六家線","集集線","沙崙線"};

    ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tftratotra_direction);
        sp1 = (Spinner) findViewById(R.id.sp1);
        sp2 = (Spinner) findViewById(R.id.sp2);
        sp3 = (Spinner) findViewById(R.id.sp3);
        sp4 = (Spinner) findViewById(R.id.sp4);
        sp5 = (Spinner) findViewById(R.id.sp5);
        sp6 = (Spinner) findViewById(R.id.sp6);
        sp7 = (Spinner) findViewById(R.id.sp7);
        sp8 = (Spinner) findViewById(R.id.sp8);
        timesp = (Spinner) findViewById(R.id.timesp);
        datesp = (Spinner) findViewById(R.id.datesp);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);

        Calendar mCal = Calendar.getInstance();

        //插入時間
        final String [] date = new String[15];

        for(int i = 0;i < 15;i++){
            if(i>0) mCal.add(mCal.DATE,1);
            CharSequence s = DateFormat.format("yyyy-MM-dd", mCal.getTime());
            date[i] = ""+s;
        }

        final ArrayAdapter<String> ap1 = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Classes);
        final ArrayAdapter<String> ap2 = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, init);
        final ArrayAdapter<String> dateap = new ArrayAdapter<>(Tftratotra_direction.this, R.layout.myspinner, date);
        final ArrayAdapter<String> timeap = new ArrayAdapter<>(Tftratotra_direction.this, R.layout.myspinner, time);

        sp1.setAdapter(ap1);
        sp2.setAdapter(ap2);
        sp5.setAdapter(ap1);
        sp6.setAdapter(ap2);

        timesp.setAdapter(timeap);
        datesp.setAdapter(dateap);


        //日期
        AdapterView.OnItemSelectedListener dateSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dateselect = date[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        //時間
        AdapterView.OnItemSelectedListener timeSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeselect = time[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 1:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = TaipeiID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = TaouyuanID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = HsinchuID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = MiaoliID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = TaichungID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = ChanghuaID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = NantouID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = YunlinID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = ChiayiID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = TainanID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = KaohsiungID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = PintungID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = TaitungID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = HualienID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 15:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = IlanID[position];
                                initMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 16:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = PingCreekID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 17:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = InnerBayID[position];
                                if(position<=1){
                                    initMid = "";
                                }else{
                                    initMid = "2203";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 18:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = GiGiID[position];
                                initMid = "1207";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 19:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                        sp2.setAdapter(adp);
                        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                start = SarenID[position];
                                initMid = "1228";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        //中途站2
        final AdapterView.OnItemSelectedListener midSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 1:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = TaipeiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TaouyuanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = HsinchuID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = MiaoliID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = TaichungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = ChanghuaID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = NantouID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = YunlinID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = ChiayiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = TainanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = KaohsiungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = PintungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = TaitungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = HualienID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 15:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = IlanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 16:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = PingCreekID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 17:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = InnerBayID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 18:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = GiGiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 19:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                        sp8.setAdapter(adp);
                        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid2 = SarenID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        //中途站1
        final AdapterView.OnItemSelectedListener midSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 1:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TaipeiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TaouyuanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = HsinchuID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = MiaoliID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TaichungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = ChanghuaID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = NantouID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = YunlinID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = ChiayiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TainanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = KaohsiungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = PintungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = TaitungID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = HualienID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 15:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = IlanID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 16:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = PingCreekID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 17:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = InnerBayID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 18:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = GiGiID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 19:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mid = SarenID[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


       //目的站
        AdapterView.OnItemSelectedListener endSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 1:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = TaipeiID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = TaouyuanID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = HsinchuID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = MiaoliID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = TaichungID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = ChanghuaID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = NantouID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = YunlinID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = ChiayiID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = TainanID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = KaohsiungID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = PintungID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = TaitungID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = HualienID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 15:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = IlanID[position];
                                autoMid = "";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 16:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = PingCreekID[position];
                                autoMid = "1804";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 17:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = InnerBayID[position];

                                if(position<=1) {
                                    autoMid = "";
                                }else{
                                    autoMid = "2203";
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 18:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = GiGiID[position];
                                autoMid = "1207";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 19:
                        adp = new ArrayAdapter<>(Tftratotra_direction.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                        sp6.setAdapter(adp);
                        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                end = Saren[position];
                                autoMid = "1228";
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        if (rb1.isChecked()){
            mid= "auto";
        }
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mid= "auto";
                }else{
                    mid = "";
                }
            }
        });
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp3.setVisibility(View.VISIBLE);
                    sp4.setVisibility(View.VISIBLE);
                    sp3.setAdapter(ap1);
                    sp4.setAdapter(ap2);
                    sp3.setOnItemSelectedListener(midSelectedListener);
                }else{
                    sp3.setVisibility(View.GONE);
                    sp4.setVisibility(View.GONE);
                    sp3.setAdapter(null);
                    sp4.setAdapter(null);
                }
            }
        });
        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sp3.setVisibility(View.VISIBLE);
                    sp4.setVisibility(View.VISIBLE);
                    sp3.setAdapter(ap1);
                    sp4.setAdapter(ap2);
                    sp3.setOnItemSelectedListener(midSelectedListener);
                    sp7.setVisibility(View.VISIBLE);
                    sp8.setVisibility(View.VISIBLE);
                    sp7.setAdapter(ap1);
                    sp8.setAdapter(ap2);
                    sp7.setOnItemSelectedListener(midSelectedListener2);
                }else{
                    sp3.setVisibility(View.GONE);
                    sp4.setVisibility(View.GONE);
                    sp3.setAdapter(null);
                    sp4.setAdapter(null);
                    sp7.setVisibility(View.GONE);
                    sp8.setVisibility(View.GONE);
                    sp7.setAdapter(null);
                    sp8.setAdapter(null);
                }
            }
        });

        datesp.setOnItemSelectedListener(dateSelectedListener);
        timesp.setOnItemSelectedListener(timeSelectedListener);
        sp1.setOnItemSelectedListener(itemSelectedListener);
        sp5.setOnItemSelectedListener(endSelectedListener);

        button = (Button) findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(start.equals("")||start==null){
                    Toast.makeText(Tftratotra_direction.this, "出發站未選擇", Toast.LENGTH_SHORT).show();
                }else if(mid.equals("")||mid==null){
                    Toast.makeText(Tftratotra_direction.this, "中途站未選擇", Toast.LENGTH_SHORT).show();
                }else if(end.equals("")||end==null){
                    Toast.makeText(Tftratotra_direction.this, "目的站未選擇", Toast.LENGTH_SHORT).show();
                }else {
                    if(rb1.isChecked()) {
                        Intent intent = new Intent(Tftratotra_direction.this, Tftratotra_detail.class);
                        intent.putExtra("date", dateselect);
                        intent.putExtra("time", timeselect);
                        intent.putExtra("start", start);
                        intent.putExtra("end", end);

                        if (initMid.equals("") || initMid == null) {
                            if (autoMid.equals("") || autoMid == null) {
                                Toast.makeText(Tftratotra_direction.this, "無直達車，建議選擇轉乘站", Toast.LENGTH_SHORT).show();
                            } else {
                                intent.putExtra("mid", autoMid);
                            }
                        } else {
                            intent.putExtra("mid", initMid);
                        }
                        startActivity(intent);
                    }else if(rb2.isChecked()){
                        Intent intent = new Intent(Tftratotra_direction.this, Tftratotra_detail.class);
                        intent.putExtra("date", dateselect);
                        intent.putExtra("time", timeselect);
                        intent.putExtra("start", start);
                        intent.putExtra("mid", mid);
                        intent.putExtra("end", end);
                        startActivity(intent);
                    }
                    else if(rb3.isChecked()){
                        Intent intent = new Intent(Tftratotra_direction.this, Tftratotra_detail2.class);
                        intent.putExtra("date", dateselect);
                        intent.putExtra("time", timeselect);
                        intent.putExtra("start", start);
                        intent.putExtra("mid1", mid);
                        intent.putExtra("mid2", mid2);
                        intent.putExtra("end", end);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
