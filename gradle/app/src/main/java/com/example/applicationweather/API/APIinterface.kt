package com.example.applicationweather.API

import com.example.applicationweather.Models.DetailList_city
import com.example.applicationweather.Models.ForecastDetails
import com.example.applicationweather.Models.SearchListItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIinterface {

    @GET("search.json")
    fun searchLocation(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): Call<List<SearchListItem>>


    @GET("current.json")
    fun getDetailedWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("aqi") includeAqi: String
    ): Call<DetailList_city>

    @GET("forecast.json")
    fun getDetailedForecast(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String,
    ): Call<ForecastDetails>

}