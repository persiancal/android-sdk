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
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.github.persiancal.sdklocal.LocalCalendarEvents
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_settings.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class LocalActivity : AppCompatActivity(), OnDayPickedListener {

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
            when (calendarType) {
                CalendarType.PERSIAN -> retrieveJalaliEvents(dayOnMonth, month)
                CalendarType.CIVIL -> retrieveGregorianEvents(dayOnMonth, month)
                CalendarType.HIJRI -> retrieveHijriEvents(dayOnMonth, month)
                else -> retrieveJalaliEvents(dayOnMonth, month)
            }
        }
    }

    private var calendarType: CalendarType = CalendarType.PERSIAN
    lateinit var localCalendarEvents: LocalCalendarEvents
    private val itemAdapter = ItemAdapter<EventItem>()
    private lateinit var fastAdapter: FastAdapter<EventItem>
    private lateinit var bottomSheetSettingsBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titleTextView.text = "Local Events"
        localCalendarEvents = LocalCalendarEvents.getInstance()
        setupBottomSheetSettings()
        calendarView.calendarType = CalendarType.PERSIAN
        calendarView.locale = Locale("fa")
        calendarView.pickType = PickType.SINGLE
        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL
        calendarView.onDayPickedListener = this
        handleBottomSheetEvents()
        setupRecyclerView()
    }

    private fun handleBottomSheetEvents() {
        gregorianRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarType = CalendarType.CIVIL
                calendarView.locale = Locale("en")
                calendarView.goto(CalendarFactory.newInstance(calendarType), false)
                hideBottomSheetSettings()
                itemAdapter.clear()
            }
        }
        jalaliRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarType = CalendarType.PERSIAN
                calendarView.locale = Locale("fa")
                calendarView.goto(CalendarFactory.newInstance(calendarType), false)
                hideBottomSheetSettings()
                itemAdapter.clear()
            }
        }
        hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarType = CalendarType.HIJRI
                calendarView.locale = Locale("ar")
                calendarView.goto(CalendarFactory.newInstance(calendarType), false)
                hideBottomSheetSettings()
                itemAdapter.clear()
            }
        }
    }

    private fun setupBottomSheetSettings() {
        settingsButton.setOnClickListener {
            bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        val settingsBottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet_settings) as View
        bottomSheetSettingsBehavior = BottomSheetBehavior.from(settingsBottomSheet)
        bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun hideBottomSheetSettings() {
        bottomSheetSettingsBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupRecyclerView() {
        fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.addEventHook(object : ClickEventHook<EventItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is EventItem.ViewHolder) {
                    viewHolder.sourceInfoButton
                } else {
                    null
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<EventItem>,
                item: EventItem
            ) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                if (!item.sources.isNullOrEmpty())
                    customTabsIntent.launchUrl(this@LocalActivity, Uri.parse(item.sources[0]))
                else
                    Toast.makeText(
                        this@LocalActivity,
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

    private fun retrieveHijriEvents(dayOnMonth: Int, month: Int) {
        if (localCalendarEvents.isHijriReady()) run {
            val hijriEvents = localCalendarEvents.getHijriEvents(dayOnMonth, month)
            for (item in hijriEvents!!) {
                val eventItem = EventItem(
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
                itemAdapter.add(eventItem)
            }
        }
    }

    private fun retrieveGregorianEvents(dayOnMonth: Int, month: Int) {
        if (localCalendarEvents.isGregorianReady()) run {
            val gregorianEvents = localCalendarEvents.getGregorianEvents(dayOnMonth, month)
            for (item in gregorianEvents!!) {
                val eventItem = EventItem(
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
                itemAdapter.add(eventItem)
            }
        }
    }

    private fun retrieveJalaliEvents(dayOnMonth: Int, month: Int) {
        if (localCalendarEvents.isJalaliReady()) run {
            val jalaliEvents = localCalendarEvents.getJalaliEvents(dayOnMonth, month)
            for (item in jalaliEvents!!) {
                val eventItem = EventItem(
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
                itemAdapter.add(eventItem)
            }
        }
    }
}
