package com.example.applicationweather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationweather.API.RetrofitHelper
import com.example.applicationweather.Adapter.DailyForecastAdapter
import com.example.applicationweather.extra_city_name
import com.example.applicationweather.Models.DetailList_city
import com.example.applicationweather.Models.ForecastDetails
import com.example.applicationweather.Models.Hour
import com.example.applicationweather.R
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val apiKey = "5121c3e648f046d09df45242231002"
        val aqi = "yes"
        val days = 3
        val alerts = "no"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val cityName = intent.getStringExtra(extra_city_name)

        val Api_interface = RetrofitHelper.create()


        val detail_typeface =
            ResourcesCompat.getFont(applicationContext, R.font.sans_semibold)
        val detail_typeface_2 =
            ResourcesCompat.getFont(applicationContext, R.font.sans_extrabold)
        val detail_Collapsing = findViewById<CollapsingToolbarLayout>(R.id.detailAc_collapsing)
        detail_Collapsing.setCollapsedTitleTypeface(detail_typeface_2)
        detail_Collapsing.setExpandedTitleTypeface(detail_typeface)


        var city_temp = findViewById<TextView>(R.id.city_Temp)
        var city_condition = findViewById<TextView>(R.id.city_condition)

        var detail_city_last_updated = findViewById<TextView>(R.id.detail_city_last_updated)
        var detail_city_country = findViewById<TextView>(R.id.detail_city_country)
        var detail_city_curr_img = findViewById<ImageView>(R.id.detail_city_curr_img)

        var detail_city_wind_speed = findViewById<TextView>(R.id.detail_city_wind_speed)
        var detail_city_wind_direction = findViewById<TextView>(R.id.detail_city_wind_direction)
        var detail_city_air_preassure = findViewById<TextView>(R.id.detail_city_air_preassure)

        var detail_city_humidity = findViewById<TextView>(R.id.detail_city_humidity)
        var detail_city_precip = findViewById<TextView>(R.id.detail_city_precip)
        var detail_city_visibility = findViewById<TextView>(R.id.detail_city_visibility)

        var detail_city_sunrise = findViewById<TextView>(R.id.detail_city_sunrise)
        var detail_city_sunset = findViewById<TextView>(R.id.detail_city_sunset)



        Api_interface.getDetailedWeather(apiKey, cityName.toString(), aqi)
            .enqueue(object : Callback<DetailList_city> {

                override fun onResponse(
                    call: Call<DetailList_city>, response: Response<DetailList_city>
                ) {
                    var res = response.body()
                    if (res != null) {

                        detail_Collapsing.title = cityName
                        city_temp.text = "${res.current.temp_c}°C"
//                        city_condition.text = res.current.condition.text

                        detail_city_country.text = res.location.country
//                        detail_city_last_updated.text = res.current.last_updated
//                        var code = res.current.condition.icon.subSequence(41, 44).toString()
//                        var icon = getIcon(code)
//                        detail_city_curr_img.setImageResource(icon)


                        detail_city_wind_speed.text = res.current.wind_kph.toString() + " km/h"
//                        detail_city_wind_direction.text = res.current.wind_dir
                        detail_city_air_preassure.text = res.current.pressure_mb.toString() + " mb"

                        detail_city_humidity.text = res.current.humidity.toString() + " g.m-3"
                        detail_city_precip.text = res.current.precip_mm.toString() + "mm"
                        detail_city_visibility.text = res.current.vis_km.toString() + " km"

                    } else {
                        Toast.makeText(this@DetailActivity, "Response Failure", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


                override fun onFailure(call: Call<DetailList_city>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }

            })



        Api_interface.getDetailedForecast(apiKey, cityName.toString(), days, aqi, alerts)
            .enqueue(object : Callback<ForecastDetails> {
                override fun onResponse(
                    call: Call<ForecastDetails>, response: Response<ForecastDetails>
                ) {
                    var res = response.body()
                    if (res != null) {
                        detail_city_sunrise.text = res.forecast.forecastday[0].astro.sunrise
                        detail_city_sunset.text = res.forecast.forecastday[0].astro.sunset

                        var mainList: List<Hour> = res.forecast.forecastday[0].hour
                        var tom_mainList: List<Hour> = res.forecast.forecastday[1].hour
                        var ovtom_mainList: List<Hour> = res.forecast.forecastday[2].hour


                        if (!(mainList.isEmpty())) {
                            val dailyForecastAdapter = DailyForecastAdapter(mainList,
                                object : DailyForecastAdapter.ItemClickListener {
                                    @SuppressLint("SetTextI18n")
                                    override fun onItemClick(hour: Hour) {

                                        val builder =
                                            AlertDialog.Builder(this@DetailActivity)
                                        val popview: View =
                                            layoutInflater.inflate(R.layout.dialouge_box, null)
                                        builder.setView(popview)

                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
                                            hour.temp_c.toString() + "°C"

//                                        var code = hour.condition.icon.subSequence(41, 44)
//                                            .toString()
//                                        var icon = getIcon(code)
//                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
//                                            .setImageResource(icon)

//                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
//                                            hour.condition.text.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
                                            hour.wind_kph.toString() + " km/h"

                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
                                            hour.pressure_mb.toString() + " mb"

                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
                                            hour.humidity.toString() + " g.m-3"

//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
//                                            hour.wind_dir.toString()

//                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
//                                            hour.cloud.toString() + " oktas"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
//                                            hour.uv.toString()

                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
                                            hour.precip_mm.toString() + "mm"

                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
                                            hour.vis_km.toString() + " km"

//                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
//                                            hour.gust_kph.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
//                                            hour.wind_degree.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
//                                            hour.heatindex_c.toString()
                                        val input = SimpleDateFormat(
                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
                                        )
                                        val output =
                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
                                        val date_output =
                                            SimpleDateFormat("dd/mm/yy", Locale.getDefault())
                                        try {
//                                            var t: Date = input.parse(hour.time)
//                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
//                                                date_output.format(t)
//                                                    .toString() + " " + output.format(t).toString()
                                        } catch (e: Exception) {
                                            //No Code
                                        }

//                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "No"
//                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "No"
                                        val popdialog = builder.create()
                                        popdialog.show()

                                        val close: ImageView =
                                            popview.findViewById(R.id.dlg_city_close)
                                        close.setOnClickListener {
                                            popdialog.dismiss()
                                        }
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.detail_city_recyclerview)
                            recyclerView.adapter = dailyForecastAdapter
                        }

//                        //add tomorrow's forecast
//                        if (!(tom_mainList.isEmpty())) {
//                            val dailyForecastAdapter = DailyForecastAdapter(tom_mainList,
//                                object : DailyForecastAdapter.ItemClickListener {
//                                    override fun onItemClick(hour: Hour) {
//
//                                        val builder =
//                                            androidx.appcompat.app.AlertDialog.Builder(this@DetailActivity)
//                                        val popview: View =
//                                            layoutInflater.inflate(R.layout.dialouge_box, null)
//                                        builder.setView(popview)
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
//                                            hour.temp_c.toString() + "°C"
//
//                                        var code = hour.condition.icon.subSequence(41, 44)
//                                            .toString()
//                                        var icon = getIcon(code)
//                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
//                                            .setImageResource(icon)
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
//                                            hour.condition.text.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
//                                            hour.wind_kph.toString() + " km/h"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
//                                            hour.pressure_mb.toString() + " mb"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
//                                            hour.humidity.toString() + " g.m-3"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
//                                            hour.wind_dir.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
//                                            hour.cloud.toString() + " oktas"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
//                                            hour.uv.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
//                                            hour.precip_mm.toString() + "mm"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
//                                            hour.vis_km.toString() + " km"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
//                                            hour.gust_kph.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
//                                            hour.wind_degree.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
//                                            hour.heatindex_c.toString()
//                                        val input = SimpleDateFormat(
//                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
//                                        )
//                                        val output =
//                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
//                                        val date_output =
//                                            SimpleDateFormat("dd/mm/yy", Locale.getDefault())
//                                        try {
//                                            var t: Date = input.parse(hour.time)
//                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
//                                                date_output.format(t)
//                                                    .toString() + " " + output.format(t).toString()
//                                        } catch (e: Exception) {
//                                            //No Code
//                                        }
//
//                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "No"
//                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "No"
//                                        val popdialog = builder.create()
//                                        popdialog.show()
//
//                                        val close: ImageView =
//                                            popview.findViewById(R.id.dlg_city_close)
//                                        close.setOnClickListener {
//                                            popdialog.dismiss()
//                                        }
//                                    }
//                                })
//
//                            var recyclerView: RecyclerView
//                            recyclerView = findViewById(R.id.detail_city_tomm_recyclerview)
//                            recyclerView.adapter = dailyForecastAdapter
//                        }


//                        if (!(ovtom_mainList.isEmpty())) {
//                            val dailyForecastAdapter = DailyForecastAdapter(ovtom_mainList,
//                                object : DailyForecastAdapter.ItemClickListener {
//                                    override fun onItemClick(hour: Hour) {
//
//                                        val builder =
//                                            androidx.appcompat.app.AlertDialog.Builder(this@DetailActivity)
//                                        val popview: View =
//                                            layoutInflater.inflate(R.layout.dialouge_box, null)
//                                        builder.setView(popview)
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_Temp).text =
//                                            hour.temp_c.toString() + "°C"
//
//                                        var code = hour.condition.icon.subSequence(41, 44)
//                                            .toString()
//                                        var icon = getIcon(code)
//                                        popview.findViewById<ImageView>(R.id.dlg_city_curr_img)
//                                            .setImageResource(icon)
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_condition).text =
//                                            hour.condition.text.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_speed).text =
//                                            hour.wind_kph.toString() + " km/h"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_air_preassure).text =
//                                            hour.pressure_mb.toString() + " mb"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_humidity).text =
//                                            hour.humidity.toString() + " g.m-3"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_direction).text =
//                                            hour.wind_dir.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_cloud).text =
//                                            hour.cloud.toString() + " oktas"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_uv_index).text =
//                                            hour.uv.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_precip).text =
//                                            hour.precip_mm.toString() + "mm"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_visibility).text =
//                                            hour.vis_km.toString() + " km"
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_gust).text =
//                                            hour.gust_kph.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_wind_degree).text =
//                                            hour.wind_degree.toString()
//
//                                        popview.findViewById<TextView>(R.id.dlg_city_heat_ind).text =
//                                            hour.heatindex_c.toString()
//                                        val input = SimpleDateFormat(
//                                            "yyyy-MM-dd hh:mm", Locale.getDefault()
//                                        )
//                                        val output =
//                                            SimpleDateFormat("hh:mm aa", Locale.getDefault())
//                                        val date_output =
//                                            SimpleDateFormat("dd/MM/YY", Locale.getDefault())
//                                        try {
//                                            var t: Date = input.parse(hour.time)
//                                            popview.findViewById<TextView>(R.id.dlg_city_close_time).text =
//                                                date_output.format(t)
//                                                    .toString() + " " + output.format(t).toString()
//                                        } catch (e: Exception) {
//                                            //No Code
//                                        }
//
//                                        if (hour.will_it_rain == 1) popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_rain).text =
//                                            "No"
//                                        if (hour.will_it_snow == 1) popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "Yes"
//                                        else popview.findViewById<TextView>(R.id.dlg_will_it_snow).text =
//                                            "No"
//                                        val popdialog = builder.create()
//                                        popdialog.show()
//
//                                        val close: ImageView =
//                                            popview.findViewById(R.id.dlg_city_close)
//                                        close.setOnClickListener {
//                                            popdialog.dismiss()
//                                        }
//                                    }
//                                })
//
//                            var recyclerView: RecyclerView
//                            recyclerView = findViewById(R.id.detail_city_ovtomm_recyclerview)
//                            recyclerView.adapter = dailyForecastAdapter
//                        }

                    } else {
                        Toast.makeText(this@DetailActivity, "Response Failure", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ForecastDetails>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun getIcon(code: String): Int {
        val map = when (code) {
            "113" -> R.drawable.sunny
            "338" -> R.drawable.sunnycloudy
            "350" -> R.drawable.sunnycloudy
            "368" -> R.drawable.sunnycloudy
            "371" -> R.drawable.snowy
            "227" -> R.drawable.snowy
            "374" -> R.drawable.snowy
            "377" -> R.drawable.snowy
            "386" -> R.drawable.rainthunder
            "389" -> R.drawable.rainthunder
            "392" -> R.drawable.rainthunder
            "395" -> R.drawable.rainthunder
            "116" -> R.drawable.sunnycloudy
            "179" -> R.drawable.sunnycloudy
            "323" -> R.drawable.sunnycloudy
            "335" -> R.drawable.sunnycloudy
            "329" -> R.drawable.sunnycloudy
            "119" -> R.drawable.cloudy
            "185" -> R.drawable.cloudy
            "326" -> R.drawable.cloudy
            "332" -> R.drawable.cloudy
            "122" -> R.drawable.cloudy
            "143" -> R.drawable.cloudy
            "200" -> R.drawable.rainthunder
            "299" -> R.drawable.sunnyrainy
            "305" -> R.drawable.sunnyrainy
            "293" -> R.drawable.sunnyrainy
            "353" -> R.drawable.sunnyrainy
            "365" -> R.drawable.sunnyrainy
            "356" -> R.drawable.sunnyrainy
            "359" -> R.drawable.sunnyrainy
            "176" -> R.drawable.sunnyrainy
            "362" -> R.drawable.sunnyrainy
            "377" -> R.drawable.sunnyrainy
            "182" -> R.drawable.sunnyrainy
            "248" -> R.drawable.pressure
            "260" -> R.drawable.pressure
            "230" -> R.drawable.pressure
            "311" -> R.drawable.wind
            "314" -> R.drawable.wind
            "263" -> R.drawable.rainy
            "296" -> R.drawable.rainy
            "302" -> R.drawable.rainy
            "317" -> R.drawable.rainy
            "320" -> R.drawable.rainy
            "308" -> R.drawable.rainy
            "266" -> R.drawable.rainy
            "281" -> R.drawable.rainy
            "284" -> R.drawable.rainy
            else -> R.drawable.sunnycloudy
        }
        return map
    }
}