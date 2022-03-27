package com.app.topupmama.ui


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.topupmama.R
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity.Object
import com.app.topupmama.databinding.ActivityMainBinding
import com.app.topupmama.ui.adapters.WeatherAdapter
import com.app.topupmama.ui.weatherDetail.WeatherDetailActivity
import com.app.topupmama.utils.CheckInternet
import com.app.topupmama.utils.Constants
import com.app.topupmama.utils.Constants.CHANNEL_ID
import com.app.topupmama.utils.DataState
import com.app.topupmama.utils.dateFormater.FormatDate
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("InlinedApi")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WeatherAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: WeatherAdapter
    private val notificationId = 1
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createNotificationChannel()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setSupportActionBar(binding.actionBar)
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)



        fetchWeather()
        getPreviousFavorite()
    }

    private fun fetchWeather(){
        if (CheckInternet(this)){
            dialog.dismiss()
            subscribeObservers()
        }else{
            showErrorDialog(getString(R.string.problem_occured), getString(R.string.internet_issues), this){subscribeObservers()}
        }
    }

    private fun subscribeObservers(){
        val id = Constants.CITY_ID
        val unit = Constants.UNIT
        val key = getString(R.string.appid)

        viewModel.setStateEvent(MainStateEvent.GetWeatherEvents, id, unit, key)
        viewModel.dataState.observe(this, { dataState ->
            run {
                when (dataState) {
                    is DataState.success<WeatherRemoteEntity> -> {
                        displayProgressBar(false)
                        initAdapter(dataState.data.list)
                        adapter.filter("")
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        displayError(dataState.exception.message) { subscribeObservers() }
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.otherError -> {
                        displayProgressBar(false)
                        displayError(dataState.error) { subscribeObservers() }
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        return true
    }

    private fun displayError(message: String?, callback: () -> Unit){
        if (message != null){
            showErrorDialog(getString(R.string.problem_occured), message, this){callback()}
        }else{
            showErrorDialog(getString(R.string.problem_occured),
                getString(R.string.issues_message), this){callback()}
        }
    }

    private fun displayProgressBar(isDisplay: Boolean){
        binding.progressBar.visibility = if (isDisplay) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun  displayUI(weatherRemoteEntity: Object){
        //for date
        val date: Date = Calendar.getInstance().time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val strDate: String = dateFormat.format(date)
        val inFormat  = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val today = inFormat.parse(strDate)
        val outFormat = SimpleDateFormat("EE", Locale.ENGLISH)
        val goal = outFormat.format(today!!)

        // for time
        val time = Date(System.currentTimeMillis())
        val  timeFormat = (SimpleDateFormat("HH:mm aaa", Locale.ENGLISH).format(time))
        binding.textView3.text = "$goal  ${FormatDate.getFormattedFullDateString(strDate)}"
        binding.textView4.text = timeFormat
            binding.textView5.text = weatherRemoteEntity.main.temp.toString() + " \u2103"
            binding.textView2.text = weatherRemoteEntity.name


        if (timeFormat.contains("PM")){
            Glide.with(binding.root)
                .load(R.drawable.ic_night)
                .into(binding.imageView)
        }else{
            Glide.with(binding.root)
                .load(R.drawable.sunny)
                .into(binding.imageView)
        }

        binding.materialCardView.setOnClickListener {
            val mainIntent = Intent(this, WeatherDetailActivity::class.java)
            val dataBundle = Bundle()
            dataBundle.putSerializable(Constants.DETAIL_ITEM, weatherRemoteEntity)
            mainIntent.putExtras(dataBundle)
            startActivity(mainIntent)
        }

    }


    private fun initAdapter(item: List<Object>){
        binding.weatherRecyclerview.layoutManager = LinearLayoutManager(binding.root.context)
        adapter = WeatherAdapter(item, this, this)
        binding.weatherRecyclerview.adapter = adapter
    }

     fun updateFavorite(favoriteEntity: Object){
        viewModel.updateFavorite(favoriteEntity){
            if (it == null){
                binding.materialCardView.visibility = View.GONE
            }else{
                binding.materialCardView.visibility = View.VISIBLE
                displayUI(it)
            }
        }
    }

    private fun getPreviousFavorite(){
        viewModel.getFavorite {
            if (it == null) return@getFavorite
            binding.materialCardView.visibility = View.VISIBLE
            displayUI(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.getFavorite {
            if (it == null) return@getFavorite
            setUpNotification(it.name, it.weather[0].description, it.main.temp.toString() + " \u2103", it)
        }
    }

    override fun onResume() {
        super.onResume()
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }


    private fun showErrorDialog(titleMessage: String, descMessage: String, context: Context, retryMess: () -> Unit) {
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog_error)
        val dismiss = dialog.findViewById(R.id.btDismissCustomDialog) as Button
        val title = dialog.findViewById(R.id.title) as TextView
        val desc = dialog.findViewById(R.id.desc) as TextView
        val retry = dialog.findViewById(R.id.retryCustomDialog) as Button

        title.text = titleMessage
        desc.text = descMessage
        dismiss.setOnClickListener {
            dialog.dismiss()
        }
        retry.setOnClickListener {
            retryMess()
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onItemClick(position: Int, item: Object) {
        val mainIntent = Intent(this, WeatherDetailActivity::class.java)
        val dataBundle = Bundle()
        dataBundle.putSerializable(Constants.DETAIL_ITEM, item)
        mainIntent.putExtras(dataBundle)
        startActivity(mainIntent)
    }

     private fun setUpNotification(title: String, message: String, temp: String, item: Object){
        val resultIntent = Intent(this, WeatherDetailActivity::class.java)
        val dataBundle = Bundle()
        dataBundle.putSerializable(Constants.DETAIL_ITEM, item)
        resultIntent.putExtras(dataBundle)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,  PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(message)
            .setContentText(temp)
            .setColor(ContextCompat.getColor(this, R.color.background_color))
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(VISIBILITY_PRIVATE)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.top_channel)
            val descriptionText = getString(R.string.my_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}