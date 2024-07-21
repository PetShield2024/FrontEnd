package com.example.petshield

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.petshield.databinding.FragmentCameraBinding
import java.io.ByteArrayOutputStream

class CameraFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
    }

    private lateinit var binding: FragmentCameraBinding
    private val viewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        // 이미지 복원
        viewModel.selectedImageUri?.let { uri ->
            binding.testSelectedIv.setImageURI(uri)
        }

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
            // 이미지 검사 시작하는 로직 추가
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
                        viewModel.selectedImageUri = uri
                        binding.testSelectedIv.setImageURI(uri)

                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        viewModel.selectedImageUri = getImageUri(requireContext(), it)
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
}
