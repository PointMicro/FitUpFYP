package com.example.fypfinal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Calendar2::class, Steps::class, HrRecordings::class, Plan::class], version = 1)
@TypeConverters(IntConverter::class)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao2
}