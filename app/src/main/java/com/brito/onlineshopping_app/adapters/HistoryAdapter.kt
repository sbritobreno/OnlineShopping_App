package com.brito.onlineshopping_app.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brito.onlineshopping_app.PurchasedCart
import com.brito.onlineshopping_app.R

class HistoryAdapter(private val historyList: ArrayList<PurchasedCart>, private var clickListener: OnHistoryItemClickListener) : RecyclerView.Adapter<HistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_recycler_template, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.initialize(historyList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val date: TextView = itemView.findViewById(R.id.date)
    private val productsQuantity: TextView = itemView.findViewById(R.id.productsQuantity)
    private val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)

    fun initialize(historyList: PurchasedCart, action: OnHistoryItemClickListener){

        date.text = historyList.date
        productsQuantity.text = historyList.products[0].quantity.toString()
        totalPrice.text = historyList.finalPrice.toString()

        itemView.setOnClickListener{
            action.onItemClick(historyList, adapterPosition)
        }
    }
}

interface OnHistoryItemClickListener{
    fun onItemClick(item: PurchasedCart, position: Int)
}