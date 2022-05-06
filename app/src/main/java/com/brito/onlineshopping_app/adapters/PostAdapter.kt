package com.brito.onlineshopping_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brito.onlineshopping_app.Products
import com.brito.onlineshopping_app.R
import kotlinx.android.synthetic.main.product_recycler_template.view.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class PostAdapter(private val productModel: ArrayList<Products>, private var clickListener: OnProductItemClickListener) : RecyclerView.Adapter<PostViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_recycler_template, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.initialize(productModel[position],clickListener)
        val loadImageView = holder.itemView.image
        Picasso.get()
            .load(productModel[position].image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(loadImageView)
    }

    override fun getItemCount(): Int {
        return productModel.size
    }
}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val title: TextView = itemView.findViewById(R.id.title)
    private val price: TextView = itemView.findViewById(R.id.price)

    fun initialize(productModel: Products, action: OnProductItemClickListener){
        title.text = productModel.title
        price.text = productModel.price.toString()

        itemView.setOnClickListener{
            action.onItemClick(productModel, adapterPosition)
        }
        itemView.findViewById<Button>(R.id.add_to_cart_btn_rview).setOnClickListener{
            action.onAddCartClick(productModel, adapterPosition)
        }
    }
}

interface OnProductItemClickListener{
    fun onItemClick(item: Products, position: Int)
    fun onAddCartClick(item: Products, position: Int)
}