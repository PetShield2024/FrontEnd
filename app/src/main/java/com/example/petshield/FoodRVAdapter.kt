package com.example.petshield

import FoodFragment
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petshield.databinding.ItemFoodBinding

class FoodRVAdapter(private var foods: List<Food>) : RecyclerView.Adapter<FoodRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onSiteClick(food: Food)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    private var filteredList: List<Food> = foods // 필터링된 데이터를 저장할 변수

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredList[position], mItemClickListener)
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateList(filteredList: List<Food>) {
        this.filteredList = filteredList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: Food, itemClickListener: MyItemClickListener) {
            with(binding) {
                itemFoodNameTv.text = food.foodName
                itemFoodPriceTv.text = "${food.price}원"
                itemFoodBrandTv.text = food.brand

                food.image?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .into(itemFoodImgIv)
                }

                itemFoodSiteIb.setOnClickListener {
                    itemClickListener.onSiteClick(food)
                }

                // TextView를 선택 상태로 설정하여 마퀴 효과 적용
                itemFoodNameTv.isSelected = true
            }
        }
    }
}
