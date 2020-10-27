package com.example.recycleview.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycleview.R
import com.example.recycleview.data.Location
import java.lang.ref.WeakReference

class LocationAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val LOCATION_ITEM = 0
        val BUTTON_ITEM = 1
    }

    var isLoading: Boolean = false
        set(value) {
            field = value
            if (value) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        }

    interface Listenner {
        fun onButtonClick()
    }

    val list: ArrayList<Location> = ArrayList()
    var listenner: Listenner? = null
        set(value) {
            field = value
        }

    fun reloadView(newList: List<Location>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun add(newList: List<Location>) {
        list.addAll(newList)
        notifyItemRangeInserted(list.size - newList.size, newList.size)
    }

    fun show() {
        for (location in list) {
            Log.d("links", location.link)
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun getItemViewType(position: Int) =
        if (position < list.size) LOCATION_ITEM else BUTTON_ITEM

    inner class ItemHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val imageLocation = itemView.findViewById<ImageView>(R.id.image_location)
        val textViewName = itemView.findViewById<TextView>(R.id.textview_name)
        val textViewLagitude = itemView.findViewById<TextView>(R.id.textview_lagitude)
        val textViewLongitude = itemView.findViewById<TextView>(R.id.textview_longitude)
    }

    inner class ButtonHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var addButton: Button
        var listennerRef: WeakReference<Listenner>

        init {
            addButton = view.findViewById(R.id.button_addMore)
            listennerRef = WeakReference(listenner)
            addButton.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listennerRef.get()?.onButtonClick()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            LOCATION_ITEM -> {
                val locationView = inflater.inflate(R.layout.item_location, parent, false)
                ItemHolder(locationView)
            }
            else -> {
                val buttonView = inflater.inflate(R.layout.button, parent, false)
                ButtonHolder(buttonView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            LOCATION_ITEM -> {
                val location = list.get(position)
                (holder as ItemHolder).apply {
                    textViewName.text = location.name
                    textViewLagitude.text = location.lagitude
                    textViewLongitude.text = location.longitude
                }
                Glide.with(holder.imageLocation).load(location.link).into(holder.imageLocation)
            }
            else -> {
                (holder as ButtonHolder).addButton.visibility = View.VISIBLE
            }
        }
    }
}