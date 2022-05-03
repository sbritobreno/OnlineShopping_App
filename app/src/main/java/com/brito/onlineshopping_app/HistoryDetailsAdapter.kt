package com.brito.onlineshopping_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.history_details_recycler_template.view.*
import kotlinx.android.synthetic.main.product_recycler_template.view.*

class HistoryDetailsAdapter(private val historyDetailsList: ArrayList<Products>, private var clickListener: OnHistoryDetailsItemClickListener) : RecyclerView.Adapter<HistoryDetailsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_details_recycler_template, parent, false)
        return HistoryDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryDetailsViewHolder, position: Int) {
        holder.initialize(historyDetailsList[position],clickListener)
        val loadImageView = holder.itemView.image_historyDetails_rView
        Picasso.get()
            .load(historyDetailsList[position].image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(loadImageView)
    }

    override fun getItemCount(): Int {
        return historyDetailsList.size
    }
}

class HistoryDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val name: TextView = itemView.findViewById(R.id.productName_historyDetails_rView)

    fun initialize(historyDetailsList: Products, action:OnHistoryDetailsItemClickListener){

        name.text = historyDetailsList.title

        itemView.setOnClickListener{
            action.onItemClick(historyDetailsList, adapterPosition)
        }
    }
}

interface OnHistoryDetailsItemClickListener{
    fun onItemClick(item: Products, position: Int)
}