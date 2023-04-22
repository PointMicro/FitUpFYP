package com.example.fypfinal

import androidx.fragment.app.Fragment
import android.Manifest
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class GoalSet: Fragment() {
//
//    lateinit var confirmButton: Button
//    lateinit var wKOSpinner: Spinner
//    lateinit var stepsNum: EditText
//    lateinit var durationNum: TextView
//
//    override fun onCreate( savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.goal, container, false)
//
//
//
//
//        stepsNum = view.findViewById(R.id.steps_edittext)
//
//
//
//
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.goal, container, false)
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
//        val dao_access = calendar_db.calendarDao()
//
//        var selectedSteps = stepsNum.text.toString()
//        wKOSpinner = view.findViewById(R.id.workout_spinner)
//        val spinner2 = resources.getStringArray(R.array.Workout)
//
//        val the_date_selected = "2023-04-21" //TEMP VALUE
//
//
//
//        lifecycleScope.launch{
//            dao_access.updateGoalsSelected(the_date_selected)
//
//        }
//
//
//
//
//
//
//
//
//        //val firstDate = dao_access.getFirstRecordedDate()
//        var counter = 0
//
//
//        confirmButton.setOnClickListener {
//
//            val selectedOption_Workout = wKOSpinner.selectedItem.toString()
//
//            val W_C = selectedOption_Workout == "Cardiovascular"
//            val W_Y = selectedOption_Workout == "Yoga"
//            val W_S = selectedOption_Workout == "Strength"
//
//
//            if(wKOSpinner != null){
//                val adapter = ArrayAdapter(requireContext(),
//                    android.R.layout.simple_spinner_item, spinner2)
//                wKOSpinner.adapter = adapter
//            }
//
//
//
//            //Change boolean to true
//            //selectedDate.da
//
//            lifecycleScope.launch {
//            if (selectedOption_Workout == "Unselected") {
//                counter += 1
//            }
//            //Add to database
//
//
//                val testthedate = PlanFragment()
//                //val testvalue = testthedate.
//
//
//                //Step goal set
//            val inputNum = selectedSteps.toIntOrNull()
//            if (inputNum == null || inputNum < 1) {
//                selectedSteps = "0"
//                dao_access.updateStepGoal("PlanFrag's date here", 0)
//                //Plan Fragment Steps is Unset
//                counter += 1
//            }else{
//                dao_access.updateStepGoal("PlanFrag's date here", inputNum)
//
//
//            }
//
//
//            //Go back
//            if (counter == 3) {
//                //reset
//            } else {
//
//
//                lifecycleScope.launch {
//
//
//                }
//
//
//
//
//
//                requireFragmentManager().popBackStack()
//            }
//        }
//        }
//
//    }



}