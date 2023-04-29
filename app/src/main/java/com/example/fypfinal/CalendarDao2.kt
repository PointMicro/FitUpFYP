package com.example.fypfinal

import androidx.room.*

@Dao
@TypeConverters(IntConverter::class)
interface CalendarDao2 {


    /* Inserting Calendar Info */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar2)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps)

    //Insert step goal
    @Query("UPDATE plan_list SET steps_goals = :stepGoal WHERE date = :input")
    suspend fun updateStepGoal(input: String, stepGoal: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitnessPlan(fitPlan: Plan)


    /* Get First and Last Dates from the Calendar */

    @Query("SELECT date FROM calendar ORDER BY date ASC LIMIT 1")
    suspend fun getFirstRecordedDate(): String

    @Query("SELECT date FROM calendar ORDER BY date DESC LIMIT 1")
    suspend fun getLastRecordedDate(): String


    /* Retrieve from Calendar Database */

    @Query("SELECT steps_goals FROM plan_list WHERE date = :date")
    suspend fun getStepGoals(date: String): Int?


    @Query("SELECT date FROM calendar WHERE date = :input")
    suspend fun getCurrDate(input: String): String?

    @Query("SELECT steps_taken FROM steps WHERE date = :input")
    suspend fun getStepsTaken(input: String): Int?

    @Query("SELECT latest_hr FROM calendar WHERE date = :input")
    suspend fun getLatestHR(input: String): Int?

    @Query("SELECT total_calories_burned FROM calendar WHERE date = :input")
    suspend fun getCaloriesBurned(input: String): Int



    /* Increments total workouts and the car */

    @Query("UPDATE calendar SET latest_hr = :heart_rate WHERE date = :input")
    suspend fun replaceHR(heart_rate: Int?, input: String)

    @Query("UPDATE calendar SET total_calories_burned = total_calories_burned + :caloriesToAdd WHERE date = :selectedDate")
    suspend fun addCaloriesBurned(selectedDate: String, caloriesToAdd: Int)
    @Query("UPDATE calendar SET workouts_completed = workouts_completed + 1 WHERE date = :input")
    suspend fun incrementWorkoutsCompleted(input: String)

    @Query("UPDATE calendar SET workout_cardio_count = workout_cardio_count + 1 WHERE date = :input")
    suspend fun incrementCardioCount(input: String)

    @Query("UPDATE calendar SET workout_yoga_count = workout_yoga_count + 1 WHERE date = :input")
    suspend fun incrementYogaCount(input: String)

    @Query("UPDATE calendar SET workout_strength_count = workout_strength_count + 1 WHERE date = :input")
    suspend fun incrementStrengthCount(input: String)

    /* Get all of the total workouts from all dates  */
    @Query("SELECT SUM(workouts_completed) FROM calendar")
    suspend fun allWorkoutsCount(): Int

    @Query("SELECT SUM(workout_yoga_count) FROM calendar")
    suspend fun allYogaCount(): Int

    @Query("SELECT SUM(workout_strength_count) FROM calendar")
    suspend fun allStrengthCount(): Int

    @Query("SELECT SUM(workout_cardio_count) FROM calendar")
    suspend fun allCardioCount(): Int

    @Query("SELECT SUM(steps_taken) FROM calendar")
    suspend fun totalSteps(): Int







    //Sets Goals for Selected Date to true
    @Query("UPDATE plan_list SET Goals_Today_Boolean = 1 WHERE date = :date")
    suspend fun updateGoalsSelected(date: String)

    //Checks which dates have goals set
    @Query("SELECT date FROM plan_list WHERE Goals_Today_Boolean = 1")
    suspend fun getDatesWithSelectedGoals(): List<String>

    //Checks if boolean is true
    @Query("SELECT EXISTS (SELECT * FROM plan_list WHERE Goals_Today_Boolean = 1 AND date = :date) ")
     suspend fun isGoalTrue(date: String): Boolean




    @Query("SELECT CASE WHEN Workout_Cardio = 1 THEN 'Cardio' WHEN Workout_Yoga = 1 THEN 'Yoga' " +
            "WHEN Workout_Strength = 1 THEN 'Strength' ELSE 'UNSET' END AS Final_Value FROM " +
            "plan_list WHERE (Workout_Cardio = 1 OR Workout_Yoga = 1 OR Workout_Strength = 1) AND date = :input")
    suspend fun getTrueSelection(input: String): String?

    @Update
    suspend fun updateWorkoutBooleans(plan: Plan)





}


