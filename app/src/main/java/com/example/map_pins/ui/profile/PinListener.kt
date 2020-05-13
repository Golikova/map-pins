package com.example.map_pins.ui.profile

import android.view.View
import com.example.map_pins.data.model.Pin

interface PinListener {
    fun onDeleteClick(view : View, pin: Pin)
    fun onEditClick(view : View, pin: Pin)
}