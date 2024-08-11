package com.example.petshield

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petshield.databinding.ItemMyfoodBinding

class MyfoodRVAdapter(private var foods: List<Food>) : RecyclerView.Adapter<MyfoodRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onSiteClick(food: Food)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setItemClickListener(listener: MyItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyfoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food, mItemClickListener)
    }

    inner class ViewHolder(private val binding: ItemMyfoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food, itemClickListener: MyItemClickListener) {
            with(binding) {
                itemFoodNameTv.text = food.foodName
                itemFoodPriceTv.text = "${food.price}원"

                itemFoodSiteIb.setOnClickListener {
                    itemClickListener.onSiteClick(food)
                }

                // TextView를 선택 상태로 설정하여 마퀴 효과 적용
                itemFoodNameTv.isSelected = true
            }
        }
    }
}
