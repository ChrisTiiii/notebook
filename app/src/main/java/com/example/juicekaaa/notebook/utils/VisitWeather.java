package com.example.juicekaaa.notebook.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.juicekaaa.notebook.bean.WeatherBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Juicekaaa on 2017/10/25.
 */

public class VisitWeather {
    private WeatherRestClient weatherRestClient = new WeatherRestClient();
    private Call<WeatherBean> call;
    private String tmp, cond, city;
    final String key = "e9b8f7aaf2df4d228fbda77f9109eb39";
    private Handler mHandler;
    Message msg;
    Bundle bundle;

    public VisitWeather(String city, Handler mHandler) {
        this.city = city;
        this.mHandler = mHandler;
    }


    //获取数据回调
    public void backData(String city) {
        call = weatherRestClient.getmIWeatherApi().getWheather(city, key);
        call.enqueue(new Callback<WeatherBean>() {
            @Override
            public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                setTmp(response.body().getHeWeather5().get(0).getNow().getTmp());
                setCond(response.body().getHeWeather5().get(0).getNow().getCond().getTxt());
                sendMessage();
            }

            @Override
            public void onFailure(Call<WeatherBean> call, Throwable t) {

            }
        });

    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public void sendMessage() {
        msg = new Message();
        msg.what = 101;
        bundle = new Bundle();
        bundle.putString("tmp", tmp);
        bundle.putString("cond", cond);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

}


