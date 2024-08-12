package com.example.petshield

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.petshield.databinding.FragmentMyinfoBinding
import com.example.petshield.databinding.FragmentResultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyinfoFragment : Fragment()  {

    private lateinit var binding: FragmentMyinfoBinding

    override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyinfoBinding.inflate(inflater, container, false)

        // 강아지 프로필과 이미지 조회
        getDogProfile(1L) // 예시로 dogId를 1로 설정
        getDogImage(1L)   // 예시로 dogId를 1로 설정

        binding.myinfoEditIb.setOnClickListener {
            val intent = Intent(requireContext(), ModifyActivity::class.java)
            startActivity(intent)
        }
        return binding.root
}

    private fun getDogProfile(dogId: Long) {
        RetrofitClientApi.retrofitInterface.getDogHome(dogId).enqueue(object :
            Callback<ApiResponse<DogHomeResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogHomeResponse>>,
                response: Response<ApiResponse<DogHomeResponse>>
            ) {
                if (response.isSuccessful) {
                    val dogProfile = response.body()?.result
                    dogProfile?.let {
                        binding.myinfoProfile01Tv.text = "이름  :  ${it.dogName}"
                        binding.myinfoProfile02Tv.text = "생일  :  ${it.birth}"
                        binding.myinfoProfile03Tv.text = "종  :  ${it.breed}"
                        binding.myinfoProfile04Tv.text = "몸무게  :  ${it.weight} kg"
                        Toast.makeText(requireContext(), "Upload successful! ID: ${dogProfile.dogName}", Toast.LENGTH_LONG).show()

                    }
                }
                else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("UploadError", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(requireContext(), "Upload failed: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogHomeResponse>>, t: Throwable) {
                // 에러 처리
                Toast.makeText(requireContext(), "Upload fail!", Toast.LENGTH_LONG).show()
                Log.e("UploadFailure", "Upload error: ${t.message}", t)


            }
        })
    }

    private fun getDogImage(dogId: Long) {
        RetrofitClientApi.retrofitInterface.getDogImage(dogId).enqueue(object :
            Callback<ApiResponse<DogImageGetResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogImageGetResponse>>,
                response: Response<ApiResponse<DogImageGetResponse>>
            ) {
                if (response.isSuccessful) {
                    val dogImage = response.body()?.result
                    dogImage?.let {
                        Glide.with(this@MyinfoFragment)
                            .load(it.imageUrl)
                            .into(binding.myinfoProfileIv)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogImageGetResponse>>, t: Throwable) {
                // 에러 처리
            }
        })
    }
}