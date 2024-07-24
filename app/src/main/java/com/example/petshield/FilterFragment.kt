package com.example.petshield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.petshield.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    // 인터페이스 초기화
    private var dataPassListener: DataPassListener? = null

    // 스피너에서 선택된 값 저장할 변수들
    private var selectedAge = "전체"
    private var selectedSize = "전체"
    private var selectedVariety = "전체"
    private var selectedWeight = "전체"
    private var selectedBrand = "전체"
    private var selectedOrigin = "전체"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup your spinners and other UI elements here
        // Example setup for age spinner
        val ageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","1세 이하", "1-4세", "5-8세","9-13세","14세 이상"))
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterAgeSpinner.adapter = ageAdapter

        val sizeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","소형견", "중형견", "대형견"))
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterSizeSpinner.adapter = sizeAdapter

        val varietyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","포메라니안", "푸들", "말티즈","비숑","리트리버"))
        varietyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterVarietySpinner.adapter = varietyAdapter

        val weightAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","저체중", "정상", "비만"))
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterWeightSpinner.adapter = weightAdapter

        val brandAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","닥터", "게더", "로얄캐닌","더텐"))
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterBrandSpinner.adapter = brandAdapter

        val originAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("전체","국내", "프랑스", "캐나다", "미국","캐니소스","힐스"))
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.filterOriginSpinner.adapter = originAdapter

        // Example of handling apply button click
        binding.filterApplyBtn.setOnClickListener {
            // Apply 버튼 클릭 시 선택된 값들을 인터페이스를 통해 전달
            selectedAge = binding.filterAgeSpinner.selectedItem.toString()
            selectedSize = binding.filterSizeSpinner.selectedItem.toString()
            selectedVariety = binding.filterVarietySpinner.selectedItem.toString()
            selectedWeight = binding.filterWeightSpinner.selectedItem.toString()
            selectedBrand = binding.filterBrandSpinner.selectedItem.toString()
            selectedOrigin = binding.filterOriginSpinner.selectedItem.toString()


            dataPassListener?.onDataPass(selectedAge, selectedSize, selectedVariety, selectedWeight, selectedBrand, selectedOrigin)
            dismiss()
        }

        // Example of handling reset button click
        binding.filterResetBtn.setOnClickListener {
            // Reset 버튼 클릭 시 선택된 값을 초기화
            selectedAge = "전체"
            selectedSize = "전체"
            selectedVariety = "전체"
            selectedWeight = "전체"
            selectedBrand = "전체"
            selectedOrigin = "전체"

            // 스피너를 초기 선택 상태로 설정
            binding.filterAgeSpinner.setSelection(0)
            binding.filterSizeSpinner.setSelection(0)
            binding.filterVarietySpinner.setSelection(0)
            binding.filterWeightSpinner.setSelection(0)
            binding.filterBrandSpinner.setSelection(0)
            binding.filterOriginSpinner.setSelection(0)


            // foodFragment에도 초기화된 값을 전달할 수 있도록 인터페이스 호출
            dataPassListener?.onDataPass(
                selectedAge,
                selectedSize,
                selectedVariety,
                selectedWeight,
                selectedBrand,
                selectedOrigin
            )
        }
    }

    // 인터페이스 설정 메소드
    fun setDataPassListener(listener: DataPassListener) {
        this.dataPassListener = listener
    }
}
