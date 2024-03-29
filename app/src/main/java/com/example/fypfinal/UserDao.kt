package com.example.fypfinal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert
    suspend fun insertAge(user: User)

    @Query("SELECT age FROM users")
    suspend fun getAge(): Int?

    @Insert
    suspend fun insertName(user: User)

    @Query("SELECT name FROM users")
    suspend fun getName(): String

    @Insert
    suspend fun insertGender(user: User)

    @Query("SELECT gender FROM users")
    suspend fun getGender(): String?

    @Insert
    suspend fun insertHeight(user: User)

    @Query("SELECT height FROM users")
    suspend fun getHeight(): Int?

    @Insert
    suspend fun insertWeight(user: User)

    @Query("SELECT weight FROM users")
    suspend fun getWeight(): Int?

    @Insert
    suspend fun insertInjuries(user: User)

    @Query("SELECT CASE WHEN injuries = 1 THEN 'YES' WHEN injuries = 0 THEN 'NO' END AS Injury_Value FROM users")
    suspend fun getInjury(): String?

    // Insert fitness goal data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFitGoal(fitGoal: FitGoal)

    // Get true fitness goal as string
    @Query("SELECT CASE WHEN FG_Other = 1 THEN 'FG_Other' WHEN FG_WL = 1 THEN 'FG_WL' WHEN FG_MB = 1 THEN 'FG_MB' END AS Final_Value FROM users WHERE FG_Other = 1 OR FG_WL = 1 OR FG_MB = 1")
    suspend fun getTrueFitGoal(): String?


    /* Updating the survey options*/

    @Query("UPDATE users SET name = :input")
    suspend fun insertnewName(input: String)

    @Query("UPDATE users SET age = :input")
    suspend fun insertNewAge(input: Int?)

    @Query("UPDATE users SET gender = :input")
    suspend fun insertNewGender(input: String)

    @Query("UPDATE users SET weight = :input")
    suspend fun insertnewWeight(input: Int?)

    @Query("UPDATE users SET height = :input")
    suspend fun insertnewHeight(input: Int?)

//    @Query("UPDATE users SET injuries = CASE WHEN :input = 'Yes' THEN 1")
//    suspend fun insertNewInjury(input: String)

//    @Query("UPDATE users SET ")
//    suspend fun insertNewFG(input: String)


}