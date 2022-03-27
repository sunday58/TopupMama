package com.app.topupmama.ui.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.topupmama.R
import com.app.topupmama.apiSource.responseEntity.WeatherRemoteEntity.Object
import com.app.topupmama.databinding.WeatherListItemBinding
import com.app.topupmama.ui.MainActivity
import com.app.topupmama.utils.dateFormater.FormatDate
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeatherAdapter (private  val listItem: List<Object>, listener: OnItemClickListener, private val activity: MainActivity):
    RecyclerView.Adapter<WeatherAdapter.WeatherViewModel>(){

    private var listener: OnItemClickListener? = null

    init {
        this.listener = listener
    }

    private val items = ArrayList<Object>()

    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(listItem)
        } else {
            text = text.lowercase()
            for (item in listItem) {
                if (item.name.lowercase().contains(text)
                ) {
                    items.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewModel {
        val binding = WeatherListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewModel, position: Int) {
        with(holder){
            with(items[position]){

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
                binding.textView5.text = main.temp.toString() + " \u2103"
                binding.textView2.text = name


                if (timeFormat.contains("PM")){
                    Glide.with(itemView)
                        .load(R.drawable.ic_night)
                        .into(binding.imageView)
                }else{
                    Glide.with(itemView)
                        .load(R.drawable.sunny)
                        .into(binding.imageView)
                }

                binding.favorite.setOnClickListener {
                    activity.updateFavorite(items[position])
                }

                itemView.setOnClickListener {
                    listener?.onItemClick(position, items[position])
                }
            }
        }
    }

    override fun getItemCount() = items.size


    interface OnItemClickListener{
        fun onItemClick(position: Int, item: Object)

    }


    class WeatherViewModel(val binding: WeatherListItemBinding):
        ViewHolder(binding.root)

}