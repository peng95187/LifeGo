package com.lifegofood.jt3282.lifego.traffic.minelike;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifegofood.jt3282.lifego.R;
import com.lifegofood.jt3282.lifego.traffic.bus.StopDetail;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRStationSearch;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusStop_Result;
import com.lifegofood.jt3282.lifego.traffic.railway.RWDetail;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.arrow_down_float;
import static android.R.drawable.arrow_up_float;

public class Like_stop extends Activity {

    RelativeLayout rl1,rl2,rl3,rl4;
    ImageView im1,im2,im3,im4;
    RecyclerView rcv1,rcv2,rcv3,rcv4;
    MyAdapter1 myAdapter1;
    MyAdapter2 myAdapter2;
    MyAdapter3 myAdapter3;
    MyAdapter4 myAdapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_stop);

        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl3 = (RelativeLayout) findViewById(R.id.rl3);
        rl4 = (RelativeLayout) findViewById(R.id.rl4);

        im1 = (ImageView) findViewById(R.id.im1);
        im2 = (ImageView) findViewById(R.id.im2);
        im3 = (ImageView) findViewById(R.id.im3);
        im4 = (ImageView) findViewById(R.id.im4);

        rcv1 = (RecyclerView) findViewById(R.id.rcv1);
        rcv2 = (RecyclerView) findViewById(R.id.rcv2);
        rcv3 = (RecyclerView) findViewById(R.id.rcv3);
        rcv4 = (RecyclerView) findViewById(R.id.rcv4);

        setrcv1();
        setrcv2();
        setrcv3();
        setrcv4();
    }
    private void setrcv1(){
        //rcv1 setadapter
        myAdapter1 = null;
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        myAdapter1 = new MyAdapter1(list);
        SharedPreferences sp = getSharedPreferences("mypref_busstop", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String stop = sp.getString("stop"+i, "");
            String county = sp.getString("county"+i, "");
            String name = sp.getString("name"+i, "");

            item.put("county",county);
            item.put("stop",stop);
            item.put("name",name);
            myAdapter1.addItem(item);
        }
        rcv1.setAdapter(myAdapter1);
        rcv1.addItemDecoration(new DividerItemDecoration(Like_stop.this, DividerItemDecoration.VERTICAL));
        rcv1.setLayoutManager(new LinearLayoutManager(Like_stop.this));

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcv1.getVisibility()== View.GONE) {
                    im1.setImageResource(arrow_up_float);
                    rcv1.setVisibility(View.VISIBLE);
                }else{
                    im1.setImageResource(arrow_down_float);
                    rcv1.setVisibility(View.GONE);
                }
            }
        });
    }
    private void setrcv2(){
        //rcv1 setadapter
        myAdapter2 = null;
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        myAdapter2 = new MyAdapter2(list);
        SharedPreferences sp = getSharedPreferences("mypref_ibusstop", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String stopname = sp.getString("name"+i, "");
            String id = sp.getString("id"+i, "");

            item.put("name",stopname);
            item.put("id",id);
            myAdapter2.addItem(item);
        }
        rcv2.setAdapter(myAdapter2);
        rcv2.addItemDecoration(new DividerItemDecoration(Like_stop.this, DividerItemDecoration.VERTICAL));
        rcv2.setLayoutManager(new LinearLayoutManager(Like_stop.this));

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcv2.getVisibility()==View.GONE) {
                    im2.setImageResource(arrow_up_float);
                    rcv2.setVisibility(View.VISIBLE);
                }else{
                    im2.setImageResource(arrow_down_float);
                    rcv2.setVisibility(View.GONE);
                }
            }
        });
    }
    private void setrcv3(){
        //rcv1 setadapter
        myAdapter3 = null;
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        myAdapter3 = new MyAdapter3(list);
        SharedPreferences sp = getSharedPreferences("mypref_trainstop", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String id = sp.getString("id"+i, "");
            String stop = sp.getString("stop"+i, "");

            item.put("id",id);
            item.put("stop",stop);
            myAdapter3.addItem(item);
        }
        rcv3.setAdapter(myAdapter3);
        rcv3.addItemDecoration(new DividerItemDecoration(Like_stop.this, DividerItemDecoration.VERTICAL));
        rcv3.setLayoutManager(new LinearLayoutManager(Like_stop.this));

        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcv3.getVisibility()==View.GONE) {
                    im3.setImageResource(arrow_up_float);
                    rcv3.setVisibility(View.VISIBLE);
                }else{
                    im3.setImageResource(arrow_down_float);
                    rcv3.setVisibility(View.GONE);
                }
            }
        });
    }
    private void setrcv4(){
        //rcv1 setadapter
        myAdapter4 = null;
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        myAdapter4 = new MyAdapter4(list);
        SharedPreferences sp = getSharedPreferences("mypref_hsrstop", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String stop = sp.getString("stop"+i, "");
            String id = sp.getString("id"+i, "");
            String date = sp.getString("date"+i, "");

            item.put("stop",stop);
            item.put("id",id);
            item.put("date",date);
            myAdapter4.addItem(item);
        }
        rcv4.setAdapter(myAdapter4);
        rcv4.addItemDecoration(new DividerItemDecoration(Like_stop.this, DividerItemDecoration.VERTICAL));
        rcv4.setLayoutManager(new LinearLayoutManager(Like_stop.this));

        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcv4.getVisibility()==View.GONE) {
                    im4.setImageResource(arrow_up_float);
                    rcv4.setVisibility(View.VISIBLE);
                }else{
                    im4.setImageResource(arrow_down_float);
                    rcv4.setVisibility(View.GONE);
                }
            }
        });
    }
    public class MyAdapter1 extends RecyclerView.Adapter<MyAdapter1.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter1(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            if(!item.get("stop").equals("")) list.add(item);
        }

        @Override
        public MyAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter1.ViewHolder holder, final int position) {

            holder.tv1.setText(list.get(position).get("county"));
            holder.tv2.setText(list.get(position).get("stop"));
            holder.tv3.setText(list.get(position).get("name"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(Like_stop.this, StopDetail.class);
                    intent.putExtra("county", list.get(position).get("county"));
                    intent.putExtra("stopuid", list.get(position).get("stop"));
                    intent.putExtra("stopname", list.get(position).get("name"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv1, tv2, tv3;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
    public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter2(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            if(!item.get("id").equals("")) list.add(item);
        }

        @Override
        public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter2.ViewHolder holder, final int position) {

            holder.tv1.setText(list.get(position).get("id"));
            holder.tv2.setText(list.get(position).get("name"));
            holder.tv3.setText("");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_stop.this, IBusStop_Result.class);
                    intent.putExtra("stopid", list.get(position).get("id"));
                    intent.putExtra("stopname", list.get(position).get("name"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv1, tv2, tv3;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
    public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter3(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            if(!item.get("id").equals("")) list.add(item);
        }

        @Override
        public MyAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter3.ViewHolder holder, final int position) {

            holder.tv1.setText(list.get(position).get("stop"));
            holder.tv2.setText("");
            holder.tv3.setText("");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_stop.this, RWDetail.class);
                    intent.putExtra("stationID",list.get(position).get("id"));
                    intent.putExtra("station",list.get(position).get("stop"));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv1, tv2, tv3;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
    public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder>{

        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();

        public MyAdapter4(ArrayList<HashMap<String,String>> newlist) {
            // TODO 自动生成的构造函数存根
            list = newlist;
        }
        public void addItem(HashMap<String, String> item) {
            if(!item.get("stop").equals("")) list.add(item);
        }

        @Override
        public MyAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter4.ViewHolder holder, final int position) {

            holder.tv1.setText(list.get(position).get("stop"));
            holder.tv2.setText("");
            holder.tv3.setText("");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_stop.this, HSRStationSearch.class);
                    intent.putExtra("stationID",list.get(position).get("id"));
                    intent.putExtra("saveTime",list.get(position).get("date"));
                    intent.putExtra("station",list.get(position).get("stop"));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return list==null?0:list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv1, tv2, tv3;
            public LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);
                tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                ll = (LinearLayout) itemView.findViewById(R.id.ll);
            }
        }
    }
}

