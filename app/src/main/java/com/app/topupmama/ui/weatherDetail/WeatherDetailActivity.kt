package com.app.topupmama.ui.weatherDetail

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.topupmama.R
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.app.topupmama.databinding.ActivityWeatherDetailBinding
import com.app.topupmama.utils.Constants
import com.app.topupmama.utils.dateFormater.FormatDate
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class WeatherDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherDetailBinding
    private val notificationId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        detailItems()
        cancelNotification()
    }

    @SuppressLint("SetTextI18n")
    private fun detailItems(){
        val extras = intent.extras
        val item = extras!!.getSerializable(Constants.DETAIL_ITEM) as WeatherRemoteEntity.Object

        val date: Date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val strDate: String = dateFormat.format(date)
        val inFormat  = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val today = inFormat.parse(strDate)
        val outFormat = SimpleDateFormat("EE", Locale.ENGLISH)
        val goal = outFormat.format(today!!)


        val time = Date(System.currentTimeMillis())
        val  timeFormat = (SimpleDateFormat("HH:mm aaa", Locale.ENGLISH).format(time))
        binding.textView3.text = "$goal  ${FormatDate.getFormattedFullDateString(strDate)}"
        binding.textView4.text = timeFormat

        if (timeFormat.contains("PM")){
            Glide.with(binding.root)
                .load(R.drawable.ic_night)
                .into(binding.imageView)
        }else{
            Glide.with(binding.root)
                .load(R.drawable.sunny)
                .into(binding.imageView)
        }

        binding.textView5.text = item.name
        binding.textView2.text = item.weather[0].description
        binding.textView10.text = item.main.temp.toString() + " \u2103"
        binding.textView11.text = item.wind.speed.toString() + " " + "km/h"
        binding.textView13.text = item.main.humidity.toString() + "%"
        binding.textView15.text = item.visibility.toString() + " " + "km"
        binding.textView17.text = item.main.pressure.toString() + " " + "mb"
        binding.textView19.text = item.clouds.all.toString() + "\u00B0"
        binding.lat.text = "Lat: " + item.coord.lat
        binding.lon.text = "Lon: " + item.coord.lon

    }

    override fun onResume() {
        super.onResume()
        cancelNotification()
    }

    private fun cancelNotification(){
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}