package com.example.petshield

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityModifyBinding
import java.util.Calendar

class ModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up gender radio buttons
        setupGenderRadioButtons()
        // Set up breed spinner
        setupBreedSpinner()
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
            finish()
        }
    }
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                binding.modifyImageIv.setImageURI(it)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getResult.launch(intent)
    }

    private fun setupGenderRadioButtons() {
        binding.modifyDogSexMaleBtn.setOnClickListener {
            selectGender("Male")
        }
        binding.modifyDogSexFemaleBtn.setOnClickListener {
            selectGender("Female")
        }
        binding.modifyDogSexNoneBtn.setOnClickListener {
            selectGender("Neutered")
        }
    }

    private fun selectGender(gender: String) {
        // Deselect all
        binding.modifyDogSexMaleBtn.isChecked = false
        binding.modifyDogSexFemaleBtn.isChecked = false
        binding.modifyDogSexNoneBtn.isChecked = false

        // Select the clicked one
        when (gender) {
            "Male" -> binding.modifyDogSexMaleBtn.isChecked = true
            "Female" -> binding.modifyDogSexFemaleBtn.isChecked = true
            "Neutered" -> binding.modifyDogSexNoneBtn.isChecked = true
        }
    }

    private fun setupBreedSpinner() {
        val breeds = arrayOf("포메라니안", "푸들", "말티즈", "비숑", "리트리버")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, breeds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.modifyDogKindLv.adapter = adapter
    }

    private fun setupSizeSpinner() {
        val sizes = arrayOf("소형견", "중형견", "대형견")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, sizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.modifyDogSizeLv.adapter = adapter
    }

    private fun setupSpecialConditionsSpinner() {
        val conditions = arrayOf("임신", "비임신", "기타")
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

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.modifyDogBirthEt.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
            }, year, month, day)

            datePickerDialog.show()
        }
    }
}

    
    