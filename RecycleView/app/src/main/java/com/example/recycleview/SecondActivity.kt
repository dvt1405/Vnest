package com.example.recycleview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recycleview.flick.FlickApi
import com.example.recycleview.data.Location
import com.example.recycleview.adapter.LocationAdapter
import com.example.recycleview.data.Page
import kotlinx.android.synthetic.main.activity_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SecondActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    LocationAdapter.Listenner {

    private val lagitude: String by lazy { intent.getStringExtra(MainActivity.EXTRA_TEXTLAGITUDE)!! }
    private val longitude: String by lazy { intent.getStringExtra(MainActivity.EXTRA_TEXTLONGITUDE)!! }
    private val name by lazy { intent.getStringExtra(MainActivity.EXTRA_TEXTNAME)!! }
    lateinit var rvLocation: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val locationAdapter: LocationAdapter by lazy { LocationAdapter(this) }
    private lateinit var page: Page
    var count = 1
    val response by lazy {
        Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        rvLocation = findViewById(R.id.rvLocation)
        locationAdapter.listenner = this
        rvLocation.adapter = locationAdapter
        rvLocation.layoutManager = LinearLayoutManager(parent)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener(this)
        callApi(lagitude, longitude) {
            page = it
            loadRecycleView(page, true)
        }
    }

    private fun callApi(lat: String, lon: String, call: (res: Page) -> Unit) {
        response.getPhotos(
            MainActivity.METHOD,
            MainActivity.API_KEY,
            lat,
            lon,
            "json",
            "1"
        ).enqueue(object : Callback<Page> {
            override fun onResponse(call: Call<Page>, response: Response<Page>) {
                val res = response.body() as Page
                call(res)
            }

            override fun onFailure(call: Call<Page>, t: Throwable) {
                Log.d("onFailure", t.toString())
            }
        })
    }

    private fun loadRecycleView(res: Page, addNew: Boolean = false) {
        val list = ArrayList<Location>()
        for (i in count..count + 19) {
            Log.e("Count",i.toString())
            list.add(Location(name, lagitude, longitude, res.photos.photo[i].getLink()))
            Log.d("size", list.size.toString())
        }
        if (addNew) {
            locationAdapter.reloadView(list)
        } else {
            locationAdapter.add(list)
        }
        locationAdapter.show()
    }

    override fun onRefresh() {
        callApi(lagitude, longitude) {
            count = 0
            page = it
            loadRecycleView(page, true)
            swipe_refresh_layout.isRefreshing = false
        }
    }

    override fun onButtonClick() {
        count += 20
        loadRecycleView(page)
    }

}