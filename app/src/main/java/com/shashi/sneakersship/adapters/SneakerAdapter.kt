package com.shashi.sneakersship.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shashi.sneakersship.R
import com.shashi.sneakersship.databinding.ItemSneakerBinding
import com.shashi.sneakersship.model.Sneaker

class SneakerAdapter : RecyclerView.Adapter<SneakerAdapter.SneakerViewHolder>() {

    private lateinit var binding: ItemSneakerBinding
    private lateinit var context: Context

    private val differCallback = object : DiffUtil.ItemCallback<Sneaker>() {
        override fun areItemsTheSame(oldItem: Sneaker, newItem: Sneaker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sneaker, newItem: Sneaker): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemSneakerBinding.inflate(inflater, parent, false)
        return SneakerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Sneaker) -> Unit)? = null

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class SneakerViewHolder(itemView: ItemSneakerBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(item: Sneaker) {
            binding.apply {
                Glide.with(itemView).load(item.media.imageUrl).into(itemImage)
                itemTitle.text = item.name
                itemPrice.text = context.getString(R.string.item_price, item.retailPrice.toString())

                itemView.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Sneaker) -> Unit) {
        onItemClickListener = listener
    }
}