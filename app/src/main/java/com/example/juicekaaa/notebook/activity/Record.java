package com.example.juicekaaa.notebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juicekaaa.notebook.InitLocation;
import com.example.juicekaaa.notebook.MainActivity;
import com.example.juicekaaa.notebook.R;
import com.example.juicekaaa.notebook.database.DatabaseHelper;
import com.example.juicekaaa.notebook.mode.IndexMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Juicekaaa on 2017/10/25.
 */

public class Record extends AppCompatActivity {
    private LinearLayout delete, save, back;
    private TextView tvAddress;
    private Spinner spinner;
    private InitLocation initLocation;
    private EditText edContent, edTitle;
    private IndexMode indexMode = null;
    private int _id = 0;

    private List<IndexMode> list = null;
    private DatabaseHelper databaseHelper = null;

    private List<String> listSpinner = new ArrayList<>();
    private ArrayAdapter<String> arr_adapter;
    private String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.record);
        initLocation = new InitLocation(this);
        initView();
        howIn();
        Log.e("asd", nowTime());

    }

    private void initView() {
        delete = (LinearLayout) findViewById(R.id.delete);
        save = (LinearLayout) findViewById(R.id.save);
        back = (LinearLayout) findViewById(R.id.back);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        edTitle = (EditText) findViewById(R.id.ed_title);
        edContent = (EditText) findViewById(R.id.ed_content);
        spinner = (Spinner) findViewById(R.id.record_spinner);

        setSpinnerData();

        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        setClick();

    }

    private void setClick() {
        //设置spinner点击事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = listSpinner.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //点击保存
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(edTitle.getText().toString().trim()))
                    Toast.makeText(Record.this, "标题不能为空哦", Toast.LENGTH_SHORT).show();
                else {
                    if (_id == 0) {
                        databaseHelper = new DatabaseHelper(Record.this);
                        databaseHelper.getWritableDatabase();
                        IndexMode indexMode = new IndexMode();
                        indexMode.setTime(nowTime());
                        indexMode.setTitle(edTitle.getText().toString());
                        indexMode.setContent(edContent.getText().toString());
                        indexMode.setAddress(initLocation.mLocationClient.getLastKnownLocation().getAddress());
                        indexMode.setType(type);
                        databaseHelper.insertData(indexMode);
                        databaseHelper.close();
                        setResult(101);
                        finish();
                    } else {
                        databaseHelper = new DatabaseHelper(Record.this);
                        databaseHelper.getWritableDatabase();
                        indexMode = new IndexMode();
                        indexMode.setTitle(edTitle.getText().toString());
                        indexMode.setContent(edContent.getText().toString());
                        indexMode.set_id(_id);
                        databaseHelper.updateData(indexMode);
                        setResult(102);
                        finish();
                    }
                }

            }
        });

        //设置删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_id != 0) {
                    new SweetAlertDialog(Record.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("真的要把我删除吗?")
                            .setContentText("删了我就没了哦!")
                            .setCancelText("再想想!")
                            .setConfirmText("立马删!")
                            .showCancelButton(true)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    databaseHelper = new DatabaseHelper(Record.this);
                                    databaseHelper.getWritableDatabase();
                                    databaseHelper.deleteIndex(_id);
                                    setResult(103);
                                    finish();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                } else
                    new SweetAlertDialog(Record.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("错误")
                            .setContentText("您还没有创建本文档哦!")
                            .show();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //获取当前时间
    private String nowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);

    }

    //判断是怎么进入这个界面的
    public void howIn() {
        Intent intent = getIntent();
        switch (intent.getIntExtra("record", 101)) {
            case 101:
                tvAddress.setText(initLocation.mLocationClient.getLastKnownLocation().getAddress());
                break;
            case 102:
                databaseHelper = new DatabaseHelper(this);
                databaseHelper.getWritableDatabase();
                indexMode = databaseHelper.oneData(intent.getIntExtra("_id", 0));
                _id = intent.getIntExtra("_id", 0);
                edTitle.setText(indexMode.getTitle());
                edContent.setText(indexMode.getContent());
                Log.e("aqqqqq", indexMode.getContent());
                tvAddress.setText(indexMode.getAddress());
                break;
        }
    }

    //设置spinner
    private void setSpinnerData() {
        listSpinner.add("默认");
        listSpinner.add("生活");
        listSpinner.add("情感");
        listSpinner.add("旅游");
        listSpinner.add("计划");
    }


}
