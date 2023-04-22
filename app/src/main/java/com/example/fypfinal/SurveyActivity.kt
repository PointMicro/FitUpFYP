package com.example.fypfinal

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import android.content.DialogInterface;
import android.widget.TextView


class SurveyActivity : AppCompatActivity() {



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)




        //Loading screen is the default page on start up

    /*Button and EditText Widget */
        val submit_button = findViewById<Button>(R.id.button)
        var name_query = findViewById<EditText>(R.id.editName)
        var age_query = findViewById<EditText>(R.id.editAge)
        var weight_query = findViewById<EditText>(R.id.editWeight)
        var height_query = findViewById<EditText>(R.id.editHeight)
        var counter = 0

        /*Errors*/
        var name_error = findViewById<TextView>(R.id.name_error)
        var age_error = findViewById<TextView>(R.id.age_error)
        var gender_error = findViewById<TextView>(R.id.gender_error)
        var inj_error = findViewById<TextView>(R.id.inj_error)
        var fg_error = findViewById<TextView>(R.id.fg_error)
        var height_error = findViewById<TextView>(R.id.height_error)
        var weight_error = findViewById<TextView>(R.id.weight_error)



        /*Survey Dropdown box configuration */

        val genList = resources.getStringArray(R.array.Gender)
        val injureList = resources.getStringArray(R.array.Injury)
        val FGList = resources.getStringArray(R.array.Fitness_Goal)


        val genSpinner = findViewById<Spinner>(R.id.genderSpinner)
            if(genSpinner != null){
                val adapter = ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, genList)
                genSpinner.adapter = adapter

                }

        val injSpinner = findViewById<Spinner>(R.id.injSpinner)
        if(injSpinner != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, injureList)
            injSpinner.adapter = adapter

        }

        val FGSpinner = findViewById<Spinner>(R.id.fgSpinner)
        if(FGSpinner != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, FGList)
            FGSpinner.adapter = adapter

        }

        /* Button stuff */
        submit_button.setOnClickListener{


            val user_db = Room.databaseBuilder(applicationContext, UserDatabase::class.java,
                "userDB").build()
            var dao_access_user = user_db.UserDao()



            val errorBuilder = StringBuilder()
            val selectedOption_FG = FGSpinner.selectedItem.toString()
            val selectedOption_Injury = injSpinner.selectedItem.toString()


            val name = name_query.text.toString()
            if(name.isBlank()) {
                name_error.visibility = View.VISIBLE
                errorBuilder.append("Please add a name")
                //setHighlight(name_query, true)
            }

            val age = age_query.text.toString().toIntOrNull()
            if(age == null || age < 16){
                age_error.visibility = View.VISIBLE
                errorBuilder.append("Invalid age / You must be 16+ to use this application")
                //setHighlight(age_query, true)
                //TOS Warning for age as well
            }

            val gender = genSpinner.selectedItem.toString()
            if(gender.isNullOrEmpty() || gender == "Unselected"){
                gender_error.visibility = View.VISIBLE
                errorBuilder.append("Please select a gender")
                //setHighlight(genSpinner, true)

            }

            val weight = weight_query.text.toString().toIntOrNull()
            if(weight == null || weight < 23 || weight > 500){
                weight_error.visibility = View.VISIBLE
                errorBuilder.append("Please enter a valid weight")
                //setHighlight(weight_query, true)

            }

            val height = height_query.text.toString().toIntOrNull()
            if(height == null || height < 90 || height > 240){
                height_error.visibility = View.VISIBLE
                errorBuilder.append("Please enter a valid height")
                //setHighlight(height_query, true)
            }

            val injuries = selectedOption_Injury == "true"
            if(injSpinner.selectedItem.toString().isNullOrEmpty() ||
                injSpinner.selectedItem.toString() == "Unselected"){
                inj_error.visibility = View.VISIBLE
                errorBuilder.append("Please select an option (Injury)")
                //setHighlight(injSpinner, true)

            }

            val FG_Other = selectedOption_FG == "Other / General"
            val FG_WL = selectedOption_FG == "Weight Loss"
            val FG_MB = selectedOption_FG == "Muscle Build"
            if(FGSpinner.selectedItem.toString().isNullOrEmpty() ||
                FGSpinner.selectedItem.toString() == "Unselected"){
                fg_error.visibility = View.VISIBLE
                errorBuilder.append("Please select an option (Fitness Goal)")
                //setHighlight(FGSpinner, true)
            }






            if (errorBuilder.isNotEmpty()) {

                val builder =  AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Invalid survey options selected. Please review and " +
                        "update your selections. ")
                builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                val dialog = builder.create()
                dialog.show()
            }else{

                //Adds info to database
                val currentUser = User( name, age, gender,  weight, height, injuries, FG_Other,
                    FG_WL, FG_MB)
                lifecycleScope.launch {
                    dao_access_user.insertUser(currentUser)
                }

                //Starts app
                val intent = Intent(this, LoadingActivity::class.java)
                startActivity(intent)

            }

        }

    }


    private fun setHighlight(view: View, highlight: Boolean) {
        if (highlight) {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        } else {
        }
    }





}