package com.example.petshield

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.petshield.databinding.FragmentMyfoodBinding

class MyfoodFragment : DialogFragment() {

    private lateinit var binding: FragmentMyfoodBinding
    private lateinit var myfoodRVAdapter: MyfoodRVAdapter
    private var myfoodDatas = ArrayList<Food>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyfoodBinding.inflate(inflater, container, false)

        inputDummyFood()
        // Set up the RecyclerView adapter
        myfoodRVAdapter = MyfoodRVAdapter(myfoodDatas)
        myfoodRVAdapter.setItemClickListener(object : MyfoodRVAdapter.MyItemClickListener {
            override fun onSiteClick(food: Food) {
                // Handle item click here
                val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(food.url))
                startActivity(urlIntent)
            }
        })

        binding.myfoodRv.adapter = myfoodRVAdapter

        // 닫기 버튼 클릭 리스너 설정
        binding.myfoodClosedBtnIv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun inputDummyFood() {
        myfoodDatas.apply {
            add(Food(1, "게더 독 엔드레스 벨리 비건 레시피", 28000, "포메라니안", "전체", "9-13세", "전체", "게더", "https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(2, "닥터독 다이어트 사료", 32500, "리트리버", "전체", "1-4세", "전체", "닥터", "https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            add(Food(3, "닥터독 위장사료", 32500, "말티즈", "전체", "1-4세", "전체", "닥터", "https://bit.ly/3V0Zy6f", "", "https://share.pet-friends.co.kr/product/share?product_id=176749&next=https%3A%2F%2Fm.pet-friends.co.kr"))
            }
    }

    // Optional: To pass data to the fragment
    fun setData(foodList: List<Food>) {
        myfoodDatas.clear()
        myfoodDatas.addAll(foodList)
        myfoodRVAdapter.notifyDataSetChanged()
    }
}
