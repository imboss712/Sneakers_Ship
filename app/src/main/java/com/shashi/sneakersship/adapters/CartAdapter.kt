package com.shashi.sneakersship.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shashi.sneakersship.R
import com.shashi.sneakersship.databinding.ItemCartBinding
import com.shashi.sneakersship.model.CartItem

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private lateinit var binding: ItemCartBinding
    private lateinit var context: Context

    private val differCallback = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((CartItem) -> Unit)? = null
    private var onCloseButtonClickListener: ((CartItem) -> Unit)? = null
    private var onPlusButtonClickListener: ((CartItem) -> Unit)? = null
    private var onMinusClickListener: ((CartItem) -> Unit)? = null

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    inner class CartViewHolder(itemView: ItemCartBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(item: CartItem) {
            binding.apply {
                Glide.with(itemView).load(item.sneaker.media.imageUrl).into(itemImage)
                itemTitle.text = item.sneaker.name
                itemPrice.text =
                    context.getString(R.string.item_price, item.sneaker.retailPrice.toString())
                Log.d("Shashi", "count: ${item.quantity}")
                quantityInput.text = item.quantity.toString()

                closeButton.setOnClickListener {
                    onCloseButtonClickListener?.let { it(item) }
                }

                plusButton.setOnClickListener {
                    onPlusButtonClickListener?.let { it(item) }
                }

                minusButton.setOnClickListener {
                    onMinusClickListener?.let { it(item) }
                }

                itemView.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (CartItem) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnCloseButtonClickListener(listener: (CartItem) -> Unit) {
        onCloseButtonClickListener = listener
    }

    fun setOnPlusButtonClickListener(listener: (CartItem) -> Unit) {
        onPlusButtonClickListener = listener
    }

    fun setOnMinusButtonClickListener(listener: (CartItem) -> Unit) {
        onMinusClickListener = listener
    }
}