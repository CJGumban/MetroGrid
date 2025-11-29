package com.example.metrogrid.data.api.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return Gson().toJson(location)
    }

    @TypeConverter
    fun toAddress(json: String?): Location?{
        return Gson().fromJson(json, Location::class.java)
    }
}