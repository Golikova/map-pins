package com.example.map_pins.viewmodel

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.UserImg
import com.example.map_pins.data.repository.PinRepository
import java.net.URI
import java.time.LocalDate
import java.util.*


class AddPinViewModel(
    private val pinRepository: PinRepository
) : ViewModel() {

    var title: String = ""
    var description: String = ""

    var datePicker: Int = 0
    var monthPicker: Int = 0
    var yearPicker: Int = 0

    var imageUri: Uri? = null
    var augmUri: Uri? = null

    var isValid = MutableLiveData<Boolean?>()


    fun afterTitleTextChanged(s: Editable?) {

        title = s.toString()

    }


    fun afterDescriptionTextChanged(s: Editable?) {

        description = s.toString()

    }

    fun onSaveBtnClick(view: View) {

        if (title.isNullOrEmpty() ||
            Uri.EMPTY == imageUri ||
            Uri.EMPTY == augmUri
        ) {
            isValid.value = false
        } else {
            isValid.value = true
            var pin = Pin(
                0,
                title,
                augmUri.toString(),
                "${yearPicker}-${monthPicker}-${datePicker}",
                description
            )
            var newPinId: Long = pinRepository.addPin(pin);

            var userImg = UserImg(0, imageUri.toString(), newPinId)
            pinRepository.addImage(userImg)

            title = ""
            description = ""
            imageUri = Uri.EMPTY
            augmUri = Uri.EMPTY

        }

    }

    fun onDateChanged(
        view: DatePicker,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        Log.d("MyTag", year.toString()+monthOfYear+dayOfMonth)
        datePicker = dayOfMonth
        monthPicker = monthOfYear
        yearPicker = year
    }
}