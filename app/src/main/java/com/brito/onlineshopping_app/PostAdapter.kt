package com.brito.onlineshopping_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(val productModel: ArrayList<Models>, var clickListener: OnProductItemClickListener) : RecyclerView.Adapter<PostViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_recycler_template, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.initialize(productModel.get(position),clickListener)
    }

    override fun getItemCount(): Int {
        return productModel.size
    }

}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val title: TextView = itemView.findViewById(R.id.title)
    private val price: TextView = itemView.findViewById(R.id.price)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val image: ImageView = itemView.findViewById(R.id.image)

    fun initialize(productModel: Models, action:OnProductItemClickListener){
        title.text = productModel.title
        price.text = productModel.price
        description.text = productModel.description

        itemView.setOnClickListener{
            action.onItemClick(productModel, adapterPosition)
        }
    }
}

interface OnProductItemClickListener{
    fun onItemClick(item: Models, position: Int)
}