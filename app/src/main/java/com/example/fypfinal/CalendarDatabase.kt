package com.example.fypfinal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Calendar2::class, Plan::class], version = 1)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao2
}