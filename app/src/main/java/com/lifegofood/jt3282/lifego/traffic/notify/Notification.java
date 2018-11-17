package com.lifegofood.jt3282.lifego.traffic.notify;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lifegofood.jt3282.lifego.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Notification extends Activity {

    RecyclerView rv;
    MyAdapter myAdapter;
    SharedPreferences sp = null,sp2 = null,sp3 = null;
    SharedPreferences.Editor spe = null,spe2 = null,spe3 = null;
    LinearLayout add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_notification);

        rv = (RecyclerView) findViewById(R.id.notify_rv);
        add = (LinearLayout) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification.this,Notification_add.class);
                startActivity(intent);
                finish();
            }
        });
        sp = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE);
        spe = getSharedPreferences("mypref_hsrnotify", MODE_PRIVATE).edit();
        sp2 = getSharedPreferences("mypref_tranotify", MODE_PRIVATE);
        spe2 = getSharedPreferences("mypref_tranotify", MODE_PRIVATE).edit();
        sp3 = getSharedPreferences("mypref_selfnotify", MODE_PRIVATE);
        spe3 = getSharedPreferences("mypref_selfnotify", MODE_PRIVATE).edit();

        setData();
    }
    private void setData(){
        myAdapter = null;
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        myAdapter = new MyAdapter(list);
        setRvHsr(myAdapter);
        setRvTra(myAdapter);
        setRvSelf(myAdapter);
        rv.setAdapter(myAdapter);
        rv.addItemDecoration(new DividerItemDecoration(Notification.this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(Notification.this));
    }

    private void setRvHsr(MyAdapter adapter){
        for(int i = 0;i < 5;i++){
            if(!sp.getString("type"+i,"").equals("")&&sp.getString("type"+i,"")!=null){
                HashMap<String, String> item = new HashMap<String, String>();
                String type = sp.getString("type"+i, "");
                String info = sp.getString("info"+i, "");
                String time = sp.getString("time"+i, "");
                String arr_time = sp.getString("arr_time"+i, "");

                item.put("type",type);
                item.put("info",info);
                item.put("no", String.valueOf(i));
                item.put("time",time);
                item.put("arr_time",arr_time);
                adapter.addItem(item);
            }
        }
    }
    private void setRvTra(MyAdapter adapter){

        for(int i = 0;i < 5;i++){
            if(!sp2.getString("type"+i,"").equals("")&&sp2.getString("type"+i,"")!=null){
                HashMap<String, String> item = new HashMap<String, String>();
                String type = sp2.getString("type"+i, "");
                String info = sp2.getString("info"+i, "");
                String time = sp2.getString("time"+i, "");
                String arr_time = sp2.getString("arr_time"+i, "");

                item.put("type",type);
                item.put("info",info);
                item.put("no", String.valueOf(i));
                item.put("time",time);
                item.put("arr_time",arr_time);
                adapter.addItem(item);
            }
        }

    }
    private void setRvSelf(MyAdapter adapter){
        for(int i = 0;i < 5;i++){
            if(!sp3.getString("type"+i,"").equals("")&&sp3.getString("type"+i,"")!=null){
                HashMap<String, String> item = new HashMap<String, String>();
                String type = sp3.getString("type"+i, "");
                String info = sp3.getString("info"+i, "");
                String time = sp3.getString("time"+i, "");
                String pick = sp3.getString("pick"+i, "");
                String arr_time = sp3.getString("arr_time"+i, "");

                item.put("type",type);
                item.put("no", String.valueOf(i));
                item.put("info",info);
                item.put("time",time);
                item.put("pick",pick);
                item.put("arr_time",arr_time);
                adapter.addItem(item);
            }
        }
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            list.add(item);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {

            holder.type.setText(list.get(position).get("type")+"   "+list.get(position).get("arr_time"));
            holder.info.setText(list.get(position).get("info"));
            if(list.get(position).get("type").equals("市區公車")||list.get(position).get("type").equals("公路客運")){
                if(list.get(position).get("time").equals("")){
                    holder.time.setText(list.get(position).get("pick").replaceAll(","," ")+" 提醒");
                }else{
                    holder.time.setText(list.get(position).get("time")+" 提醒");
                }
            }else{
                holder.time.setText(list.get(position).get("time")+" 提醒");
            }

            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!list.get(position).get("type").equals("")&&
                            (!list.get(position).get("type").equals("市區公車")||list.get(position).get("type").equals("公路客運"))){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Notification.this);
                        dialog.setTitle("變更時間");
                        if(list.get(position).get("time").equals("10分鐘前")) {
                            dialog.setMessage("預設提醒為 10 分鐘前");
                        }else dialog.setMessage("目前為 "+list.get(position).get("time")+" 分鐘前");
                        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //not to do
                            }

                        });
                        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                AlertDialog.Builder editDialog = new AlertDialog.Builder(Notification.this);
                                editDialog.setTitle("時間設定 ( 分鐘 )");

                                final EditText editText = new EditText(Notification.this);
                                editText.setSingleLine(true);
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                                editText.setText(list.get(position).get("time").replace("分鐘前",""));
                                editDialog.setView(editText, 10, 0, 10, 0);

                                editDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        int num = Integer.parseInt(editText.getText().toString())*1;
                                        if(list.get(position).get("type").equals("高鐵")) {
                                            spe.remove("time" + list.get(position).get("no"));
                                            spe.putString("time" + list.get(position).get("no"), num +"分鐘前");
                                            spe.commit();
                                        }else if(list.get(position).get("type").equals("台鐵")){
                                            spe2.remove("time" + list.get(position).get("no"));
                                            spe2.putString("time" + list.get(position).get("no"), num+"分鐘前");
                                            spe2.commit();
                                        }
                                        setData();
                                    }
                                });
                                editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        //...
                                    }
                                });
                                editDialog.show();
                            }
                        });
                        dialog.show();
                    }else{
                        if(!list.get(position).get("type").equals("市區公車")&&!list.get(position).get("type").equals("公路客運"))
                        Toast.makeText(Notification.this, "無此提醒", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.dis_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Notification.this);
                    dialog.setTitle("取消設定");
                    dialog.setMessage("");
                    dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //not to do
                        }

                    });
                    dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if(list.get(position).get("type").equals("高鐵")){
                                spe.remove("type"+list.get(position).get("no"));
                                spe.remove("info"+list.get(position).get("no"));
                                spe.remove("time"+list.get(position).get("no"));
                                spe.remove("arr_time"+list.get(position).get("no"));
                                spe.commit();
                            }else if(list.get(position).get("type").equals("台鐵")){
                                spe2.remove("type"+list.get(position).get("no"));
                                spe2.remove("info"+list.get(position).get("no"));
                                spe2.remove("time"+list.get(position).get("no"));
                                spe2.remove("arr_time"+list.get(position).get("no"));
                                spe2.commit();
                            }else if(list.get(position).get("type").equals("市區公車")
                                    ||list.get(position).get("type").equals("公路客運")){
                                spe3.remove("type"+list.get(position).get("no"));
                                spe3.remove("info"+list.get(position).get("no"));
                                spe3.remove("time"+list.get(position).get("no"));
                                spe3.remove("pick"+list.get(position).get("no"));
                                spe3.remove("arr_time"+list.get(position).get("no"));
                                spe3.commit();
                            }
                            setData();
                        }
                    });
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView type, info, time;
            public ImageButton dis_btn;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                info = (TextView) itemView.findViewById(R.id.info);
                type = (TextView) itemView.findViewById(R.id.type);
                time = (TextView) itemView.findViewById(R.id.time);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
                dis_btn = (ImageButton) itemView.findViewById(R.id.dis_btn);
            }
        }
    }
}
