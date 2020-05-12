package com.example.map_pins.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.UserImg

@Dao
interface UserImageDAO {

    @Insert
    fun upsert(userImage: UserImg) : Long

    @Query("Select * from userimg where parentPinId like :id")
    fun getImageByPinId(id: Long) : List<UserImg>

}