package com.github.persiancal.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.github.persiancal.androidsdk.CalendarEvents
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
            if (calendarEvents.isReady()) run {
                val jalaliEvents = calendarEvents.getJalaliEvents(dayOnMonth, month)
                Log.d("jalaliEvents", jalaliEvents!!.size.toString())
            }
        }
    }

    lateinit var calendarEvents: CalendarEvents
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalendarView()
        calendarEvents = CalendarEvents.getInstance()
    }

    private fun setupCalendarView() {
        // https://github.com/aminography/PrimeDatePicker

        calendarView.calendarType = CalendarType.PERSIAN
        calendarView.pickType = PickType.SINGLE
        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL
        calendarView.locale = Locale("fa")
        calendarView.onDayPickedListener = this
    }
}
