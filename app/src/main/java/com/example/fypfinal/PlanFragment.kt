package com.example.fypfinal

import android.os.Bundle
import android.util.Log
import com.example.fypfinal.R
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.widget.CalendarView.OnDateChangeListener
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.*
import kotlin.text.*
import kotlin.io.*



class PlanFragment : Fragment() {


    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    lateinit var goalButton: Button
    lateinit var workout_text: TextView
    lateinit var steps_text: TextView
    lateinit var duration_text: TextView
    lateinit var resetButton: Button
    lateinit var workoutSpinner: Spinner
    lateinit var stepsNum: EditText
    var calendar_selected_date: String = ""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    operator fun ClosedRange<LocalDate>.iterator() : Iterator<LocalDate>{
        return object: Iterator<LocalDate> {
            private var next = this@iterator.start
            private val finalElement = this@iterator.endInclusive
            private var hasNext = !next.isAfter(this@iterator.endInclusive)

            override fun hasNext(): Boolean = hasNext
            override fun next(): LocalDate {
                val value = next
                if(value == finalElement) {
                    hasNext = false
                }
                else {
                    next = next.plusDays(1)
                }
                return value
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_plan, container, false) as ViewGroup

        // Inflate the second layout file and add its views to the first layout
        val additionalLayout = inflater.inflate(R.layout.goal, container, false)
        rootView.addView(additionalLayout)

        return rootView}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // initializing variables of
        // list view with their ids.
        dateTV = view.findViewById(R.id.idTVDate)
        calendarView = view.findViewById(R.id.calendarView)
        goalButton = view.findViewById(R.id.addGoalButton)
        resetButton = view.findViewById(R.id.resetbutton)
        workout_text = view.findViewById(R.id.wkg_textview)
        steps_text= view.findViewById(R.id.sg_textview)
        duration_text = view.findViewById(R.id.duration_textview)

        //By default its disabled
        resetButton.isEnabled = false
        resetButton.visibility = View.INVISIBLE
        workout_text.visibility = View.INVISIBLE
        steps_text.visibility = View.INVISIBLE
        duration_text.visibility = View.INVISIBLE

        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access = calendar_db.calendarDao()

        // Set the initial date to db date
        lifecycleScope.launch {
            var firstDate = dao_access.getFirstRecordedDate()
            var lastDate = dao_access.getLastRecordedDate()

            val initialDate = dateStringToLong(firstDate)
            calendarView.date = initialDate

            // Set the maximum date to one year from the initial date
            val maxDate = dateStringToLong(lastDate)
            calendarView.maxDate = maxDate

            Log.d("Date String", "Start Date: " + firstDate + ", End Date: " + lastDate)
            val plans = dao_access.getDatesWithSelectedGoals()
            val startDate = LocalDate.parse(firstDate)
            val endDate = LocalDate.parse(lastDate)
            val dateRange = startDate..endDate

            var currentDateFound = false
            for(date in plans){
                val planDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date) // parse date from the plans list into a Date object
                if(planDate.toString() in firstDate..lastDate){
                    currentDateFound = true
                    break
                }

            }
            if (currentDateFound){

                goalButton.visibility = View.INVISIBLE
                goalButton.isEnabled = false

                //Text Views and Reset are visible
                resetButton.isEnabled = true
                resetButton.visibility = View.VISIBLE
                workout_text.visibility = View.VISIBLE
                steps_text.visibility = View.VISIBLE
                duration_text.visibility = View.VISIBLE

            }else{
                goalButton.visibility = View.VISIBLE
                goalButton.isEnabled = true

                //Text Views and Reset are visible
                resetButton.isEnabled = true
                resetButton.visibility = View.INVISIBLE
                workout_text.visibility = View.INVISIBLE
                steps_text.visibility = View.INVISIBLE
                duration_text.visibility = View.INVISIBLE

            }



            // Set appearance of currently selected date
           // updateSelectedDateAppearance(calendar_selected_date, dao_access)

            // Check again on date change
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendar_selected_date = "${year}-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                //updateSelectedDateAppearance(calendar_selected_date, dao_access)
            }
        }




        goalButton.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            val inflater = LayoutInflater.from(requireContext())
            val dialogLayout = inflater.inflate(R.layout.goal, null)

            workoutSpinner = dialogLayout.findViewById(R.id.workout_spinner)
            stepsNum = dialogLayout.findViewById(R.id.steps_edittext)
            val spinner2 = resources.getStringArray(R.array.Workout)

            val options = listOf("Unselected", "Cardiovascular", "Strength", "Yoga")
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            workoutSpinner.adapter = adapter

            builder.setView(dialogLayout).setPositiveButton("Confirm") { dialogInterface, i ->
                    // handle confirm button click



                    val calendar_db = Room.databaseBuilder(requireContext(),
                        CalendarDatabase::class.java, "calendar_db").build()
                    val dao_access = calendar_db.calendarDao()



                    val selectedWorkout = workoutSpinner.selectedItem.toString()
                    var enteredSteps = stepsNum.text.toString()
                    // do something with selected workout and entered steps


                        //Add the dates workout to db for that day
                        val workout_C = selectedWorkout == "Cardiovascular"
                        val workout_Y = selectedWorkout == "Yoga"
                        val workout_S = selectedWorkout == "Strength"
                        Log.d("Date String", "Here: " + calendar_selected_date)
                        if(enteredSteps.isNullOrEmpty() || enteredSteps.toInt() < 1){
                            enteredSteps = "0"
                        }
                        val plan = Plan(calendar_selected_date, goals_selected = true, W_Y = workout_Y, W_S = workout_S, W_C = workout_C, enteredSteps.toInt() )
                lifecycleScope.launch {  dao_access.insertFitnessPlan(plan) }


                    //Set the text
                lifecycleScope.launch {
                       val imported_workout = dao_access.getTrueSelection(calendar_selected_date)
                        workout_text.setText("Workout Goal: " + imported_workout) //is null for some reason
                        steps_text.setText("Steps Goal: " + enteredSteps.toInt())
                }

                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    // handle cancel button click
                }
            val alertDialog = builder.create()
            alertDialog.show()



            }
        }

    private fun updateSelectedDateAppearance(selectedDate: String, dao_access: CalendarDao2) {
        lifecycleScope.launch {
//            var firstDate = dao_access.getFirstRecordedDate()
//            var lastDate = dao_access.getLastRecordedDate()
        if (dao_access.isGoalTrue(selectedDate)) {
//            val startDate = LocalDate.parse(firstDate)
//            val endDate = LocalDate.parse(lastDate)
//            val dateRange = startDate..endDate
//            val plans = dao_access.getDatesWithSelectedGoals()
//            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

//            for (date in dateRange){
//                val formattedDate = date.format(formatter)

               // if(plans.contains(selectedDate) ){

                calendarView.dateTextAppearance = R.style.goal_selected
                //"Add a goal" is disabled and invisible
                goalButton.visibility = View.INVISIBLE
                goalButton.isEnabled = false

                //Text Views and Reset are visible
                resetButton.isEnabled = true
                resetButton.visibility = View.VISIBLE
                workout_text.visibility = View.VISIBLE
                steps_text.visibility = View.VISIBLE
                duration_text.visibility = View.VISIBLE
                } else {
                    calendarView.dateTextAppearance = R.style.default_style
                goalButton.visibility = View.INVISIBLE
                goalButton.isEnabled = false

                //Text Views and Reset are visible
                resetButton.isEnabled = true
                resetButton.visibility = View.VISIBLE
                workout_text.visibility = View.VISIBLE
                steps_text.visibility = View.VISIBLE
                duration_text.visibility = View.VISIBLE
                }
           // }
        }
    }

    fun dateStringToLong(dateString: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dateString)
        return date?.time ?: 0
        }

    }




