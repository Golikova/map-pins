package com.example.map_pins.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class PinWithImages (
    @Embedded val pin : Pin,
    @Relation(
        parentColumn = "pinId",
        entityColumn = "parentPinId"
    ) val imgList: List<UserImg>
)