package com.dreamer.expensesmoney.ui.DayTransaction

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dreamer.expensesmoney.R
import com.dreamer.expensesmoney.data.Transaction
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*


class DayTransactionAdapter(private val listener: (String) -> Unit) :
    ListAdapter<Transaction, DayTransactionAdapter.ViewHolder>(
        DiffCallback3()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition).date)
            }
        }

        fun bind(transaction: Transaction) {
            with(transaction) {
                transaction_name.text = transaction.name
                transaction_date.text = transaction.date
                transaction_amount.text = transaction.amount.toString()
                transaction_mode.isVisible = false


                if (transaction.plusMinus == 1) {

                    itemView.plus_minus.text = "+"
                    itemView.plus_minus.setTextColor(Color.parseColor("#ADFF2F"))
                    itemView.transaction_amount.setTextColor(Color.parseColor("#ADFF2F"))
                    itemView.type_view.setBackgroundColor(Color.parseColor("#ADFF2F"))

                } else if (transaction.plusMinus == 0) {

                    itemView.plus_minus.text = "-"
                    itemView.plus_minus.setTextColor(Color.parseColor("#ff726f"))
                    itemView.transaction_amount.setTextColor(Color.parseColor("#ff726f"))
                    itemView.type_view.setBackgroundColor(Color.parseColor("#ff726f"))

                }
                txtVieArrow_right.visibility = View.INVISIBLE
                dot.visibility = View.INVISIBLE
            }
        }
    }
}


class DiffCallback3 : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}