package com.example.moneymate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.R
import com.example.moneymate.model.Transaction
import com.example.moneymate.utils.AppConstants
import com.example.moneymate.utils.CurrencyHelper

class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private val items = mutableListOf<Transaction>()

    fun submitList(data: List<Transaction>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemTitle: TextView = itemView.findViewById(R.id.tvItemTitle)
        private val tvItemCategoryDate: TextView = itemView.findViewById(R.id.tvItemCategoryDate)
        private val tvItemAmount: TextView = itemView.findViewById(R.id.tvItemAmount)
        private val tvItemType: TextView = itemView.findViewById(R.id.tvItemType)

        fun bind(item: Transaction) {
            tvItemTitle.text = item.title
            tvItemCategoryDate.text = "${item.category} • ${item.date}"
            tvItemAmount.text = CurrencyHelper.formatToRupiah(item.amount)
            tvItemType.text = item.type

            val amountColor = if (item.type == AppConstants.TYPE_INCOME) {
                ContextCompat.getColor(itemView.context, R.color.income_green)
            } else {
                ContextCompat.getColor(itemView.context, R.color.expense_red)
            }

            tvItemAmount.setTextColor(amountColor)
            tvItemType.setBackgroundColor(amountColor)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}