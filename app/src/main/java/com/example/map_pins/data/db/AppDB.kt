package com.example.map_pins.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.model.UserImg


@Database(
    entities = [Pin::class, UserImg::class],
    version = 2
)
abstract class AppDB: RoomDatabase() {

        abstract fun getPinDao() :PinDAO
        abstract fun getUserImgDao() :UserImageDAO

        companion object{

            private  var instance: AppDB? = null
            private val LOCK = Any()

            val MIGRATION_1_2: Migration = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("Alter table pin add column description TEXT not null default ' ' ")

                }
            }

            operator fun invoke(context : Context) =
                instance ?: synchronized(LOCK) {
                    instance ?: buildDB(context)
                        .also { instance = it }
                }

            private  fun buildDB (context: Context) =
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "AppDB.db"
                ).allowMainThreadQueries()
                    .addMigrations(AppDB.MIGRATION_1_2)
                    .build()
        }


    }
