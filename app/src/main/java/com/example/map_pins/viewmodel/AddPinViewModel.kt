package com.example.map_pins.viewmodel

import androidx.lifecycle.ViewModel
import com.example.map_pins.data.repository.PinRepository

class AugmentedViewModel (
    private val pinRepository: PinRepository
) : ViewModel() {

    var title: String = "Имя пина"

}