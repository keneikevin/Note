package com.example.note.data.local

import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverters
    fun fromList(list: List<String>):String{
        return Gson().toJson(list)
    }
    @TypeConverters
    fun toList(string: String):List<String>{
        return Gson().fromJson(string, object :TypeToken<List<String>>(){}.type)
    }
}