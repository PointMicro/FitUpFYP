package com.example.fypfinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.concurrent.TimeoutException


class SettingsFragment : Fragment() {

    lateinit var dM_Button: Button
    lateinit var details_Button: Button
    lateinit var delete_Button: Button
    lateinit var genderSpinner: Spinner
    lateinit var fitnessGoalSpinner: Spinner
    lateinit var injurySpinner: Spinner
    lateinit var name_Settings: TextView
    lateinit var age_Settings: TextView
    lateinit var weight_Settings: TextView
    lateinit var height_Settings: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.fypfinal.R.layout.fragment_settings, container, false)

        dM_Button = view.findViewById(R.id.darkModebutton)
        delete_Button = view.findViewById(R.id.deleteButton)
        details_Button = view.findViewById(R.id.detailsButton)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dM_Button.setOnClickListener{

        }


        delete_Button.setOnClickListener{

        }

        details_Button.setOnClickListener{

            val builder = AlertDialog.Builder(requireContext())
            val inflater = LayoutInflater.from(requireContext())
            val dialogLayout = inflater.inflate(R.layout.change_details, null)



            val genderSettingsList = listOf("Unchanged", "Male", "Female", "Other")
            val injurySettingsList = listOf("Unchanged", "Yes", "No")
            val fGSettingsList = listOf("Unchanged", "Muscle Build", "Other / General", "Weight Loss")

            val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderSettingsList)
            val injuryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, injurySettingsList)
            val fGSettingsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fGSettingsList)

            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            injuryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fGSettingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            genderSpinner.adapter = genderAdapter
            injurySpinner.adapter = injuryAdapter
            fitnessGoalSpinner.adapter = fGSettingsAdapter




            builder.setView(dialogLayout).setPositiveButton("Confirm") { dialogInterface, i ->
                // handle confirm button click

                val calendar_db = Room.databaseBuilder(requireContext(),
                    CalendarDatabase::class.java, "calendar_db").build()
                val dao_access_calendar = calendar_db.calendarDao()


                val user_db = Room.databaseBuilder(requireContext(),
                    UserDatabase::class.java, "userDB").build()
                val dao_access_user = user_db.UserDao()

                lifecycleScope.launch {
                    val old_name = dao_access_user.getName()
                    val old_age = dao_access_user.getAge()
                    val old_height = dao_access_user.getHeight()
                    val old_weight = dao_access_user.getWeight()
                    val old_injury = dao_access_user.getInjury()
                    val old_fitness = dao_access_user.getTrueFitGoal()



                genderSpinner.selectedItem.toString()
                injurySpinner.selectedItem.toString()
                fitnessGoalSpinner.selectedItem.toString()


                val changeBuilder = StringBuilder()
                val errorBuilder = StringBuilder()

                    //Null means keep unchanged and dont acknowledge it
                        //If age is under 16 then error appears

                val name = name_Settings.text.toString()
                if(name.isNotBlank()) {
                    changeBuilder.append(old_name + "Will be updated to:  " + name)
                }
                val age = age_Settings.text.toString().toIntOrNull()
                 if(age != null && age > 15){
                     changeBuilder.append(old_age.toString() + "Will be updated to:" + age)
                 }else if(age != null && age < 16){
                     errorBuilder.append("This application is 16+")
                 }
                    val height = height_Settings.text.toString().toInt()
                    if(height != null && height > 240 || height < 90){
                        errorBuilder.append("Enter a valid height")
                    }else if(height != null){
                        changeBuilder.append(old_height.toString() + "cm will be updated to: "
                                + height)
                    }
                    val weight = weight_Settings.text.toString().toInt()
//                    if (){
//
//                    }



                }

            }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    // handle cancel button click
                }
            val alertDialog = builder.create()
            alertDialog.show()

        }

    }


/*
deleteButton.setOnClickListener {
    // Call the clearAllTables() method to delete the database
    //also warn the user and make them type a password to reset
    user_db.clearAllTables()
    calendar_db.clearAllTables()
}

 */


}