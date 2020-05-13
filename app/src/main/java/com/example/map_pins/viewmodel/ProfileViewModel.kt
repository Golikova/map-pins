package com.example.map_pins.viewmodel

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.UserImg
import com.example.map_pins.data.repository.PinRepository
import kotlinx.android.synthetic.main.edit_dialog.view.*

class ProfileViewModel(
    private val pinRepository: PinRepository
) : ViewModel() {

    var pinList = MutableLiveData<List<Pin>>()

    var pin = Pin(0,"","", "", "")

    var isValid = MutableLiveData<Boolean?>()

    fun refresh() {
        pinList.value = pinRepository.getAllPins()
    }

    fun removePin(pin: Pin) {
        pinRepository.deletePin(pin)
        pinList.value = pinRepository.getAllPins()
    }

    fun updatePin(pin: Pin) {
        pinRepository.updatePin(pin)
        pinList.value = pinRepository.getAllPins()
    }
}