package com.example.applicationweather.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationweather.Models.Hour
import com.example.applicationweather.R
import java.text.SimpleDateFormat
import java.util.*

class DailyForecastAdapter(
    private val dailyForecastList: List<Hour>,
    val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(hour: Hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_forcast_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dailyForecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyForecastList[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        var daily_forecast_item_date =
//            itemView.findViewById<TextView>(R.id.daily_forecast_item_date)
        var daily_forecast_item_time =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_time)
        var daily_forecast_item_temp =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_temp)
        var daily_forecast_item_img = itemView.findViewById<ImageView>(R.id.daily_forecast_item_img)
        var daily_forecast_item_condition =
            itemView.findViewById<TextView>(R.id.daily_forecast_item_condition)

        fun bind(hour: Hour) {
            daily_forecast_item_temp.text = hour.temp_c.toString() + "°C"
            daily_forecast_item_condition.text = hour.condition.text

            //-> get image code from url
            var code = hour.condition.icon.subSequence(41, 44).toString()
            var icon = getIcon(code)
            daily_forecast_item_img.setImageResource(icon)

            //-> set time
            val input = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
            val output = SimpleDateFormat("hh:mm aa", Locale.getDefault())
//            val date_output = SimpleDateFormat("dd/MM/YY", Locale.getDefault())
            try {
                var t: Date = input.parse(hour.time)
                daily_forecast_item_time.text = output.format(t)
//                daily_forecast_item_date.text = date_output.format(t)
            } catch (e: Exception) {
                //No Code
            }
            itemView.setOnClickListener {
                itemClickListener.onItemClick(hour)
            }
        }
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