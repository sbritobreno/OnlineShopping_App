package com.brito.onlineshopping_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brito.onlineshopping_app.activities.CartActivity
import com.brito.onlineshopping_app.utils.productListFromTheApi
import kotlinx.android.synthetic.main.product_recycler_template.view.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class CartAdapter(private val ProductsInCart: ArrayList<Product>, private var clickListener: OnCartItemClickListener) : RecyclerView.Adapter<CartViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler_view_template, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val productInCart = ProductsInCart[position]
        holder.initialize(ProductsInCart[position],clickListener)

        holder.itemView.findViewById<ImageButton>(R.id.add_product).setOnClickListener{
            productInCart.quantity = productInCart.quantity?.plus(1)
            notifyItemChanged(position)
        }
        holder.itemView.findViewById<ImageButton>(R.id.remove_product).setOnClickListener{
            productInCart.quantity = productInCart.quantity?.minus(1)

            if(productInCart.quantity!! <= 0)
                ProductsInCart.removeAt(position)

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ProductsInCart.size
    }
}

class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val title: TextView = itemView.findViewById(R.id.title_cart_rView)
    private var price: TextView = itemView.findViewById(R.id.final_product_price_cart_rView)
    private var quantity: TextView = itemView.findViewById(R.id.quantity_cart_rView)
    private var image: ImageView = itemView.findViewById(R.id.image_cart_rView)

    fun initialize(ProductsInCart: Product, action:OnCartItemClickListener){

        var cartItemId = ProductsInCart.productId
        var cartItem = Products()

        for( p in productListFromTheApi){
            if(p.id == cartItemId)
                cartItem = p
        }

        title.text = cartItem.title
        price.text = cartItem.price?.times(ProductsInCart.quantity!!).toString()
        quantity.text = ProductsInCart.quantity.toString()

        val loadImageView = image
        Picasso.get()
            .load(cartItem.image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(loadImageView)

        itemView.setOnClickListener{
            action.onCartItemClick(ProductsInCart, adapterPosition)
        }
        itemView.findViewById<Button>(R.id.buy_btn_cart_rView).setOnClickListener{
            action.onBuyItemClick(ProductsInCart, adapterPosition)
        }
    }
}

interface OnCartItemClickListener{
    fun onCartItemClick(item: Product, position: Int)
    fun onBuyItemClick(item: Product, position: Int)
}