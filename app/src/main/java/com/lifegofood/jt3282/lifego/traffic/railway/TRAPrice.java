package com.lifegofood.jt3282.lifego.traffic.railway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

public class TRAPrice extends Activity {

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

    String [] Changhua = {"彰化","花壇","大村","員林","永靖","社頭","田中","二水"};
    String [] ChanghuaID = {"1120","1202","1240","1203","1204","1205","1206","1207"};

    String [] Nantou = {"二水","源泉","濁水","龍泉","集集","水里","車埕"};
    String [] NantouID = {"1207","2702","2703","2704","2705","2706","2707"};

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

    Spinner sp1,sp2,sp3,sp4;
    String save,stop,save2,stop2;
    Button btn;
    ArrayAdapter<String> adp;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traprice);

        intent.setClass(this,TRAPriceSearch.class);
        btn = (Button)findViewById(R.id.search);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Classes);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, init);

        sp1 = (Spinner)findViewById(R.id.spinner1);
        sp2 = (Spinner)findViewById(R.id.spinner2);
        sp3 = (Spinner)findViewById(R.id.spinner3);
        sp4 = (Spinner)findViewById(R.id.spinner4);

        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter2);
        sp3.setAdapter(adapter);
        sp4.setAdapter(adapter2);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 1:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = TaipeiID[position];
                                    stop = Taipei[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 2:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = TaouyuanID[position];
                                    stop = Taouyuan[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 3:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = HsinchuID[position];
                                    stop = Hsinchu[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 4:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = MiaoliID[position];
                                    stop = Miaoli[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 5:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = TaichungID[position];
                                    stop = Taichung[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 6:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = ChanghuaID[position];
                                    stop = Changhua[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 7:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = NantouID[position];
                                    stop = Nantou[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 8:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = YunlinID[position];
                                    stop = Yunlin[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 9:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = ChiayiID[position];
                                    stop = Chiayi[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 10:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = TainanID[position];
                                    stop = Tainan[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 11:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = KaohsiungID[position];
                                    stop = Kaohsiung[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 12:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = PintungID[position];
                                    stop = Pintung[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 13:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = TaitungID[position];
                                    stop = Taitung[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 14:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = HualienID[position];
                                    stop = Hualien[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 15:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = IlanID[position];
                                    stop = Ilan[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 16:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = PingCreekID[position];
                                    stop = PingCreek[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 17:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = InnerBayID[position];
                                    stop = PingCreek[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 18:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = GiGiID[position];
                                    stop = GiGi[position];
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            break;
                        case 19:
                            adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                            sp2.setAdapter(adp);
                            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    save = SarenID[position];
                                    stop = Saren[position];
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
        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taipei);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = TaipeiID[position];
                                stop2 = Taipei[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 2:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taouyuan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = TaouyuanID[position];
                                stop2 = Taouyuan[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 3:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Hsinchu);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = HsinchuID[position];
                                stop2 = Hsinchu[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 4:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Miaoli);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = MiaoliID[position];
                                stop2 = Miaoli[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 5:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taichung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = TaichungID[position];
                                stop2 = Taichung[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 6:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Changhua);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = ChanghuaID[position];
                                stop2 = Changhua[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 7:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Nantou);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = NantouID[position];
                                stop2 = Nantou[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 8:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Yunlin);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = YunlinID[position];
                                stop2 = Yunlin[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 9:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Chiayi);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = ChiayiID[position];
                                stop2 = Chiayi[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 10:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Tainan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = TainanID[position];
                                stop2 = Tainan[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 11:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Kaohsiung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = KaohsiungID[position];
                                stop2 = Kaohsiung[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 12:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Pintung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = PintungID[position];
                                stop2 = Pintung[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 13:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Taitung);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = TaitungID[position];
                                stop2 = Taitung[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 14:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Hualien);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = HualienID[position];
                                stop2 = Hualien[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 15:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Ilan);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = IlanID[position];
                                stop2 = Ilan[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 16:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, PingCreek);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = PingCreekID[position];
                                stop2 = PingCreek[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 17:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, InnerBay);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = InnerBayID[position];
                                stop2 = PingCreek[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 18:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, GiGi);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = GiGiID[position];
                                stop2 = GiGi[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        break;
                    case 19:
                        adp = new ArrayAdapter<>(TRAPrice.this, android.R.layout.simple_spinner_dropdown_item, Saren);
                        sp4.setAdapter(adp);
                        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                save2 = SarenID[position];
                                stop2 = Saren[position];
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

        sp1.setOnItemSelectedListener(itemSelectedListener);
        sp3.setOnItemSelectedListener(itemSelectedListener2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save==null||save2==null){
                    Toast.makeText(TRAPrice.this, "未選擇", Toast.LENGTH_SHORT).show();
                }else if(save.equals(save2)) {
                    Toast.makeText(TRAPrice.this, "選擇相同車站", Toast.LENGTH_SHORT).show();
                }else{
                    intent.putExtra("station",stop);
                    intent.putExtra("stationID",save);
                    intent.putExtra("station2",stop2);
                    intent.putExtra("stationID2",save2);
                    startActivity(intent);
                }
            }
        });
    }
}
