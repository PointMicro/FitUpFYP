package com.example.fypfinal

import androidx.room.*

@Dao
@TypeConverters(IntConverter::class)
interface CalendarDao2 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar2)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalories(calories: Calories)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeartRateRecordings(hrRecordings: List<HrRecordings>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitnessPlan(fitPlan: Plan)

    @Update
    suspend fun updateWorkoutBooleans(plan: Plan)

    //Insert step goal
    @Query("UPDATE plan_list SET steps_goals = :stepGoal WHERE date = :input")
    suspend fun updateStepGoal(input: String, stepGoal: Int)


    //select first date
    @Query("SELECT date FROM calendar ORDER BY date ASC LIMIT 1")
    suspend fun getFirstRecordedDate(): String

    //select last date
    @Query("SELECT date FROM calendar ORDER BY date DESC LIMIT 1")
    suspend fun getLastRecordedDate(): String

//    @Query("SELECT date, workout_goals FROM plan_list WHERE date = :input")
//    suspend fun getWorkoutGoals(input: String): Plan?

    @Query("SELECT steps_goals FROM plan_list WHERE date = :date")
    suspend fun getStepGoals(date: String): Int?

    @Query("SELECT date FROM calendar WHERE date = :input")
    suspend fun getCurrDate(input: String): String?

    @Query("SELECT * FROM steps WHERE date = :input")
    suspend fun getStepsTaken(input: String): Steps?

    @Query("SELECT * FROM hr_recordings WHERE date = :input")
    suspend fun getHeartRateRecordings(input: String): List<HrRecordings>

    @Query("SELECT * FROM calories WHERE date = :input")
    suspend fun getCaloriesBurned(input: String): Calories?

    //Sets Goals for Selected Date to true
    @Query("UPDATE plan_list SET Goals_Today_Boolean = 1 WHERE date = :date")
    suspend fun updateGoalsSelected(date: String)

    //Checks which dates have goals set
    @Query("SELECT date FROM plan_list WHERE Goals_Today_Boolean = 1")
    suspend fun getDatesWithSelectedGoals(): List<String>

    //Checks if boolean is true
    @Query("SELECT EXISTS (SELECT * FROM plan_list WHERE Goals_Today_Boolean = 1 AND date = :date) ")
     suspend fun isGoalTrue(date: String): Boolean


    // Increments the workout count
    @Query("UPDATE calendar SET workouts_completed = workouts_completed + 1 WHERE date = :input")
    suspend fun incrementWorkoutsCompleted(input: String)

    // Increments the cardio count
    @Query("UPDATE calendar SET workout_cardio_count = workout_cardio_count + 1 WHERE date = :input")
    suspend fun incrementCardioCount(input: String)

    // Increments the yoga count
    @Query("UPDATE calendar SET workout_yoga_count = workout_yoga_count + 1 WHERE date = :input")
    suspend fun incrementYogaCount(input: String)

    // Increments the strength count
    @Query("UPDATE calendar SET workout_strength_count = workout_strength_count + 1 WHERE date = :input")
    suspend fun incrementStrengthCount(input: String)

    @Query("SELECT CASE WHEN Workout_Cardio = 1 THEN 'Cardio' WHEN Workout_Yoga = 1 THEN 'Yoga' " +
            "WHEN Workout_Strength = 1 THEN 'Strength' ELSE 'UNSET' END AS Final_Value FROM " +
            "plan_list WHERE (Workout_Cardio = 1 OR Workout_Yoga = 1 OR Workout_Strength = 1) AND date = :input")
    suspend fun getTrueSelection(input: String): String?


}
