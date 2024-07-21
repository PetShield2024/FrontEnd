package com.example.petshield

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

        // 닫기 버튼 클릭 리스너 설정
        binding.myfoodClosedBtnIv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
