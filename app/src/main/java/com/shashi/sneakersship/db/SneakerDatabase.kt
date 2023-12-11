package com.shashi.sneakersship.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shashi.sneakersship.model.CartItem

@Database(
    entities = [CartItem::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SneakerDatabase : RoomDatabase() {

    abstract fun getSneakerDao(): SneakerDao

    companion object {
        @Volatile
        private var instance: SneakerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SneakerDatabase::class.java,
                "sneaker_db.db"
            ).build()
    }
}