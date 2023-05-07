package com.example.fypfinal


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class TermsServiceActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos)


        lifecycleScope.launch {
            checkIfDBExists()
        }

        var tos_text = findViewById<TextView>(R.id.textView)

        tos_text.text = "By using Fit-Up-Fit-Now, you agree to the following terms and conditions:\n" +
                "\n" +
                "    Personal Use Only: You may use our app for personal use only.\n" +
                "\n" +
                "    Privacy: We respect your privacy and do not share your information with third parties.\n" +
                "\n" +
                "    User Content: You are responsible for any content you upload or share through our app.\n" +
                "\n" +
                "    Intellectual Property: Our app and intellectual property are owned by us or our licensors.\n" +
                "\n" +
                "    No Warranties: Our app is provided on an \"as is\" and \"as available\" basis.\n" +
                "\n" +
                "    Limitation of Liability: We are not liable for any damages arising from your use of our app."


        val accept_button = findViewById<Button>(R.id.ac1)
        val decline_button = findViewById<Button>(R.id.dc1)


        accept_button.setOnClickListener {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)
        }

        decline_button.setOnClickListener {
            Toast.makeText(this, "Sorry. Please select 'Accept' to continue use" +
                    " of the application.", Toast.LENGTH_LONG).show()

            finish()
        }

    }


    private suspend fun checkIfDBExists(){
        val database_Name = "userDB"
        val databaseFilePath = getDatabasePath(database_Name)

        if (databaseFilePath.exists()){
            //skip the terms of service
            checkIfSurveyExists()

        }

    }

    private suspend fun checkIfSurveyExists(){
        val user_db = Room.databaseBuilder(applicationContext, UserDatabase::class.java,
            "userDB").build()
        var dao_access = user_db.UserDao()

        val user_name = dao_access.getName()
        val age = dao_access.getAge()
        val gender = dao_access.getGender()
        val height = dao_access.getHeight()
        val weight = dao_access.getWeight()
        val injury = dao_access.getInjury()
        val fitGoal = dao_access.getTrueFitGoal()




        //if all of them are not empty
        //temp removed name - user_name.isNotBlank() &&
        if (age != null && gender != null && height != null && weight
            != null && injury != null && fitGoal != null) {
            //if the survey is complete. skip to the home page

            //alternative way the calendar db can be created

            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

    }








}

//ask boolean search im having trouble wrapping my head around this. So I want TOS to be set as true. Once it is true....