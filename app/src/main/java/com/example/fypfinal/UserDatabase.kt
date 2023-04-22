package com.example.fypfinal


import androidx.room.Database
import androidx.room.RoomDatabase



@Database(entities = [User::class, FitGoal::class], version=1)
abstract class UserDatabase: RoomDatabase(){
    abstract fun UserDao(): UserDao
}