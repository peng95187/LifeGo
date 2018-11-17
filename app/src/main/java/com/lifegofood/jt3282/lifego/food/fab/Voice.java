package com.lifegofood.jt3282.lifego.food.fab;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.essay.EssayByClass;
import com.lifegofood.jt3282.lifego.food.userpage.EssayOwn;

import java.util.ArrayList;

/**
 * Created by jt3282 on 2017/12/26.
 */

public class Voice extends Activity {

    private String all,account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_layout);

        // Button button = (Button) findViewById(R.id.btn1);

        // button.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View v) {

        //透過 Intent 的方式開啟內建的語音辨識 Activity...
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話..."); //語音辨識 Dialog 上要顯示的提示文字

        startActivityForResult(intent, 1);
        //    }
        // });
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
                SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
                account = preferences.getString("USER_NAME","");
                chooseCommands(all);
            }
        }
        finish();
    }
    private void chooseCommands(String result){
        if(result.contains("切換")){
            MyWindowManager.removeBigWindow(Voice.this);
            MyWindowManager.removeSmallWindow(Voice.this);
            Intent i = new Intent(Voice.this, FloatWindowService.class);
            Voice.this.stopService(i);
            Intent intent = new Intent();
            intent.setClass(Voice.this, SelectFunction.class);
            startActivity(intent);
        }else if(result.contains("發文")){
            Intent intent = new Intent();
            intent.setClass(Voice.this, EssayVoice.class);
            startActivity(intent);
        }else if(result.contains("查詢")||result.contains("搜尋")){
            Intent intent = new Intent();
            intent.setClass(Voice.this, Third.class);
            startActivity(intent);
        }else if(result.contains("最新")){
            Intent intent = new Intent();
            intent.setClass(Voice.this, First.class);
            startActivity(intent);
        }else if(result.contains("附近")){
            Intent intent = new Intent();
            intent.setClass(Voice.this, NearlyFood.class);
            startActivity(intent);
        }else if(result.contains("用戶資訊")) {
            Intent intent = new Intent();
            intent.setClass(Voice.this, Forth.class);
            startActivity(intent);
        }else if(result.contains("文章")) {
            Intent intent = new Intent();
            intent.putExtra("account",account);
            intent.putExtra("key","no");
            intent.setClass(Voice.this,EssayOwn.class);
            startActivity(intent);
        }else if(result.contains("收藏")) {
            Intent intent = new Intent();
            intent.putExtra("account",account);
            intent.putExtra("key","yes");
            intent.setClass(Voice.this,EssayOwn.class);
            startActivity(intent);
        }else if(result.contains("輕食")||result.contains("侵蝕")) {
            DinnerIntent("輕食");
        }
        else if(result.contains("小吃")) {
            DinnerIntent("小吃");
        }
        else if(result.contains("鍋類")) {
            DinnerIntent("鍋類");
        }
        else if(result.contains("早午餐")) {
            DinnerIntent("早午餐");
        }
        else if(result.contains("下午茶點心")) {
            DinnerIntent("下午茶點心");
        }
        else if(result.contains("精緻")) {
            DinnerIntent("精緻");
        }
        else if(result.contains("麵食")) {
            DinnerIntent("麵食");
        }
        else if(result.contains("燒烤")) {
            DinnerIntent("燒烤");
        }
        else if(result.contains("日式")) {
            DinnerIntent("日式");
        }
        else if(result.contains("泰式")) {
            DinnerIntent("泰式");
        }
        else if(result.contains("港式")) {
            DinnerIntent("港式");
        }
        else if(result.contains("海鮮")) {
            DinnerIntent("海鮮");
        }
        else if(result.contains("熱炒")) {
            DinnerIntent("熱炒");
        }
        else if(result.contains("飲品")) {
            DinnerIntent("冰/飲品");
        }
        else if(result.contains("其他")) {
            DinnerIntent("其他");
        }else Toast.makeText(Voice.this, "主人~我聽不懂啦(⋟﹏⋞)", Toast.LENGTH_SHORT).show();
    }
    private void DinnerIntent(String s){
        Intent intent = new Intent();
        intent.putExtra("class",s);
        intent.setClass(Voice.this,EssayByClass.class);
        startActivity(intent);
    }
    //Light,Snack,Pot,BB,AftTee,Exquisite,Noodle,Roast,Japan,Thai,HK,SeaFood,Fried,Drink,Other;

}