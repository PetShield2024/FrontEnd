package com.example.petshield

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.petshield.databinding.ActivityProfileBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var selectedImageUri: Uri? = null
    private var dogId: Long? = null
    private val STORAGE_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 권한 체크 및 요청
        checkStoragePermission()

        setupGenderRadioButtons()
        setupSizeSpinner()
        setupSpecialConditionsSpinner()
        setupBirthdatePicker()

        binding.profileSelectImageBtn.setOnClickListener {
            openGallery()
        }

        binding.profileNextBtn.setOnClickListener {
            registerDogProfile()
            val intent = Intent(this@ProfileActivity, FirstActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 없는 경우 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "저장소 접근 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "저장소 접근 권한이 거부되었습니다. 이미지 선택이 불가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let {
                binding.profileImageIv.setImageURI(it)
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
        } else {
            // 권한이 없으면 권한 요청
            checkStoragePermission()
        }
    }

    private fun setupGenderRadioButtons() {
        binding.profileDogSexMaleBtn.setOnClickListener {
            selectGender("수컷")
        }
        binding.profileDogSexFemaleBtn.setOnClickListener {
            selectGender("암컷")
        }
        binding.profileDogSexNoneBtn.setOnClickListener {
            selectGender("중성화")
        }
    }

    private fun selectGender(gender: String) {
        binding.profileDogSexMaleBtn.isChecked = gender == "수컷"
        binding.profileDogSexFemaleBtn.isChecked = gender == "암컷"
        binding.profileDogSexNoneBtn.isChecked = gender == "중성화"
    }

    private fun setupSizeSpinner() {
        val sizes = arrayOf("소형견", "중형견", "대형견")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.profileDogSizeLv.adapter = adapter
    }

    private fun setupSpecialConditionsSpinner() {
        val conditions = arrayOf("임신", "비임신")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.profileDogEctLv.adapter = adapter
    }

    private fun setupBirthdatePicker() {
        binding.profileDogBirthEt.setOnClickListener {
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
                    binding.profileDogBirthEt.setText("$selectedYear-$formattedMonth-$formattedDay")
                },
                year, month, day
            )

            datePickerDialog.datePicker.setCalendarViewShown(false)
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }
    }

    private fun registerDogProfile() {
        val userId = 1L  // Replace with actual user ID

        val dogName = binding.profileDogNameEt.text.toString()
        val gender = when {
            binding.profileDogSexMaleBtn.isChecked -> "수컷"
            binding.profileDogSexFemaleBtn.isChecked -> "암컷"
            binding.profileDogSexNoneBtn.isChecked -> "중성화"
            else -> ""
        }
        val birthDate = binding.profileDogBirthEt.text.toString();



        val breed = binding.profileDogKindLv.text.toString()
        val weight = binding.profileDogWeightEt.text.toString().toFloatOrNull() ?: 0.0f
        var size = binding.profileDogSizeLv.selectedItem.toString();
        var extra = binding.profileDogEctLv.selectedItem.toString();



        val dogProfile = DogProfileRequest(
            dogName = dogName,
            gender = gender,
            birth = birthDate,
            breed = breed,
            weight = weight,
            size = size,
            extra = extra
        )

        // Toast.makeText(this@ProfileActivity, "강아지 날짜: ${dogProfile.gender},  ${dogProfile.dogName},  ${dogProfile.breed},  ${dogProfile.weight},  ${dogProfile.size}, ${dogProfile.birth},${dogProfile.extra}.", Toast.LENGTH_SHORT).show()

        val dogProfileBody = Gson().toJson(dogProfile).toRequestBody("application/json".toMediaTypeOrNull())

        RetrofitClientApi.retrofitInterface.registerDogProfile(
            userId,
            dogProfileBody
        ).enqueue(object : Callback<ApiResponse<DogProfileResponse>> {
            override fun onResponse(call: Call<ApiResponse<DogProfileResponse>>, response: Response<ApiResponse<DogProfileResponse>>) {
                if (response.isSuccessful) {
                    val dogProfileResponse = response.body()
                    dogId = dogProfileResponse?.result?.dogId
                    Toast.makeText(this@ProfileActivity, "강아지 정보가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()

                    // 이미지를 등록할 수 있는 경우
                    if (selectedImageUri != null && dogId != null) {
                        uploadDogImage(dogId!!)
                    }
                } else {
                    Log.e("ProfileActivity", "Error: ${response.errorBody()?.string()},${response.body()}")
                    Toast.makeText(this@ProfileActivity, "강아지 정보 등록 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogProfileResponse>>, t: Throwable) {
                Log.e("ProfileActivity", "Failure: ${t.message}")
                Toast.makeText(this@ProfileActivity, "강아지 정보 등록 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun uploadDogImage(dogId: Long) {
        val imagePart = selectedImageUri?.let {
            val file = File(getRealPathFromURI(it))
            val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, requestBody)
        }

        RetrofitClientApi.retrofitInterface.registerDogImage(
            dogId,
            imagePart
        ).enqueue(object : Callback<ApiResponse<DogImageResponse>> {
            override fun onResponse(call: Call<ApiResponse<DogImageResponse>>, response: Response<ApiResponse<DogImageResponse>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "강아지 이미지가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ProfileActivity", "Image Upload Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@ProfileActivity, "이미지 등록 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<DogImageResponse>>, t: Throwable) {
                Log.e("ProfileActivity", "Image Upload Failure: ${t.message}")
                Toast.makeText(this@ProfileActivity, "이미지 등록 오류: ${t.message}", Toast.LENGTH_SHORT).show()
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
