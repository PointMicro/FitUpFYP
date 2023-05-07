package com.example.fypfinal

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room.databaseBuilder
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.*
import kotlin.text.*


class PlanFragment : Fragment() {


    lateinit var goalButton: Button
    lateinit var workout_text: TextView
    lateinit var steps_text: TextView
    lateinit var resetButton: Button
    lateinit var workoutSpinner: Spinner
    lateinit var stepsNum: EditText
    private val eventMap = mutableMapOf<String, String>()
    lateinit var  compactCalendarView: CompactCalendarView
    lateinit var monthTextView: TextView
    lateinit var goal_step_text: TextView
    lateinit var goal_wko_text: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.calendar_new, container, false) as ViewGroup

        // Inflate the second layout file and add its views to the first layout
//        val additionalLayout = inflater.inflate(R.layout.goal, container, false)
//        rootView.addView(additionalLayout)

        goalButton = rootView.findViewById(R.id.addGoalButton)
        resetButton = rootView.findViewById(R.id.resetbutton)
        workout_text = rootView.findViewById(R.id.wkg_textview)
        steps_text= rootView.findViewById(R.id.sg_textview)
         compactCalendarView = rootView.findViewById(R.id.compactcalendar_view)
        monthTextView = rootView.findViewById(R.id.idTVDate)

        return rootView}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val currentMonth = DateFormatSymbols().months[Calendar.getInstance().get(Calendar.MONTH)]
        monthTextView.text = currentMonth

        //By default its disabled
        resetButton.isEnabled = false
        resetButton.visibility = View.INVISIBLE
        workout_text.visibility = View.INVISIBLE
        steps_text.visibility = View.INVISIBLE

        val calendar_db = databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access_calendar = calendar_db.calendarDao()


        /*
        Add database dates to list
         */
        val dateRaw = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2023-04-01")
        val x = Calendar.getInstance()
        x.add(Calendar.MONTH, 6)
        val end_date = x.time //6 Month Later Date
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date_lists = mutableListOf<String>()
        var date_tracked = dateRaw

        while (date_tracked.before(end_date)) {
            val start_date = formatter.format(date_tracked)
            date_lists.add(start_date)
            x.add(Calendar.DAY_OF_MONTH, 1)
            date_tracked = x.time
        }


        /*
        Add database dates as millis to list
         */
        val calendar_m = Calendar.getInstance()
        calendar_m.set(2023, Calendar.APRIL, 1) // Set the starting date to April 1st, 2023
        val timeInMillis = calendar_m.timeInMillis // Get the time in millis for the starting date
        val dates = mutableListOf<Long>() // Create an empty list to hold the dates

        for (i in 1..6) { // Loop over the next 6 months
            calendar_m.add(Calendar.MONTH, 1) // Add 1 month to the current date
            val dateInMillis = calendar_m.timeInMillis // Get the time in millis for the current date
            dates.add(dateInMillis) // Add the current date to the list
        }

        var clickedDate: Date? = null

        // Set a listener for when a date is clicked
        compactCalendarView.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                lifecycleScope.launch {
                clickedDate = dateClicked


                val convertedEE = formatDateEE(dateClicked) //dateclicked in yyyy-MM-dd format
                if(dao_access_calendar.isGoalTrue(convertedEE)){

                    goalButton.isEnabled = false
                    goalButton.visibility = View.INVISIBLE
                    resetButton.isEnabled = true
                    resetButton.visibility = View.VISIBLE
                    workout_text.visibility = View.VISIBLE
                    steps_text.visibility = View.VISIBLE

                    val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.UK)
                    val date = inputDateFormat.parse(dateClicked.toString())
                    val timeInMillis = date.time

                    val eventData = dao_access_calendar.getTrueSelection(convertedEE) + ":" + dao_access_calendar.getStepGoals(convertedEE).toString() //fix getworkoutgoals
                    val workout = eventData.split(":")[0]
                    val steps = eventData.split(":")[1].toInt()
                    compactCalendarView.addEvent(Event(Color.BLUE,timeInMillis,workout + " "+ steps.toString()))

                    workout_text.text = "Workout Goal: " + workout
                    steps_text.text = "Steps for the day: " + steps.toString()

                }


                }


                //its in millis string format
                val events = compactCalendarView.getEvents(dateClicked)
                if (events.isNotEmpty()) {
                    val eventNames = events.map { it.data as String }
                    val value = dateClicked
                    val message = "Events for ${dateClicked}: ${eventNames.joinToString(", ")}"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                    //Show the views and stuff
                    goalButton.visibility = View.INVISIBLE
                    goalButton.isEnabled = false

                    //Text Views and Reset are visible
                    resetButton.isEnabled = true
                    resetButton.visibility = View.VISIBLE
                    workout_text.visibility = View.VISIBLE
                    steps_text.visibility = View.VISIBLE

                } else {
                        //dateClicked is EEE MMM dd HH:mm:ss 'GMT'Z yyyy
                    Toast.makeText(requireContext(), "No events for ${dateClicked}", Toast.LENGTH_SHORT).show()
                    //Keep it the same
                    goalButton.visibility = View.VISIBLE
                    goalButton.isEnabled = true

                    //Text Views and Reset are visible
                    resetButton.isEnabled = false
                    resetButton.visibility = View.INVISIBLE
                    workout_text.visibility = View.INVISIBLE
                    steps_text.visibility = View.INVISIBLE
                }

            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                val currentMonth = DateFormatSymbols().months[compactCalendarView.firstDayOfCurrentMonth.month]
                monthTextView.text = currentMonth
            }
        })



        goalButton.setOnClickListener {

            clickedDate?.let { date ->
                // Use the date here
                //val inputDateString = "Fri Apr 28 00:00:00 GMT+-1:00 2023"
                val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
                val date2 = inputDateFormat.parse(date.toString())
                val timeInMillis = date2.time //clickedDate in millis

                val convertedgoalEE = formatDateEE(date) //clicked date in yyyy-MM-dd format



                val builder = AlertDialog.Builder(requireContext())
                val inflater = LayoutInflater.from(requireContext())
                val dialogLayout = inflater.inflate(R.layout.goal, null)

                workoutSpinner = dialogLayout.findViewById(R.id.workout_spinner)
                stepsNum = dialogLayout.findViewById(R.id.steps_edittext)
                goal_step_text = dialogLayout.findViewById(R.id.steps_textview)
                goal_wko_text = dialogLayout.findViewById(R.id.workout_textview)


                val options = listOf("Unselected", "Cardiovascular", "Strength", "Yoga")
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                workoutSpinner.adapter = adapter

                builder.setView(dialogLayout).setPositiveButton("Confirm") { dialogInterface, i ->

                    goal_wko_text.visibility = View.VISIBLE
                    goal_step_text.visibility = View.VISIBLE
                    workoutSpinner.visibility = View.VISIBLE
                    stepsNum.visibility = View.VISIBLE


                    val calendar_db = databaseBuilder(requireContext(),
                        CalendarDatabase::class.java, "calendar_db").build()
                    val dao_access = calendar_db.calendarDao()



                    val selectedWorkout = workoutSpinner.selectedItem.toString()
                    var enteredSteps = stepsNum.text.toString()
                    // do something with selected workout and entered steps


                    //Add the dates workout to db for that day
                    val workout_C = selectedWorkout == "Cardiovascular"
                    val workout_Y = selectedWorkout == "Yoga"
                    val workout_S = selectedWorkout == "Strength"
                    Log.d("Date String", "Here: " + convertedgoalEE)
                    if(enteredSteps.isNullOrEmpty() || enteredSteps.toInt() < 1){
                        enteredSteps = "0"
                    }
                    val plan = Plan(convertedgoalEE, goals_selected = true, W_Y = workout_Y, W_S = workout_S, W_C = workout_C, enteredSteps.toInt() )
                    lifecycleScope.launch {  dao_access.insertFitnessPlan(plan) }


                    //Set the text
                    lifecycleScope.launch {
                        val imported_workout = dao_access.getTrueSelection(convertedgoalEE)
                        workout_text.setText("Workout Goal: " + imported_workout) //is null for some reason
                        steps_text.setText("Steps Goal: " + enteredSteps.toInt())
                    }
                    lifecycleScope.launch {

                        val ed1 = dao_access_calendar.getTrueSelection(convertedgoalEE)
                        val ed2 = dao_access_calendar.getStepGoals(convertedgoalEE)?.toString() ?: "0"
                        val eventData = ed1 + ":" + ed2
                        val workout = eventData.split(":")[0]
                        val steps = eventData.split(":")[1].toInt()
                        compactCalendarView.addEvent(Event(Color.BLUE,timeInMillis,workout + " "+ steps.toString()))
                        goalButton.isEnabled = false
                        goalButton.visibility = View.INVISIBLE
                        resetButton.isEnabled = true
                        resetButton.visibility = View.VISIBLE
                        workout_text.visibility = View.VISIBLE
                        steps_text.visibility = View.VISIBLE

                        workout_text.text = "Workout Goal: " + workout
                        steps_text.text = "Steps Goal: " + steps.toString()
                    }
                    goal_wko_text.visibility = View.INVISIBLE
                    goal_step_text.visibility = View.INVISIBLE
                    workoutSpinner.visibility = View.INVISIBLE
                    stepsNum.visibility = View.INVISIBLE
                }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }


        }

        resetButton.setOnClickListener {
            clickedDate?.let { date ->

                val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
                val date2 = inputDateFormat.parse(date.toString())
                val timeInMillis = date2.time //clickedDate in millis

                val convertedgoalEE = formatDateEE(date) //clicked date in yyyy-MM-dd format

                val convertedDate = formatDateEE(date)
                val calendar_db = databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
                val dao_access = calendar_db.calendarDao()

                lifecycleScope.launch {
                    dao_access.clearFitnessPlans(convertedDate)
                    val updated_Plan = Plan(convertedgoalEE, false, false, false, false, null)
                    dao_access.insertFitnessPlan(updated_Plan)
                    dao_access.updateGoalsFalse(convertedgoalEE)

                   // val ed1 = dao_access_calendar.getTrueSelection(convertedgoalEE)
                 //   val ed2 = dao_access_calendar.getStepGoals(convertedgoalEE)?.toString() ?: "0"
                 //   val eventData = ed1 + ":" + ed2
                //    val workout = eventData.split(":")[0]
                 //   val steps = eventData.split(":")[1].toInt()

                compactCalendarView.removeEvent(Event(Color.BLUE,timeInMillis))   //goals today is false
                }

                // Update the UI to reflect that the goal has been removed
                workout_text.visibility = View.INVISIBLE
                steps_text.visibility = View.INVISIBLE
                goalButton.isEnabled = true
                goalButton.visibility = View.VISIBLE
                resetButton.isEnabled = false
                resetButton.visibility = View.INVISIBLE
            }
        }

    }
    fun formatDateEE(inputDateString: Date?): String {
        val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US)
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        val date = inputDateFormat.parse(inputDateString.toString())
        return outputDateFormat.format(date)
    }


}