package com.example.map_pins.ui.ar

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.map_pins.data.model.Pin

class AugmentedAdapter(
    var pin: Pin
) : BaseObservable() {

    @Bindable
    fun getTitle() : String {
        return pin.name
    }

}