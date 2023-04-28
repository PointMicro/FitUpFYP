package com.example.fypfinal

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fypfinal.CalendarAdapter.OnItemListener


class CalendarViewHolder(itemView: View, onItemListener: OnItemListener) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val dayOfMonth: TextView
    private val onItemListener: OnItemListener

    init {
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, dayOfMonth.text as String)
    }
}