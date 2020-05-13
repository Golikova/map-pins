package com.example.map_pins.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.map_pins.data.repository.PinRepository

class ProfileViewModelFactory (
    private  val pinRepository: PinRepository
) :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        return ProfileViewModel(
            pinRepository
        ) as T
    }

}