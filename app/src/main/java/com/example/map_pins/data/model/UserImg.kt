package com.example.map_pins.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserImg(
    @PrimaryKey(autoGenerate = true) val imgId: Long,
    val imgName: String,
    val parentPinId: Long
)