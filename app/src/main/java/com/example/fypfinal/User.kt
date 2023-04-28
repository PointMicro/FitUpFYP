package com.example.fypfinal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "weight") val weight: Int?,
    @ColumnInfo(name = "height") val height: Int?,
    @ColumnInfo(name = "injuries") val injuries: Boolean?,
    @ColumnInfo(name = "FG_Other") val FG_Other: Boolean?,
    @ColumnInfo(name = "FG_WL") val FG_WL: Boolean?,
    @ColumnInfo(name = "FG_MB") val FG_MB: Boolean?,
    @ColumnInfo(name = "dark_mode") val darkMode: Boolean = false
)

@Entity
data class FitGoal(
    @PrimaryKey val finalValue: String
)
