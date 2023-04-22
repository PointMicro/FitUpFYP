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
    @ColumnInfo(name = "FG_MB") val FG_MB: Boolean?
)

@Entity
data class FitGoal(
    @PrimaryKey val finalValue: String
)



//Items recorded in User survey
//Name
//Age
//Gender
//Weight
//Height
//BMI will be calculated from the Height and Weight
//Injuries - Boolean
//Fitness Goal - Weight Loss


//OPTIONAL STUFF IF I HAVE ENOUGH TIME
//Birthday
//Phone Number
//Email