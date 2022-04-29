package com.brito.onlineshopping_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brito.onlineshopping_app.activities.productListFromTheApi
import kotlinx.android.synthetic.main.product_recycler_template.view.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class CartAdapter(private val ProductsInCart: ArrayList<Cart>, private var clickListener: OnCartItemClickListener) : RecyclerView.Adapter<CartViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler_view_template, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.initialize(ProductsInCart[position],clickListener)
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

    fun initialize(ProductsInCart: Cart, action:OnCartItemClickListener){
        var cartItemId = ProductsInCart.products[0].productId
        var cartItem = Products()


        for( p in productListFromTheApi){
            if(p.id == cartItemId)
                cartItem = p
        }

        title.text = cartItem.title
        price.text = cartItem.price?.times(ProductsInCart.products[0].quantity!!).toString()
        quantity.text = ProductsInCart.products[0].quantity.toString()

        val loadImageView = image
        Picasso.get()
            .load(cartItem.image)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .into(loadImageView)

        itemView.setOnClickListener{
            action.onCartItemClick(ProductsInCart, adapterPosition)
        }
    }
}

interface OnCartItemClickListener{
    fun onCartItemClick(item: Cart, position: Int)
}