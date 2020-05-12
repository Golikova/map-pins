package com.example.map_pins.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.ar.core.AugmentedImage
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Entity
data class Pin(
    @PrimaryKey(autoGenerate = true) val pinId:Long,
    var name:String,
    var augmentedImage: String,
    var date:String,
    var description: String
    ) {

}