// CallLogAdapter.kt
package com.clintmoyer.conversight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter(private val callLogs: List<CallEntry>) :
    RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(callLogs[position])
    }

    override fun getItemCount() = callLogs.size

    class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val durationTextView: TextView = itemView.findViewById(R.id.durationTextView)

        fun bind(callEntry: CallEntry) {
            numberTextView.text = "Number: ${callEntry.number}"
            typeTextView.text = "Type: ${callEntry.type}"
            dateTextView.text = "Date: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(callEntry.date))}"
            durationTextView.text = "Duration: ${callEntry.duration} seconds"
        }
    }
}

