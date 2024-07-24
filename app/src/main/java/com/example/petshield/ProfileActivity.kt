package com.example.petshield

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petshield.databinding.ActivityProfileBinding
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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

        binding.profileSelectImageBtn.setOnClickListener {
            openGallery()
        }

        binding.profileNextBtn.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                binding.profileImageIv.setImageURI(it)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        getResult.launch(intent)
    }

    private fun setupGenderRadioButtons() {
        binding.profileDogSexMaleBtn.setOnClickListener {
            selectGender("Male")
        }
        binding.profileDogSexFemaleBtn.setOnClickListener {
            selectGender("Female")
        }
        binding.profileDogSexNoneBtn.setOnClickListener {
            selectGender("Neutered")
        }
    }

    private fun selectGender(gender: String) {
        // Deselect all
        binding.profileDogSexMaleBtn.isChecked = false
        binding.profileDogSexFemaleBtn.isChecked = false
        binding.profileDogSexNoneBtn.isChecked = false

        // Select the clicked one
        when (gender) {
            "Male" -> binding.profileDogSexMaleBtn.isChecked = true
            "Female" -> binding.profileDogSexFemaleBtn.isChecked = true
            "Neutered" -> binding.profileDogSexNoneBtn.isChecked = true
        }
    }

    private fun setupBreedSpinner() {
        val breeds = arrayOf("포메라니안", "푸들", "말티즈", "비숑", "리트리버")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, breeds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.profileDogKindLv.adapter = adapter
    }

    private fun setupSizeSpinner() {
        val sizes = arrayOf("소형견", "중형견", "대형견")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.profileDogSizeLv.adapter = adapter
    }

    private fun setupSpecialConditionsSpinner() {
        val conditions = arrayOf("임신", "비임신", "기타")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.profileDogEctLv.adapter = adapter
    }

    private fun setupBirthdatePicker() {
        binding.profileDogBirthEt.setOnClickListener {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Set the selected date to the EditText
                    binding.profileDogBirthEt.setText("$selectedYear-${selectedMonth + 1}-$selectedDay")
                },
                year, month, day
            )

            // Hide the calendar view
            datePickerDialog.datePicker.setCalendarViewShown(false)

            // Set the background of the dialog to be transparent
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // Show the dialog
            datePickerDialog.show()
        }
    }

}
