package com.example.fypfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.widget.CalendarView.OnDateChangeListener
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*


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
    var Date = ""
    var formattedDate = ""
    var hello = ""

    //When you click this tab it opens up a Calendar view of the dates recorded on the calendar
        //When you click a date. it brings up that dates info and has a box view under it labelled
                // with "Goals" at the top of the view (im thinking dark grey green border
            //



    //C_DB items
        // doingGoalforThisDay - Boolean
        // duration_goal -Int? that can be nullable [DURATION MAY NOT BE NEEDED]
        // workout_goal - Int?
        // steps_goal - Int?
            //goals are null unless dgftd is true
                //duration could be calculated automatically when selecting a workout


    //Calendar here.
        //Upon opening the homefragment
            //It retrieves the fragment.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    //On click date
        //An AlertDialog(?) or something bigger pops up
            //Displays the info with Date as title




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


        resetButton.isEnabled = false

        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access = calendar_db.calendarDao()

        // Set the initial date to db date
        lifecycleScope.launch{
            val firstDate = dao_access.getFirstRecordedDate()
            val lastDate = dao_access.getLastRecordedDate()


            val initialDate = dateStringToLong(firstDate)
            calendarView.date = initialDate

            // Set the maximum date to one year from the initial date
            val maxDate = dateStringToLong(lastDate)
            calendarView.maxDate = maxDate

        }




        // on below line we are adding set on
        // date change listener for calendar view.
        calendarView
            .setOnDateChangeListener(
                OnDateChangeListener { view, year, month, dayOfMonth ->

                    Date = (dayOfMonth.toString()  + "-" +  (month + 1)  + "-" + year )
                     formattedDate = "${year}-${(month + 1).toString().padStart(2, 
                        '0')}-${dayOfMonth.toString().padStart(2, '0')}"
                })



        goalButton.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            val inflater = LayoutInflater.from(requireContext())
            val dialogLayout = inflater.inflate(R.layout.goal, null)

            builder.setView(dialogLayout)
                .setPositiveButton("Confirm") { dialogInterface, i ->
                    // handle confirm button click

                    val calendar_db = Room.databaseBuilder(requireContext(),
                        CalendarDatabase::class.java, "calendar_db").build()
                    val dao_access = calendar_db.calendarDao()


                    workoutSpinner = view.findViewById(R.id.workout_spinner)
                    stepsNum = view.findViewById(R.id.steps_edittext)
                    val spinner2 = resources.getStringArray(R.array.Workout)

                    if(workoutSpinner != null){
                        val adapter = ArrayAdapter(requireContext(),
                            android.R.layout.simple_spinner_item, spinner2)
                        workoutSpinner.adapter = adapter
                    }
                    val selectedWorkout = workoutSpinner.selectedItem.toString()
                    val enteredSteps = stepsNum.text.toString()
                    // do something with selected workout and entered steps

                    lifecycleScope.launch{
                        //Makes the date remove add goal button
                       // dao_access.updateGoalsSelected(Date)

                        //Add the dates step count to db for that day
                        //dao_access.updateStepGoal(Date, enteredSteps.toInt())

                        //Add the dates workout to db for that day
                        val workout_C = selectedWorkout == "Cardiovascular"
                        val workout_Y = selectedWorkout == "Yoga"
                        val workout_S = selectedWorkout == "Strength"
                        Log.d("Date String", "Here: " + formattedDate)
                        val plan = Plan(formattedDate, goals_selected = true, W_Y = workout_Y, W_S = workout_S, W_C = workout_C, enteredSteps.toInt() )
                        dao_access.updateWorkoutBooleans(plan)

                    }

                    //"Add a goal" is disabled and invisible
                    goalButton.visibility = View.INVISIBLE
                    goalButton.isEnabled = false

                    //Text Views and Reset are visible
                    resetButton.isEnabled = true
                    resetButton.visibility = View.VISIBLE
                    workout_text.visibility = View.VISIBLE
                    steps_text.visibility = View.VISIBLE
                    duration_text.visibility = View.VISIBLE

                    //Set the text
                    lifecycleScope.launch {
                        val imported_workout = dao_access.getTrueSelection(formattedDate)
                        workout_text.setText("Workout Goal: " + imported_workout)
                        steps_text.setText("Steps Goal: " + enteredSteps.toInt())
                    }

                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    // handle cancel button click
                }
            val alertDialog = builder.create()
            alertDialog.show()

            //val isGoalsTodaySelected: Boolean = dao_access.isGoalTrue(formattedDate)

           // val imported_steps = dao_access.getStepGoals(formattedDate)


        }




//to do
        //Make the Calendar the correct date according to DB
        //Conenct to DB
                            //BELOW MAY CHANGE FOR  FIRST LAUNCH
                            // Connects to the DB and sets the goal button visiblity according to the Boolean (for loop maybe)
        //When you click add goal:

        //it changes the boolean and removes the Add goal button and makes an alertdialog popup
                //the popup lets you change the details. Not changing it leaves it as unset.

                //No edit button, only reset and add


    }


    fun dateStringToLong(dateString: String?): Long {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(dateString)
        return date?.time ?: 0
    }





}