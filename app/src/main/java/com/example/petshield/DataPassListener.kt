package com.example.petshield

interface DataPassListener {
    fun onDataPass(
        age: String,
        size: String,
//        variety: String,
        weight: String,
        selectedBrand: String,
        selectedOrigin: String
    )
}
