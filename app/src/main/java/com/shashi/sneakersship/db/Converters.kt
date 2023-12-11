package com.shashi.sneakersship.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.shashi.sneakersship.model.Sneaker

class Converters {
    @TypeConverter
    fun fromJavaObjectToJson(sneaker: Sneaker): String {
        return Gson().toJson(sneaker)
    }

    @TypeConverter
    fun fromJsonToJavaObject(json: String): Sneaker {
        return Gson().fromJson(json, Sneaker::class.java)
    }
}