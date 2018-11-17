package com.lifegofood.jt3282.lifego.traffic.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lifegofood.jt3282.lifego.R;

public class Transfer_Select extends Activity {

    private Button tratohsr,hsrtotra,tratotra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_select);

        tratohsr = (Button) findViewById(R.id.tratohsr);
        hsrtotra = (Button) findViewById(R.id.hsrtotra);
        tratotra = (Button) findViewById(R.id.tratotra);

        tratohsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transfer_Select.this,Tftratohsr_direction.class);
                startActivity(intent);
            }
        });
        hsrtotra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transfer_Select.this,Tfhsrtotra_direction.class);
                startActivity(intent);
            }
        });
        tratotra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transfer_Select.this,Tftratotra_direction.class);
                startActivity(intent);
            }
        });
    }
}
