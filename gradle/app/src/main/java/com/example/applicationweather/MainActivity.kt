package com.example.applicationweather

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import retrofit2.Callback
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationweather.API.RetrofitHelper
import com.example.applicationweather.Adapter.SearchCityAdapter
import com.example.applicationweather.Models.SearchListItem
import com.google.android.material.appbar.CollapsingToolbarLayout
import retrofit2.Call
import retrofit2.Response

const val extra_city_name = "EXTRA_CITY_NAME"

class MainActivity : AppCompatActivity() {

    //
    val apiKey = "5121c3e648f046d09df45242231002"
    lateinit var linearLayoutManager: LinearLayoutManager
    val customCityList = mutableListOf(
        SearchListItem(
            id = "1125257",
            name = "Mumbai",
            region = "Maharashtra",
            country = "India",
            lat = 18.98,
            lon = 72.83,
            url = "mumbai-maharashtra-india"
        ),
        SearchListItem(
            id = "1129302",
            name = "Pune",
            region = "Maharashtra",
            country = "India",
            lat = 18.53,
            lon = 73.87,
            url = "pune-maharashtra-india"
        ), SearchListItem(
            id = "1126581",
            name = "New Delhi",
            region = "Delhi",
            country = "India",
            lat = 28.6,
            lon = 77.2,
            url = "new-delhi-delhi-india"
        ), SearchListItem(
            id = "1120848",
            name = "Kolkata",
            region = "West Bengal",
            country = "India",
            lat = 22.57,
            lon = 88.37,
            url = "kolkata-west-bengal-india"
        )
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val typeface = ResourcesCompat.getFont(applicationContext, R.font.sans_bold)
        val typeface_2 =
            ResourcesCompat.getFont(applicationContext, R.font.sans_extrabold)
        val mainCollapsing = findViewById<CollapsingToolbarLayout>(R.id.main_collapsing)
        mainCollapsing.setCollapsedTitleTypeface(typeface_2)
        mainCollapsing.setExpandedTitleTypeface(typeface)

        var recyclerView: RecyclerView
        recyclerView = findViewById(R.id.city_rview)

        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


        var searchText: AppCompatEditText? = null
        searchText = findViewById(R.id.edtSearch)


        val searchCitiesAdapter = SearchCityAdapter(
            customCityList,
            object : SearchCityAdapter.ItemClickListener {
                override fun onItemClick(city: SearchListItem) {
                    val intent =
                        Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(extra_city_name, city.name)
                    startActivity(intent)
                }
            })

        var recyclerView_2: RecyclerView
        recyclerView_2 = findViewById(R.id.city_rview)
        recyclerView_2.adapter = searchCitiesAdapter

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 2) {
                    AlldataSearch(s)
                } else {

                    val searchCitiesAdapter = SearchCityAdapter(
                        customCityList,
                        object : SearchCityAdapter.ItemClickListener {
                            override fun onItemClick(city: SearchListItem) {
                                val intent =
                                    Intent(this@MainActivity, DetailActivity::class.java)
                                intent.putExtra(extra_city_name, city.name)
                                startActivity(intent)
                            }
                        })

                    var recyclerView: RecyclerView
                    recyclerView = findViewById(R.id.city_rview)
                    recyclerView.adapter = searchCitiesAdapter
                }

            }

            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
            }
        })
    }

    private fun AlldataSearch(s: CharSequence?) {


        val Api_interface = RetrofitHelper.create()

        Api_interface.searchLocation(apiKey, s.toString())
            .enqueue(object : Callback<List<SearchListItem>> {
                override fun onResponse(
                    call: Call<List<SearchListItem>>, response: Response<List<SearchListItem>>
                ) {
                    if (response.isSuccessful) {
                        var cityList = response.body()
                        if (cityList.isNullOrEmpty()) {

                        } else {
                            val searchCitiesAdapter = SearchCityAdapter(
                                cityList as MutableList<SearchListItem>,
                                object : SearchCityAdapter.ItemClickListener {
                                    override fun onItemClick(city: SearchListItem) {
                                        val intent =
                                            Intent(
                                                this@MainActivity,
                                                DetailActivity::class.java
                                            )
                                        intent.putExtra(extra_city_name, city.name)
                                        startActivity(intent)
                                    }
                                })

                            var recyclerView: RecyclerView
                            recyclerView = findViewById(R.id.city_rview)
                            recyclerView.adapter = searchCitiesAdapter
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Response Failure",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<SearchListItem>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Network Failure", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}