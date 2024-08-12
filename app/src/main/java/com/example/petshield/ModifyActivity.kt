package com.example.petshield

import android.Manifest
import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.petshield.databinding.ActivityModifyBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar

class ModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyBinding
    private var selectedImageUri: Uri? = null
    private var dogId: Long? = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기존 강아지 정보 불러오기
        loadDogInfo(dogId!!)

        // Set up gender radio buttons
        setupGenderRadioButtons()

        // Set up size spinner
        setupSizeSpinner()
        // Set up special conditions spinner
        setupSpecialConditionsSpinner()
        // Set up birthdate picker
        setupBirthdatePicker()

        binding.modifySelectImageBtn.setOnClickListener {
            openGallery()
        }

        binding.modifyCompleteBtn.setOnClickListener {
            registerDogModify()
            finish()
        }
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let {
                binding.modifyImageIv.setImageURI(it)
            }
        }
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            getResult.launch(intent)
        }
    }

    private fun setupGenderRadioButtons() {
        binding.modifyDogSexMaleBtn.setOnClickListener {
            selectGender("수컷")
        }
        binding.modifyDogSexFemaleBtn.setOnClickListener {
            selectGender("암컷")
        }
        binding.modifyDogSexNoneBtn.setOnClickListener {
            selectGender("중성화")
        }
    }

    private fun selectGender(gender: String) {
        binding.modifyDogSexMaleBtn.isChecked = gender == "수컷"
        binding.modifyDogSexFemaleBtn.isChecked = gender == "암컷"
        binding.modifyDogSexNoneBtn.isChecked = gender == "중성화"
    }

    private fun setupSizeSpinner() {
        val sizes = arrayOf("소형견", "중형견", "대형견")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, sizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.modifyDogSizeLv.adapter = adapter
    }

    private fun setupSpecialConditionsSpinner() {
        val conditions = arrayOf("임신", "비임신")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, conditions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.modifyDogEctLv.adapter = adapter
    }

    private fun setupBirthdatePicker() {
        binding.modifyDogBirthEt.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedMonth = String.format("%02d", selectedMonth + 1)
                    val formattedDay = String.format("%02d", selectedDay)
                    binding.modifyDogBirthEt.setText("$selectedYear-$formattedMonth-$formattedDay")
                },
                year, month, day
            )

            datePickerDialog.datePicker.setCalendarViewShown(false)
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }
    }

    private fun loadDogInfo(dogId: Long) {
        RetrofitClientApi.retrofitInterface.getDogInfo(dogId).enqueue(object : Callback<ApiResponse<DogInfoResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogInfoResponse>>,
                response: Response<ApiResponse<DogInfoResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.result?.let { dogInfo ->
                        // Set fields with current dog information
                        binding.modifyDogNameEt.setText(dogInfo.dogName)
                        binding.modifyDogBirthEt.setText(dogInfo.birth)
                        binding.modifyDogWeightEt.setText(dogInfo.weight.toString())
                        binding.modifyDogKindLv.setText(dogInfo.breed)

                        // Set gender radio buttons
                        selectGender(dogInfo.gender)

                        // Set size spinner
                        val sizePosition = (binding.modifyDogSizeLv.adapter as ArrayAdapter<String>)
                            .getPosition(dogInfo.size)
                        binding.modifyDogSizeLv.setSelection(sizePosition)

                        // Set extra conditions spinner
                        val extraPosition = (binding.modifyDogEctLv.adapter as ArrayAdapter<String>)
                            .getPosition(dogInfo.extra)
                        binding.modifyDogEctLv.setSelection(extraPosition)

                        // Load current dog image
                        loadDogImage(dogId)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogInfoResponse>>, t: Throwable) {
                Toast.makeText(this@ModifyActivity, "강아지 정보를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDogImage(dogId: Long) {
        RetrofitClientApi.retrofitInterface.getDogImage(dogId).enqueue(object : Callback<ApiResponse<DogImageGetResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogImageGetResponse>>,
                response: Response<ApiResponse<DogImageGetResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.result?.imageUrl?.let { imageUrl ->
                        Glide.with(this@ModifyActivity)
                            .load(imageUrl)
                            .into(binding.modifyImageIv)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogImageGetResponse>>, t: Throwable) {
                Toast.makeText(this@ModifyActivity, "강아지 이미지를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerDogModify() {
        val dogName = binding.modifyDogNameEt.text.toString()
        val gender = when {
            binding.modifyDogSexMaleBtn.isChecked -> "수컷"
            binding.modifyDogSexFemaleBtn.isChecked -> "암컷"
            binding.modifyDogSexNoneBtn.isChecked -> "중성화"
            else -> ""
        }
        val birthDate = binding.modifyDogBirthEt.text.toString()
        val breed = binding.modifyDogKindLv.text.toString()
        val weight = binding.modifyDogWeightEt.text.toString().toFloatOrNull() ?: 0.0f
        val size = binding.modifyDogSizeLv.selectedItem.toString()
        val extra = binding.modifyDogEctLv.selectedItem.toString()

        val dogModify = DogProfileRequest(
            dogName = dogName,
            gender = gender,
            birth = birthDate,
            breed = breed,
            weight = weight,
            size = size,
            extra = extra
        )

        // Serialize the dog profile request
        val dogModifyBody = Gson().toJson(dogModify).toRequestBody("application/json".toMediaTypeOrNull())

        // API call to modify dog profile
        RetrofitClientApi.retrofitInterface.modifyDogProfile(dogId!!, dogModifyBody).enqueue(object : Callback<ApiResponse<DogModifyResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogModifyResponse>>,
                response: Response<ApiResponse<DogModifyResponse>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ModifyActivity, "강아지 정보가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()

                    // Upload the image if it was changed
                    if (selectedImageUri != null) {
                        uploadDogImage(dogId!!)
                    }
                } else {
                    Log.e("ModifyActivity", "Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ModifyActivity, "강아지 정보 수정 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogModifyResponse>>, t: Throwable) {
                Log.e("ModifyActivity", "Failure: ${t.message}")
                Toast.makeText(this@ModifyActivity, "강아지 정보 수정 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadDogImage(dogId: Long) {
        val imagePart = selectedImageUri?.let {
            val file = File(getRealPathFromURI(it))
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, requestBody)
        }

        RetrofitClientApi.retrofitInterface.modifyDogImage(dogId, imagePart).enqueue(object : Callback<ApiResponse<DogImageModifyResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<DogImageModifyResponse>>,
                response: Response<ApiResponse<DogImageModifyResponse>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ModifyActivity, "강아지 이미지가 성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ModifyActivity", "Image Upload Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ModifyActivity, "이미지 수정 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogImageModifyResponse>>, t: Throwable) {
                Log.e("ModifyActivity", "Image Upload Failure: ${t.message}")
                Toast.makeText(this@ModifyActivity, "이미지 수정 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        contentResolver.query(contentUri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return ""
    }
}
