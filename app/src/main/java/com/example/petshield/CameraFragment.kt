package com.example.petshield

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.petshield.databinding.FragmentCameraBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class CameraFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
        private const val REQUEST_PERMISSION_CODE = 100
    }

    private lateinit var binding: FragmentCameraBinding
    private var selectedImageUri: Uri? = null
    private val dogId: Long = 1 // Replace with actual dogId


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        // 이미지 복원
        // Load obesity image
        loadObesityImage(1L)

        // 갤러리 열기
        binding.testAlbumIb.setOnClickListener {
            openGallery()
        }

        // 카메라 열기
        binding.testRetryIb.setOnClickListener {
            openCamera()
        }

        // 이미지 검사 시작
        binding.testStartTestIb.setOnClickListener {
            selectedImageUri?.let { uri ->
                uploadImage(uri)
            } ?: run {
                 Toast.makeText(requireContext(), "Please select an image first.", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    data?.data?.let { uri ->
                        selectedImageUri = uri
                        binding.testSelectedIv.setImageURI(uri)

                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        selectedImageUri = getImageUri(requireContext(), it)
                        binding.testSelectedIv.setImageBitmap(it)
                    }
                }
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "PetShield_Image",
            null
        )
        return Uri.parse(path ?: "")
    }


    private fun uploadImage(uri: Uri) {
        val file = File(getRealPathFromURI(uri)!!)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val call = RetrofitClientApi.retrofitInterface.uploadObesityImage(dogId, body)
        call.enqueue(object : Callback<ApiResponse<ObesityImageResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<ObesityImageResponse>>,
                response: Response<ApiResponse<ObesityImageResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val message = responseBody?.message ?: "No message from server"
                    val obesityImageResponse = responseBody?.result
                    Toast.makeText(requireContext(), "Upload successful! ID: ${obesityImageResponse?.obesityId}", Toast.LENGTH_LONG).show()
                    // Show ResultFragment
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ResultFragment())
                        .addToBackStack(null) // Optional: Add to back stack to enable back navigation
                        .commit()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("UploadError", "Error: ${response.code()} - $errorBody")
                    Toast.makeText(requireContext(), "Upload failed: $errorBody", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<ObesityImageResponse>>, t: Throwable) {
                Log.e("UploadFailure", "Upload error: ${t.message}", t)
                Toast.makeText(requireContext(), "Upload error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val filePath = it.getString(columnIndex)
            it.close()
            return filePath
        }
        return null
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
                            Glide.with(this@CameraFragment)
                                .load(it)
                                .into(binding.testSelectedIv)
                        } ?: run {
                            Toast.makeText(context, "Image not available", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("CameraFragment", "Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<ObesityGetImageResponse>>, t: Throwable) {
                    Log.e("CameraFragment", "Failure: ${t.message}")
                }
            })
    }


}
