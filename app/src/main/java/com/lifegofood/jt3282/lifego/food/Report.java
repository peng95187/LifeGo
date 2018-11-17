package com.lifegofood.jt3282.lifego.food;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.food.edit.DownloadImg;

public class Report extends Activity {

    private EditText editText;
    private Button send;
    private String _code;
    private String UPDATE_URL = "http://lifego7777.000webhostapp.com/excute_good.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        _code = getIntent().getStringExtra("report");
        editText = (EditText)findViewById(R.id.edit_report);
        send = (Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = editText.getText().toString();
                if(edit==null||edit.equals("")){
                    Toast.makeText(Report.this, "請描述情況 謝謝", Toast.LENGTH_SHORT).show();
                }else if(edit.contains(";")||edit.contains("'")||edit.contains("")) {
                    Toast.makeText(Report.this, "非法字元", Toast.LENGTH_SHORT).show();
                }else{
                        if(edit.length()>50){
                            Toast.makeText(Report.this, "字數不得超過50", Toast.LENGTH_SHORT).show();
                        }else{
                            send(_code,edit);
                            Toast.makeText(Report.this, "已送出", Toast.LENGTH_SHORT).show();
                        }
                }

            }
        });
    }
    private void send(final String code, final String edit){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = DownloadImg.executeQuery(UPDATE_URL,"INSERT INTO report(essay_code,report_comment) value('"+code+"','"+edit+"')");

            }
        }).start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            finish();
        }
        return true;
    }
}
