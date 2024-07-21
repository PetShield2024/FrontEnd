package com.example.petshield

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class CameraFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 672
    private val imageFilePath: String? = null
    private val photoUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        return view
    }

}
