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
import com.lifegofood.jt3282.lifego.traffic.bus.RouteDetail;
import com.lifegofood.jt3282.lifego.traffic.bus.RouteList;
import com.lifegofood.jt3282.lifego.traffic.hsr.HSRSearchDetail;
import com.lifegofood.jt3282.lifego.traffic.ibus.IBusSearch_Detail;
import com.lifegofood.jt3282.lifego.traffic.railway.TrainSearchDetail;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.arrow_down_float;
import static android.R.drawable.arrow_up_float;

public class Like_route extends Activity {

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
        setContentView(R.layout.like_route);

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
        SharedPreferences sp = getSharedPreferences("mypref_busid", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String county = sp.getString("county"+i, "");
            String route = sp.getString("id"+i, "");
            String direction = sp.getString("direction"+i, "");

            item.put("county",county);
            item.put("route",route);
            item.put("direction",direction);
            myAdapter1.addItem(item);
        }
        rcv1.setAdapter(myAdapter1);
        rcv1.addItemDecoration(new DividerItemDecoration(Like_route.this, DividerItemDecoration.VERTICAL));
        rcv1.setLayoutManager(new LinearLayoutManager(Like_route.this));

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcv1.getVisibility()==View.GONE) {
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
        SharedPreferences sp = getSharedPreferences("mypref_ibusid", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String route = sp.getString("route"+i, "");
            String id = sp.getString("id"+i, "");
            String direction = sp.getString("direction"+i, "");

            item.put("route",route);
            item.put("id",id);
            item.put("direction",direction);
            myAdapter2.addItem(item);
        }
        rcv2.setAdapter(myAdapter2);
        rcv2.addItemDecoration(new DividerItemDecoration(Like_route.this, DividerItemDecoration.VERTICAL));
        rcv2.setLayoutManager(new LinearLayoutManager(Like_route.this));

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
        SharedPreferences sp = getSharedPreferences("mypref_trainid", MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("mypref_trainid_type", MODE_PRIVATE);
        SharedPreferences sp3 = getSharedPreferences("mypref_trainid_direction", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String id = sp.getString("id"+i, "");
            String type = sp2.getString("type"+i, "");
            String direction = sp3.getString("direction"+i, "");

            item.put("id",id);
            item.put("type",type);
            item.put("direction",direction);
            myAdapter3.addItem(item);
        }
        rcv3.setAdapter(myAdapter3);
        rcv3.addItemDecoration(new DividerItemDecoration(Like_route.this, DividerItemDecoration.VERTICAL));
        rcv3.setLayoutManager(new LinearLayoutManager(Like_route.this));

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
        SharedPreferences sp = getSharedPreferences("mypref_hsrid", MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("mypref_hsrid_direction", MODE_PRIVATE);
        SharedPreferences sp3 = getSharedPreferences("mypref_hsrid_way", MODE_PRIVATE);

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> item = new HashMap<String, String>();
            String id = sp.getString("id"+i, "");
            String direction = sp2.getString("direction"+i, "");
            String way = sp3.getString("way"+i, "");

            item.put("id",id);
            item.put("way",way);
            item.put("direction",direction);
            myAdapter4.addItem(item);
        }
        rcv4.setAdapter(myAdapter4);
        rcv4.addItemDecoration(new DividerItemDecoration(Like_route.this, DividerItemDecoration.VERTICAL));
        rcv4.setLayoutManager(new LinearLayoutManager(Like_route.this));

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
            if(!item.get("route").equals("")) list.add(item);
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
            holder.tv2.setText(list.get(position).get("route"));
            holder.tv3.setText(list.get(position).get("direction"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (list.get(position).get("county").equals("Taipei") || list.get(position).get("county").equals("NewTaipei")) {
                        intent.setClass(Like_route.this, RouteDetail.class);
                    } else intent.setClass(Like_route.this, RouteList.class);
                    intent.putExtra("location", list.get(position).get("county"));
                    intent.putExtra("route", list.get(position).get("route"));
                    intent.putExtra("direction", list.get(position).get("direction"));
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

            holder.tv1.setText(list.get(position).get("route"));
            holder.tv2.setText(list.get(position).get("id"));
            holder.tv3.setText(list.get(position).get("direction"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_route.this, IBusSearch_Detail.class);
                    intent.putExtra("route", list.get(position).get("route"));
                    intent.putExtra("subid", list.get(position).get("id"));
                    intent.putExtra("sub", list.get(position).get("direction"));
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

            holder.tv1.setText(list.get(position).get("type"));
            holder.tv2.setText(list.get(position).get("id"));
            holder.tv3.setText(list.get(position).get("direction"));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_route.this, TrainSearchDetail.class);
                    intent.putExtra("id",list.get(position).get("id"));
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
            if(!item.get("id").equals("")) list.add(item);
        }

        @Override
        public MyAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lr_rv,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter4.ViewHolder holder, final int position) {

            holder.tv1.setText(list.get(position).get("id"));
            holder.tv2.setText(list.get(position).get("direction"));
            if(list.get(position).get("way").equals("0")){
                holder.tv3.setText("下行");
            }else holder.tv3.setText("上行");
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Like_route.this, HSRSearchDetail.class);
                    intent.putExtra("id",list.get(position).get("id"));
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
