package com.example.fypfinal
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar")
data class Calendar2(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "workouts_completed")
    val workoutsCompleted: Int = 0,
    @ColumnInfo(name = "workout_cardio_count")
    val workoutCardioCount: Int = 0,
    @ColumnInfo(name = "workout_yoga_count")
    val workoutYogaCount: Int = 0,
    @ColumnInfo(name = "workout_strength_count")
    val workoutStrengthCount: Int = 0,
    @ColumnInfo(name = "steps_taken")
    val stepsTaken: Int = 0,
    @ColumnInfo(name = "total_calories_burned")
    val totalCaloriesBurned: Int = 0,
    @ColumnInfo(name = "latest_hr")
    val latestHR: Int? = null
)

//@Entity(tableName = "steps")
//data class Steps(
//    @PrimaryKey
//    val date: String,
//    @ColumnInfo(name = "steps_taken")
//    val stepsTaken: Int?
//)

@Entity(tableName = "plan_list")
data class Plan(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "Goals_Today_Boolean")
    val goals_selected: Boolean = false, //Always false unless set true
    @ColumnInfo(name = "Workout_Yoga")
    val W_Y: Boolean?,
    @ColumnInfo(name = "Workout_Strength")
    val W_S: Boolean?,
    @ColumnInfo(name = "Workout_Cardio")
    val W_C: Boolean?,
    @ColumnInfo(name = "steps_goals")
    val steps_goals: Int?
)
