package com.example.fypfinal
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

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
    val stepsTaken: Int? = null,
    @ColumnInfo(name = "total_calories_burned")
    val totalCaloriesBurned: Int? = null,
    @ColumnInfo(name = "heart_rate_recordings")
    val heartRateRecordings: List<Int>? = null
)

@Entity(tableName = "steps")
data class Steps(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "steps_taken")
    val stepsTaken: Int?
)

@Entity(tableName = "calories")
data class Calories(
    @PrimaryKey
    val date: String,
    @ColumnInfo(name = "total_calories_burned")
    val totalCaloriesBurned: Int?
)

@Entity(tableName = "hr_recordings")
data class HrRecordings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "heart_rate")
    val heartRate: Int
)



//C_DB items
// doingGoalforThisDay - Boolean
// duration_goal -Int? that can be nullable [DURATION MAY NOT BE NEEDED]
// workout_goal - Int?
// steps_goal - Int?
//goals are null unless doing goals for this day is true
//duration could be calculated automatically when selecting a workout


//@Entity(tableName = "plan_list")
//data class Plan(
//    @PrimaryKey
//    val date: String,
//    @ColumnInfo(name = "Goals_Today_Boolean")
//    val goals_selected: Boolean = false, //Always false unless set true
////    @ColumnInfo(name = "workout_goals")
////    val workout_goal: String?,
//
//    @ColumnInfo(name = "Workout_Yoga")
//    val W_Y: Boolean?,
//    @ColumnInfo(name = "Workout_Strength")
//    val W_S: Boolean?,
//    @ColumnInfo(name = "Workout_Cardio")
//    val W_C: Boolean?,
//    @ColumnInfo(name = "steps_goals")
//    val step_goal: Int?
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



//Calendar
    //When you pick a date it should display:
            //[Add a Goal] if Unselected

                //Add a Goal is replaced with
                    //Workout Goal: [Workout type here or UNSET]
                    //Duration: [Estimated to whatever exercises were selected]
                    //Steps Goal: [Either a number or UNSET]

                //Date with a goal set is highlighted
//CHECK PAINT FILES



//TALK ABOUT CUT DEV ABILITIES (HOW I LIMTIED THE CALENDAR PAGE - WORKOUT FROM 2 TO 1)

//Date could be a string


//Make a for loop where it goes through all dates from march 2023 to december 2024
    //converts all of the dates to string
    //converts it back if it wants to check

