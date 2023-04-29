package com.example.fypfinal

import SliderAdapter
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.fypfinal.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), SensorEventListener {
    //to call a textview you need com.example.fypfitness.r.layout.fragmenthome


    val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }


//Todo
    //HomePage
        //Omit List heart rate and just keep the latest as an int
            //Remove the Back button from HRM and just tell user to press home
            //Remove add to DB button, it adds automatically
            //Latest HR is is saved in database and replaces number already in it
            //Quote and Notification tab at the top
            //Make it say your name
            //Fix steps counter(it does the total steps for the device, not start from 0) - may be complicated
            //Add Latest HR to replace HRM number

    //Misc in stats has:
        //calories burned in total
        //Count for each workout
   //Settings
        //Remove dark mode button and replace with privacy policy
        //Fix change details button
        //Delete account button
        //Add the image to settings
        //Make the name change in settings page
    //Loading
        //Add more images (add a b&w muscle pic)
        //Add more inspirational quotes
        //Welcome the user if they have a DB
        //Terms of Service
    //Train page
        //Make calories burned center top and remove the ugly view around it
        //Move the tips to above the buttons and make it bold and purple
        //If enough time, add more videos and make them randomly appear on carousel
        //Make a tip telling user to complete their goals!


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

    /* Carousel stuff */

    // on below line we are creating a variable
    // for our array list for storing our images.
    lateinit var imageUrl: ArrayList<Int>

    // on below line we are creating
    // a variable for our slider view.
    lateinit var sliderView: SliderView

    // on below line we are creating
    // a variable for our slider adapter.
    lateinit var sliderAdapter: SliderAdapter



    lateinit var videoView: VideoView



    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { createCalendarDB() }



    }
//Weather section. Use photosvhop to make pictures depending on the weather
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
//talk abouyt how it was a vid to gif to vid

        sliderView = view.findViewById(R.id.news_section)



        // on below line we are initializing
        // our image url array list.
        imageUrl = ArrayList()
      //  imageUrl.add(R.raw.vid)
       // imageUrl.add("https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1")
       // imageUrl.add(R.raw.vidtwo)



        // on below line we are initializing our
        // slider adapter and adding our list to it.
        sliderAdapter = SliderAdapter( imageUrl)

        // on below line we are setting auto cycle direction
        // for our slider view from left to right.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        // on below line we are setting adapter for our slider.
        sliderView.setSliderAdapter(sliderAdapter)

        // on below line we are setting scroll time
        // in seconds for our slider view.
        sliderView.scrollTimeInSec = 3

        // on below line we are setting auto cycle
        // to true to auto slide our items.
        sliderView.isAutoCycle = false

        // on below line we are calling start
        // auto cycle to start our cycle.
        //sliderView.startAutoCycle()



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


            /*Gets the set date and 6 months ahead from it
            * Then adds the values to the database for the Calendar */
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
                val calendar = Calendar.getInstance()
                calendar.time = date_tracked
                calendar.add(Calendar.DATE, 1)
                date_tracked = calendar.time
                val calendarAdd = Calendar2(date = start_date)
                calendar_dao_access.insertCalendar(calendarAdd)
            }

        }

    }


//    fun weatherTemp(){
//        val queue = Volley.newRequestQueue(this)
//        val url: String = weather_url1
//
//
//
//    }




}