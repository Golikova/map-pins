package com.example.map_pins.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.PinWithImages

@Dao
interface PinDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(pin: Pin) : Long

    @Query("Select * from pin")
    fun getAllPins() : List<Pin>

    @Query("Select augmentedImage from pin")
    fun getAllAugmentedPicks() : List<String>

    @Query("Select * from pin where augmentedImage like :name")
    fun getPinByAugmentedImage(name: String) : List<Pin>

    @Query("DELETE FROM pin")
    fun deleteAllPins()

    @Delete
    fun deletePin(pin: Pin)

    @Transaction
    @Query("SELECT * FROM Pin")
    fun getPinsWithImages(): List<PinWithImages>

    @Transaction
    @Query("Select * from pin where pinId like :pinId")
    fun getImagesByPinId(pinId: Long) : List<PinWithImages>

}