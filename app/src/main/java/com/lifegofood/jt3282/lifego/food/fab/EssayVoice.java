package com.lifegofood.jt3282.lifego.food.fab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.EditActivity;

/**
 * Created by jt3282 on 2017/12/28.
 */

public class EssayVoice extends Activity {

    private String all,account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.essayvoice);

        list();
    }

    private void list(){
        final String[] dinner = {"輕食","小吃","鍋類","早午餐","下午茶點心","精緻","麵食","燒烤","日式","泰式","港式","海鮮","熱炒","冰/飲品","其他"};
        SharedPreferences preferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);  ;
        account = preferences.getString("USER_NAME","");
        AlertDialog.Builder dialog_list = new AlertDialog.Builder(this);
        dialog_list.setTitle("請選擇分類");
        dialog_list.setItems(dinner, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(EssayVoice.this, "你選的是" + dinner[which], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("account",account);
                intent.putExtra("class",dinner[which]);
                intent.setClass(EssayVoice.this, EditActivity.class);
                startActivity(intent);
                EssayVoice.this.finish();
            }
        });
        dialog_list.show();
    }
}
