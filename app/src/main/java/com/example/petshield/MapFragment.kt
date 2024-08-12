package com.example.petshield

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    // 로그에 현재 위치를 출력
                    Log.d("MapFragment", "Current Location: Latitude = ${location.latitude}, Longitude = ${location.longitude}")

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f))

                    // 이미지 파일을 비트맵으로 변환
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.user_location_icon)


                    // XML 벡터 드로어블을 비트맵으로 변환
//                    val vectorDrawable = VectorDrawableCompat.create(resources, R.drawable.user_location_icon, null)
//                    val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//                    val canvas = Canvas(bitmap)
//                    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
//                    vectorDrawable.draw(canvas)

                    // 비트맵을 마커 아이콘으로 설정
                    val markerOptions = MarkerOptions()
                        .position(userLocation)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)) // 비트맵을 사용한 마커 아이콘
                    googleMap.addMarker(markerOptions)

                    loadAnimalHospitals(userLocation.latitude, userLocation.longitude)                } else {
                    Log.e("MapFragment", "Location is null")
                    // You might want to handle this case, perhaps default to a predefined location.
                }
            }
        }
    }

    private fun loadAnimalHospitals(latitude: Double, longitude: Double) {
        RetrofitClient.service.getAnimalHospitals("9107a04751bc4e6a866681e4d8caf97b", 1, 50, "고양시")
            .enqueue(object : Callback<AnimalHospitalResponse> {
                override fun onResponse(call: Call<AnimalHospitalResponse>, response: Response<AnimalHospitalResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("AnimalHospital", "Response body: $responseBody")

                        responseBody?.row?.forEach { hospital ->
                            val hospitalLat = hospital.refineWgs84Lat ?: 0.0
                            val hospitalLng = hospital.refineWgs84Logt ?: 0.0
                            val position = LatLng(hospitalLat, hospitalLng)

                            // PNG 이미지를 마커 아이콘으로 사용
                            val markerOptions = MarkerOptions()
                                .position(position)
                                .title(hospital.bizplcName)
                                .snippet(hospital.lotnoAddr)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_location_icon)) // PNG 이미지 사용

                            googleMap.addMarker(markerOptions)
                        }

//                    val firstHospital = responseBody?.row?.firstOrNull()
//                    firstHospital?.let {
//                        val position = LatLng(it.refineWgs84Lat ?: 0.0, it.refineWgs84Logt ?: 0.0)
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12f))
//                    }
                    } else {
                        Log.e("AnimalHospital", "Failed to fetch data: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AnimalHospitalResponse>, t: Throwable) {
                    when (t) {
                        is java.net.SocketTimeoutException -> {
                            Log.e("AnimalHospital", "Network timeout: ${t.message}")
                        }
                        is java.net.UnknownHostException -> {
                            Log.e("AnimalHospital", "No internet connection: ${t.message}")
                        }
                        else -> {
                            Log.e("AnimalHospital", "Unknown error: ${t.message}")
                        }
                    }
                    t.printStackTrace()
                }
            })
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
