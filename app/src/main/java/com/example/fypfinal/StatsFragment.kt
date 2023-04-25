package com.example.fypfinal

import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AlignmentSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StatsFragment : Fragment() {

    lateinit var stepsTV: TextView
    lateinit var workoutTV: TextView
    lateinit var miscText: TextView
    lateinit var bmiText: TextView
    lateinit var surveyTextView: TextView
    val todayUnformatted = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = todayUnformatted.format(formatter)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user_db = Room.databaseBuilder(requireContext(), UserDatabase::class.java,
            "userDB").build()
        var dao_access_user = user_db.UserDao()

        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access_calendar = calendar_db.calendarDao()




        //Could be a loading info screen



        //Get the name, gender height and width.
            //Replace "Survey Options" with the database values (includes new line)

            //BMI is calculated

            //Common is replaced with the most common workout


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.stats_precreation, container, false)

        stepsTV = view.findViewById(R.id.steps_textview)
        bmiText = view.findViewById(R.id.BMI_TV)
        workoutTV = view.findViewById(R.id.commonWorkout_TV)
        miscText = view.findViewById(R.id.misc_TV)
        surveyTextView = view.findViewById(R.id.surveyTextView)

        val user_db = Room.databaseBuilder(requireContext(), UserDatabase::class.java,
            "userDB").build()
        var dao_access_user = user_db.UserDao()

        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access_calendar = calendar_db.calendarDao()

        lifecycleScope.launch {

            /* Initialising the database variables */

            var name = dao_access_user.getName()
            var gender = dao_access_user.getGender().toString()
            var height = dao_access_user.getHeight()!!
            var weight = dao_access_user.getWeight()!!
            var injury = dao_access_user.getInjury()
            var fitness_goal = dao_access_user.getTrueFitGoal()
            var workout_yoga_count = dao_access_calendar.allYogaCount()
            var workout_strength_count = dao_access_calendar.allStrengthCount()
            var workout_cardio_count = dao_access_calendar.allCardioCount()

            /* The highest count of the 3*/
            val highestWorkout = getHighestWorkoutCount(workout_cardio_count,
                workout_yoga_count, workout_strength_count)
            var total_workouts = dao_access_calendar.allWorkoutsCount()
            var total_steps = 0 //dao_access_calendar.totalSteps()
            var today_steps = 0 //dao_access_calendar.getStepsTaken(today)
            var bmi = calculateBMI(weight, height)

            /* Changing the Text Views to adjust to the database */

            stepsTV.text = "Total Steps: $total_steps\nSteps Today: $today_steps"
            workoutTV.text = "Most Common Workout: \n" + highestWorkout
            bmiText.text = "BMI: " + bmi +"\n" + bmiResult(bmi)
            miscText.text = ""
           // surveyTextView.text = "Survey Results:\n" + "Name: " + name +"\n" + "Gender: "+ gender +
           //         "\n" + "Height: "

            val builder = SpannableStringBuilder()

            val survey_text_title = "Survey Results:\n" + "\n"
            val nameTitle = "Name:\n"
            val genderTitle = "Gender:\n"
            val heightTitle = "Height:\n"
            val weightTitle = "Weight:\n"
            val fitGoalTitle = "Main Goal:\n"
            val centerAlignment = Layout.Alignment.ALIGN_CENTER
            val titleStyle = StyleSpan(Typeface.BOLD)
            val titleSpannable = SpannableString(survey_text_title)

            titleSpannable.setSpan(AlignmentSpan.Standard(centerAlignment), 0, survey_text_title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleSpannable.setSpan(titleStyle, 0, survey_text_title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val nameSpannable = SpannableString(name+ "\n" + "\n")
            nameSpannable.setSpan(AlignmentSpan.Standard(centerAlignment), 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val genderSpannable = SpannableString(gender+ "\n" + "\n")
            genderSpannable.setSpan(AlignmentSpan.Standard(centerAlignment), 0, gender.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val heightSpannable = SpannableString(height.toString()+ "cm" + "\n" + "\n")
            heightSpannable.setSpan(AlignmentSpan.Standard(centerAlignment), 0, height.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val weightSpannable = SpannableString(weight.toString()+ "kg" + "\n" + "\n")
            weightSpannable.setSpan(AlignmentSpan.Standard(centerAlignment), 0, weight.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val fitGoalSpannabler = SpannableString(fitness_goal?.let { changeFGText(it) } + "\n" + "\n")
            if (fitness_goal != null) {
                fitGoalSpannabler.setSpan(AlignmentSpan.Standard(centerAlignment), 0, fitness_goal.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }


            builder.append(titleSpannable)
            builder.append(nameTitle)
            builder.append(nameSpannable )
            builder.append(genderTitle)
            builder.append(genderSpannable)
            builder.append(heightTitle)
            builder.append(heightSpannable)
            builder.append(weightTitle)
            builder.append(weightSpannable)
            builder.append(fitGoalTitle)
            builder.append(fitGoalSpannabler)






// Setting the final text to a TextView
            surveyTextView.setText(builder, TextView.BufferType.SPANNABLE)




            Log.d("OVERRIDE CHECK", "onCreateView Loaded")

        }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


      //  Log.d("Name after lifecycle: ", "HERE: " + name)

    }

    fun getHighestWorkoutCount(cardio: Int, yoga: Int, strength: Int): String {
        val highestCount = maxOf(cardio, yoga, strength)
        return when {
            cardio == highestCount && yoga == highestCount && strength == highestCount ->
                "No most common"
            cardio == highestCount && yoga == highestCount -> "Cardio, Yoga"
            cardio == highestCount && strength == highestCount -> "Cardio, Strength"
            yoga == highestCount && strength == highestCount -> "Yoga, Strength"
            cardio == highestCount -> "Cardiovascular"
            yoga == highestCount -> "Yoga"
            strength == highestCount -> "Strength"
            else -> ""
        }
    }

    fun calculateBMI(weight: Int, height: Int): String {
        val convertedHeight = height / 100.0
        val bmi = weight / Math.pow(convertedHeight, 2.0)
        val formattedBmi = String.format("%.2f", bmi)
        return formattedBmi
    }


    fun bmiResult(bmi: String): String {
        if (bmi.toDouble() > 35){
            return "Extremely Obese"
        }else if (bmi.toDouble() < 35 && bmi.toDouble() >= 30){
            return "Obese"
        }else if(bmi.toDouble() >= 25 && bmi.toDouble() < 30 ){
            return "Overweight"
        }else if(bmi.toDouble() >= 18.5 && bmi.toDouble() < 25){
            return "Normal"
        }else{
            return "Underweight"
        }
    }

    fun changeFGText(fitness_goal: String): String{
        if (fitness_goal == "FG_MB"){
            return "Muscle Building"
        }else if(fitness_goal == "FG_WL"){
            return "Weight Loss"
        }else if(fitness_goal == "FG_Other"){
            return "General"
        }else{
            return "No Fitness Goal"
        }


    }



}