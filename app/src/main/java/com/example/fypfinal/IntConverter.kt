package com.example.fypfinal

import androidx.room.TypeConverter

class IntConverter {
    @TypeConverter
    fun fromInt(value: Int?): List<Int>? {
        return value?.let { listOf(it) }
    }

    @TypeConverter
    fun toInt(value: List<Int>?): Int? {
        return value?.get(0)
    }

    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    @JvmSuppressWildcards
    fun toList(string: String?): List<Int>? {
        return string?.split(",")?.map { it.toInt() }
    }

}
