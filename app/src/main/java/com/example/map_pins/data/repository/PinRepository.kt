package com.example.map_pins.data.repository

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.map_pins.data.db.AppDB
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.UserImg


class PinRepository (
    private val db: AppDB
) {

    fun getAllPins() = db.getPinDao().getAllPins()
    fun getAllAugmentedPicks() = db.getPinDao().getAllAugmentedPicks()
    fun getPinByAugmentedImage(name: String) = db.getPinDao().getPinByAugmentedImage(name)
    fun getPinById(pin: Pin) = db.getPinDao().getPinById(pin.pinId)
    fun addPin(pin : Pin) = db.getPinDao().upsert(pin)
    fun updatePin(pin : Pin) = db.getPinDao().upsert(pin)
    fun getAllPinsWithImages() = db.getPinDao().getPinsWithImages()
    fun getImagesByPin(pin: Pin) = db.getPinDao().getImagesByPinId(pin.pinId)
    fun deleteAllPins() = db.getPinDao().deleteAllPins()
    fun deletePin(pin : Pin) = db.getPinDao().deletePin(pin)

    fun addImage(img : UserImg) = db.getUserImgDao().upsert(img)
    fun getImageByPinId(pin: Pin) = db.getUserImgDao().getImageByPinId(pin.pinId)



}