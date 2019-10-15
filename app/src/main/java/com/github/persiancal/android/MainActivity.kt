package com.github.persiancal.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.github.persiancal.androidsdk.CalendarEvents
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
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
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = fastAdapter
    }
}
