package com.github.persiancal.android

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.github.persiancal.androidsdk.CalendarEvents
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), OnDayPickedListener {
    override fun onDayPicked(
        pickType: PickType,
        singleDay: PrimeCalendar?,
        startDay: PrimeCalendar?,
        endDay: PrimeCalendar?
    ) {
        if (pickType == PickType.SINGLE) {
            val dayOnMonth = singleDay!!.dayOfMonth
            val month = singleDay.month + 1
            itemAdapter.clear()
            if (calendarEvents.isReady()) run {
                val jalaliEvents = calendarEvents.getJalaliEvents(dayOnMonth, month)
                for (item in jalaliEvents!!) {
                    val jalaliEventItem = JalaliEventItem(
                        item.key,
                        item.calendar,
                        item.month,
                        item.sources,
                        item.year,
                        item.description_fa_IR,
                        item.title_fa_IR,
                        item.day,
                        item.holiday_Iran
                    )
                    itemAdapter.add(jalaliEventItem)
                }
            }
        }
    }

    lateinit var calendarEvents: CalendarEvents
    private val itemAdapter = ItemAdapter<JalaliEventItem>()
    private lateinit var fastAdapter: FastAdapter<JalaliEventItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarEvents = CalendarEvents.getInstance()
        setupCalendarView()
        setupRecyclerView()

    }

    private fun setupCalendarView() {
        // https://github.com/aminography/PrimeDatePicker

        calendarView.calendarType = CalendarType.PERSIAN
        calendarView.pickType = PickType.SINGLE
        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL
        calendarView.locale = Locale("fa")
        calendarView.onDayPickedListener = this
    }

    private fun setupRecyclerView() {
        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.addEventHook(object : ClickEventHook<JalaliEventItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is JalaliEventItem.ViewHolder) {
                    viewHolder.sourceInfoButton
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<JalaliEventItem>,
                item: JalaliEventItem
            ) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                if (!item.sources.isNullOrEmpty())
                    customTabsIntent.launchUrl(this@MainActivity, Uri.parse(item.sources[0]))
                else
                    Toast.makeText(
                        this@MainActivity,
                        "Source url not set",
                        Toast.LENGTH_LONG
                    ).show()
            }

        })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = fastAdapter
    }
}
