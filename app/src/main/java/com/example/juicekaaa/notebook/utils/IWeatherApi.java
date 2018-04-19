package com.example.juicekaaa.notebook.utils;

import com.example.juicekaaa.notebook.bean.WeatherBean;

import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Juicekaaa on 2017/10/24.
 */

public interface IWeatherApi {
    @GET("now?")
    Call<WeatherBean> getWheather(@Query("city") String city, @Query("key") String key);

}
