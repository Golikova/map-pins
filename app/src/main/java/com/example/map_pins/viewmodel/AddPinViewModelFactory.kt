package com.example.map_pins.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.map_pins.data.repository.PinRepository

class AugmentedViewModelFactory (
    private  val pinRepository: PinRepository
) :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        return AugmentedViewModel(
            pinRepository
        ) as T
    }

}