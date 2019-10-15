package com.github.persiancal.android

import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

data class JalaliEventItem(
    val key: Long?,
    val calendar: List<String?>? = null,
    val month: Int? = null,
    val sources: List<String?>? = null,
    val year: Int? = null,
    val description_fa_IR: String? = null,
    val title_fa_IR: String? = null,
    val day: Int? = null,
    val holiday_Iran: List<String>? = null
) : AbstractItem<JalaliEventItem.ViewHolder>() {
    override val layoutRes: Int
        get() = R.layout.jalali_event_item

    override val type: Int
        get() = R.id.fastadapter_item


    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<JalaliEventItem>(view) {
        var titleTextView: TextView = view.findViewById(R.id.titleTextView)
        var descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        override fun bindView(item: JalaliEventItem, payloads: MutableList<Any>) {
            titleTextView.text = item.title_fa_IR
            descriptionTextView.text = item.description_fa_IR
        }

        override fun unbindView(item: JalaliEventItem) {
            titleTextView.text = null
            descriptionTextView.text = null
        }
    }
}