package com.example.petshield

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.petshield.databinding.FragmentResultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultFragment : Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val dogId: Long = 1 // Replace with actual dogId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)

        // Load obesity image
        loadObesityImage(dogId)

        binding.resultNearMapIb.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, MapFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.resultRetryIb.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, CameraFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun loadObesityImage(dogId: Long) {
        RetrofitClientApi.retrofitInterface.getObesityImage(dogId)
            .enqueue(object : Callback<ApiResponse<ObesityGetImageResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<ObesityGetImageResponse>>,
                    response: Response<ApiResponse<ObesityGetImageResponse>>
                ) {
                    if (response.isSuccessful) {
                        val obesityImageUrl = response.body()?.result?.obesityImage
                        obesityImageUrl?.let {
                            // Load image into ImageView using Glide
                            Glide.with(this@ResultFragment)
                                .load(it)
                                .into(binding.resultSelectedIv)
                        } ?: run {
                            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("ResultFragment", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<ObesityGetImageResponse>>, t: Throwable) {
                    Log.e("ResultFragment", "Failure: ${t.message}")
                }
            })
    }
}
