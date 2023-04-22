package com.example.fypfinal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.fypfinal.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), SensorEventListener {
    //to call a textview you need com.example.fypfitness.r.layout.fragmenthome


    val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
//val sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]


    /*General variables for the step counter */
    private lateinit var sensorManager: SensorManager
    private var current = 0f
    private var started = false
    private var previous = 0f
    private lateinit var yesterdayCountDisplay: TextView
    private lateinit var unavailableSC: TextView
    private lateinit var todayCountDisplay: TextView
    private lateinit var hr_button: Button
    private lateinit var calendar_DB: CalendarDatabase
//    /* Quote Text variable */
//    private lateinit var qText: TextView


    /*Binding*/
    private lateinit var binding : ActivityHomeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { createCalendarDB() }




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // val HEART_CONTAINER_ID = 0x7f090222
        val view = inflater.inflate(com.example.fypfinal.R.layout.test_fragment, container, false)


        val quoteBannerText = view.findViewById<TextView>(com.example.fypfinal.R.id.quoteText)

        yesterdayCountDisplay = view.findViewById<TextView>(com.example.fypfinal.R.id.yesterday_step_num)
        unavailableSC = view.findViewById<TextView>(com.example.fypfinal.R.id.unimplemented_textSC)
        todayCountDisplay = view.findViewById<TextView>(com.example.fypfinal.R.id.today_step_num)


        return view
        //return inflater.inflate(com.example.fypfitness.R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        hr_button = view.findViewById(com.example.fypfinal.R.id.HRM_Button)

        sharedViewModel.isHrButtonVisible.observe(viewLifecycleOwner, { isVisible ->
            hr_button.isVisible = isVisible
        })




        hr_button.setOnClickListener {
            val fragment = HeartView()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.home_container, fragment)
            transaction.addToBackStack(null)
            sharedViewModel.updateHrButtonVisibility(false)
            transaction.commit()
        }


    }



    override fun onResume() {
        super.onResume()
        started = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            //Replaces Steps with No sensor available
            unavailableSC.visibility = View.VISIBLE
            yesterdayCountDisplay.setText("")
            todayCountDisplay.setText("")

        }else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

            //
        }


    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER && started) {
            current = event.values[0]
            val currentSteps = current.toInt() - previous.toInt()
            todayCountDisplay.text = currentSteps.toString()



            // Debug logging statements
            Log.d("StepCounter", "Received sensor data: ${event.values[0]}")
            Log.d("StepCounter", "Previous step count: $previous")
            Log.d("StepCounter", "Current step count: $current")
            Log.d("StepCounter", "Current step count delta: $currentSteps")
        }





    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun randomText(): String {
        // Create a list of sentences to choose from
        val quotes = arrayListOf<String>(
            "Your body can stand almost anything. It's your mind that you have to convince.",
            "The only bad workout is the one that didn't happen.",
            "Exercise is not a punishment. It's a celebration of what your body can do.",
            "Exercise is the best way to boost your mood, increase energy, and improve your health.",
            "Your body is your home. Make it a place you want to live in.",
            "Fitness is not a size, it's a feeling.",
            "Exercise is the best way to improve your energy levels and productivity.",
            "You can't out-train a bad diet.",
            "The pain you feel today will be the strength you feel tomorrow.",
            "Civilize the mind but make savage the body"


        )

        val r = kotlin.random.Random.nextInt(quotes.size)
        val randomString = quotes[r]

        return randomString
    }

    fun resetSteps() {
        //On date change
        //Todays steps is added to a calendar database
        //Yesterdays steps become the previous todays steps
        //Todays steps becomes 0
    }

    private fun buildCalendarDB(): CalendarDatabase{
        return Room.databaseBuilder(requireContext().applicationContext,
            CalendarDatabase::class.java,"calendar_db").build()
    }

    private suspend fun createCalendarDB(){
        //Check if DB exists
        val database_Name = "calendar_db"
        val databaseFilePath = requireContext().getDatabasePath(database_Name)

        //then
        //Database created
        if(databaseFilePath.exists()){

//            val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java,
//                "calendar_db").build()
//            var dao_access = calendar_db.calendarDao()

            val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
            val dao_access = calendar_db.calendarDao()



        }else {

            calendar_DB = buildCalendarDB()
            val calendar_dao_access = calendar_DB.calendarDao()


            /*Gets the current date and a year ahead from it
            * Then adds the values to the database for the Calendar */
            val dateRaw = Date() //Today date
            val x = Calendar.getInstance()
            x.add(Calendar.YEAR, 1)
            val end_date = x.time //1 Year Later Date
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


            val date_lists = mutableListOf<String>()
            var date_tracked = dateRaw

            while (date_tracked.before(end_date)) {
                val start_date = formatter.format(date_tracked)
                date_lists.add(start_date)
                val calendar = Calendar.getInstance()
                calendar.time = date_tracked
                calendar.add(Calendar.DATE, 1)
                date_tracked = calendar.time
                val calendarAdd = Calendar2(date = start_date)
                calendar_dao_access.insertCalendar(calendarAdd)
            }

        }

    }


    /* Uses the current date and adds 1 year to it */
//    private fun yearAhead(date: Date): Date{
//        val c = Calendar.getInstance()
//        c.add(Calendar.YEAR, 1)
//        return c.time
//    }




}