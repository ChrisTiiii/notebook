package com.example.juicekaaa.notebook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.juicekaaa.notebook.activity.Record;
import com.example.juicekaaa.notebook.adapter.IndexAdapter;
import com.example.juicekaaa.notebook.database.DatabaseHelper;
import com.example.juicekaaa.notebook.mode.IndexMode;
import com.example.juicekaaa.notebook.utils.VisitWeather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvCity, tvWheather;
    private ImageView imgAdd;
    private Spinner spinner;
    private String tmp, cond;
    //初始化定位类
    private InitLocation initLocation;
    private RecyclerView mRecyclerView;

    private Handler handler;
    private RecyclerView.LayoutManager mLayoutManager;
    private IndexAdapter mAdapter = null;
    private List<IndexMode> mList = null;
    //查询天气
    private VisitWeather visitWeather;

    private DatabaseHelper databaseHelper = null;

    //spinner数据
    private List<String> listSpinner = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;

    private final int addBack = 101;
    private final int viewBack = 102;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();
        setClick();
    }


    private void initView() {
        tvCity = (TextView) findViewById(R.id.city);
        tvWheather = (TextView) findViewById(R.id.wheather);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgAdd = (ImageView) findViewById(R.id.index_add);

        //spinner操作：
        spinner = (Spinner) findViewById(R.id.index_spinner);
        setSpinnerData();


        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);

        setData("默认");
        //初始化定位
        initLocation = new InitLocation(this);
        tvCity.setText(initLocation.mLocationClient.getLastKnownLocation().getCity());
        //初始化天气
        initUtils(initLocation.mLocationClient.getLastKnownLocation().getCity());


        //设置recyclerview的线性布局
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new IndexAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                MainActivity.this, LinearLayoutManager.HORIZONTAL, 1, R.color.gray));

    }
    //初始化天气
    private void initUtils(String city) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 101:
                        tmp = msg.getData().getString("tmp");
                        cond = msg.getData().getString("cond");
                        tvWheather.setText(" " + tmp + "℃" + " " + cond);
                        break;

                }
            }
        };
        visitWeather = new VisitWeather(city, handler);
        visitWeather.backData(city);

    }


    //设置点击事件
    private void setClick() {
        //添加
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("record", addBack);
                intent.putExtra("address", initLocation.mLocationClient.getLastKnownLocation().getAddress());
                intent.setClass(MainActivity.this, Record.class);
                startActivityForResult(intent, addBack);
            }
        });
        //item点击
        mAdapter.setOnItemClick(new IndexAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("record", viewBack);
                intent.putExtra("_id", mList.get(position).get_id());
                Log.e("asd", String.valueOf(mList.get(position).get_id()));
                intent.setClass(MainActivity.this, Record.class);
                startActivityForResult(intent, viewBack);
            }

        });
        //spinner设置点击事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setData(listSpinner.get(position));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //初始化数据
    public void setData(String type) {
        if (mList == null)
            mList = new ArrayList<>();
        mList.clear();
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        databaseHelper.searchIndex(mList, type);
        databaseHelper.close();
    }

    //设置spinner
    private void setSpinnerData() {
        listSpinner.add("默认");
        listSpinner.add("生活");
        listSpinner.add("情感");
        listSpinner.add("旅游");
        listSpinner.add("计划");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        spinner.setSelection(0);
        setData("默认");
        mAdapter.notifyDataSetChanged();
    }
}
