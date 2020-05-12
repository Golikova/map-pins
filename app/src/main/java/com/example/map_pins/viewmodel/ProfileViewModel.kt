package com.example.map_pins.viewmodel

import androidx.lifecycle.ViewModel
import com.example.map_pins.data.repository.PinRepository

class ProfileViewModel(
    private val pinRepository: PinRepository
) : ViewModel() {

}