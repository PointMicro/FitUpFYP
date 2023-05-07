package com.example.fypfinal


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Environment
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
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Text


class HomeFragment : Fragment(), SensorEventListener {
    //to call a textview you need com.example.fypfitness.r.layout.fragmenthome


    val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var initialStepCount: Int = -1
    private var sessionStepCount: Int = 0



    /*General variables for the step counter */
    private lateinit var sensorManager: SensorManager
    private var current = 0f
    private var started = false
    private var previous = 0f
    private lateinit var unavailableSC: TextView
    private lateinit var todayCountDisplay: TextView
    private lateinit var hr_button: Button
    private lateinit var hr_text: TextView
    private lateinit var calendar_DB: CalendarDatabase
    private lateinit var weather_Tip: TextView


    /*Binding*/
    private lateinit var binding : ActivityHomeBinding


    //Weather stuff

    //Weatherbit API
    var api_key = "e1c4fb38c9cf4140b8c40e053e6de991"
    //URL for JSON
    var weather_url = ""
    private lateinit var city: TextView
    private  lateinit var temp: TextView
    private lateinit var desc: TextView
    private lateinit var country: TextView
    private lateinit var weatherimg: ImageView
    private lateinit var weatherCard: CardView
     var stepCounter: Int = 0





    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch { createCalendarDB() }


        //Start Weather
        obtainLocation()

    }
//Weather section. Use photosvhop to make pictures depending on the weather
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // val HEART_CONTAINER_ID = 0x7f090222
    val view = inflater.inflate(com.example.fypfinal.R.layout.test_fragment, container, false) as ViewGroup

    val sharedPreferences = activity?.getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
    sessionStepCount = sharedPreferences?.getInt("sessionStepCount", 0) ?: 0


        val quoteBannerText = view.findViewById<TextView>(com.example.fypfinal.R.id.quoteText)
        quoteBannerText.text = randomText()
        quoteBannerText.isSelected = true


    //Weather stuff
    weatherCard = view.findViewById(R.id.weather_card)
    city = view.findViewById(R.id.city_name_textView)
    temp = view.findViewById(R.id.temperature_textView)
    desc = view.findViewById(R.id.description_textView)
    country = view.findViewById(R.id.country_name_textView)
    weatherimg = view.findViewById(R.id.weather_img)
    hr_text = view.findViewById(R.id.hrm_test)
    weather_Tip = view.findViewById(R.id.weatherTip_TV)


    val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
    val dao_access = calendar_db.calendarDao()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = Calendar.getInstance().time
    val todayFormatted = dateFormat.format(today)

    lifecycleScope.launch {
        val hr_value = dao_access.getLatestHR(todayFormatted)
        hr_text.text = hr_value.toString() }



//step counter stuff
        unavailableSC = view.findViewById<TextView>(com.example.fypfinal.R.id.unimplemented_textSC)
        todayCountDisplay = view.findViewById<TextView>(com.example.fypfinal.R.id.today_step_num)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            //talk abouyt how it was a vid to gif to vid
        //talk about how i removed the carousel from the home page


        hr_button = view.findViewById(com.example.fypfinal.R.id.HRM_Button)

        sharedViewModel.isHrButtonVisible.observe(viewLifecycleOwner, { isVisible ->
            hr_button.isVisible = isVisible
        })

        sharedViewModel.isCardViewVisible.observe(viewLifecycleOwner,{ isVisible ->
            weatherCard.isVisible = isVisible
            city.isVisible = isVisible
            temp.isVisible = isVisible
            desc.isVisible = isVisible
            country.isVisible = isVisible
            weatherimg.isVisible = isVisible
        })


        hr_button.setOnClickListener {
            val fragment = HeartView()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.home_container, fragment)
            transaction.addToBackStack(null)
            sharedViewModel.updateHrButtonVisibility(false)
            sharedViewModel.updateCardViewVisibility(false)
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
            todayCountDisplay.setText("")

        }else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)

        }


    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {



        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount == -1) {
                // Store the initial step count if it hasn't been obtained yet
                initialStepCount = event.values[0].toInt()
            } else {
                // Calculate the number of steps taken since the initial count
                val currentSteps = event.values[0].toInt() - initialStepCount
                todayCountDisplay.text = currentSteps.toString()



                // Debug logging statements
                Log.d("StepCounter", "Received sensor data: ${event.values[0]}")
                Log.d("StepCounter", "Initial step count: $initialStepCount")
                Log.d("StepCounter", "Current step count: ${event.values[0].toInt()}")
                Log.d("StepCounter", "Current step count delta: $currentSteps")
            }
        }
    }





    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun randomText(): String {
        // Create a list of sentences to choose from
        val quotes = arrayListOf<String>(
            "You don't have to be great to start, but you have to start to be great.",
            "Fitness is not about being better than someone else. It's about being better than you used to be.",
            "Believe you can and you're halfway there.",
            "The only limits in life are the ones you make.",
            "Success is not final, failure is not fatal: It is the courage to continue that counts.",
            "Sweat is magic. Cover yourself in it daily to grant your wishes.",
            "Take care of your body. It's the only place you have to live.",
            "The difference between try and triumph is just a little umph!",
            "Strive for progress, not perfection.",
            "Motivation is what gets you started. Habit is what keeps you going.",
            "Success is not just about what you accomplish in your life, it's about what you inspire others to do.",
            "The body achieves what the mind believes.",
            "The pain of discipline is far less than the pain of regret.",
            "Make the most of yourself by fanning the tiny, inner sparks of possibility into flames of achievement.",
            "The only bad workout is the one you didn't do."

        )

        val r = kotlin.random.Random.nextInt(quotes.size)
        val randomString = quotes[r]

        return randomString
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




    fun getInfo() {


        var requestQueue = Volley.newRequestQueue(requireContext())
        // Instantiate the RequestQueue using the Volley library.
        // Define the URL of the weather API.
        val url: String = weather_url
        // Create a request for a string response from the API.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            // If the request succeeds, this function is called with the response string.
            { response ->
                // Parse the response string as a JSON object.
                val weatherObj = JSONObject(response)
                // Extract the "data" array from the JSON object.
                val weatherArr = weatherObj.getJSONArray("data")
                // Extract the first object from the "data" array.
                val weatherObj2 = weatherArr.getJSONObject(0)
                //Extract data from the weather array
                val weatherObj3 = weatherObj2.getJSONObject("weather")
                // Set the city name on the TextView
                var countrycode = "${weatherObj2.getString("country_code")}"
                city.text = "${weatherObj2.getString("city_name")}"
                country.text = getCountryName(countrycode)
                val description1  = "${weatherObj3.getString("description")}"
                desc.text = description1
                temp.text = "${weatherObj2.getString("temp")}" + "C"

                val (drawableId, weatherTip) = weatherImageAndTip(description1)
                weatherimg.setImageResource(drawableId)
                weather_Tip.text = weatherTip


            },
            // If the request fails, this function is called with an error message.
            { error ->
                // Set error messages for all the relevant TextViews
                country.text = "ERROR"
                desc.text = "ERROR"
                temp.text = "ERROR"
                weatherimg.setImageResource(R.drawable.error) //change green icon to error img
            })
        // Add the request to the RequestQueue to execute it.
        requestQueue.add(stringRequest)
    }

    fun getCountryName(countryCode: String): String? {
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }



    fun weatherImageAndTip(description: String): Pair<Int, String> {        return when (description) {
        "Thunderstorm with light rain" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with rain" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with heavy rain" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with light drizzle" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with drizzle" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with heavy drizzle" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Thunderstorm with hail" -> Pair(R.drawable.thunder, "Recommended to stay indoors")
        "Light drizzle" -> Pair(R.drawable.rain, "Bring an umbrella")
        "Drizzle" -> Pair(R.drawable.rain, "Bring an umbrella")
        "Heavy drizzle" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Light rain" -> Pair(R.drawable.rain, "Bring an umbrella")
        "Moderate rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Heavy rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Freezing rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Light shower rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Shower rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Heavy shower rain" -> Pair(R.drawable.rain, "Recommended to stay indoors")
        "Light snow" -> Pair(R.drawable.snow, "Wear warm clothes")
        "Snow" -> Pair(R.drawable.snow, "Wear warm clothes")
        "Heavy snow" -> Pair(R.drawable.snow, "Recommended to stay indoors")
        "Mix snow/rain" -> Pair(R.drawable.snow, "Recommended to stay indoors")
        "Sleet" -> Pair(R.drawable.snow, "Recommended to stay indoors")
        "Heavy sleet" -> Pair(R.drawable.snow, "Recommended to stay indoors")
        "Snow shower" -> Pair(R.drawable.snow, "Wear warm clothes")
        "Heavy snow shower" -> Pair(R.drawable.snow, "Recommended to stay indoors")
        "Flurries" -> Pair(R.drawable.snow, "Wear warm clothes!")
        "Mist" -> Pair(R.drawable.fog, "Recommended to stay indoors")
        "Smoke" -> Pair(R.drawable.fog, "Recommended to stay indoors")
        "Haze" -> Pair(R.drawable.fog, "Wear a mask if you plan to go out!")
        "Sand/dust" -> Pair(R.drawable.fog, "Stay indoors")
        "Fog" -> Pair(R.drawable.fog, "Recommended to stay indoors")
        "Freezing fog" -> Pair(R.drawable.fog, "Recommended to stay indoors")
        "Clear sky" -> Pair(R.drawable.clear_sky, "Go out for a jog and enjoy the sunshine!")
        "Few clouds" -> Pair(R.drawable.cloudy, "Fine for a short jog!")
        "Scattered clouds" -> Pair(R.drawable.cloudy, "Fine for a short jog!")
        "Broken clouds" -> Pair(R.drawable.cloudy, "Fine for a short jog!")
        "Overcast clouds" -> Pair(R.drawable.cloudy, "Fine for a short jog!")
        else -> Pair(R.drawable.error, "Error")        }
    }

    fun obtainLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        if (hasPermissions()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        weather_url = "https://api.weatherbit.io/v2.0/current?" + "lat=" +
                                location.latitude +"&lon="+ location.longitude + "&key="+ api_key

                        Log.d(ContentValues.TAG, "Got url: "+ weather_url)

                        getInfo()
                    } else {
                        // Location is null, do something
                        Log.d(ContentValues.TAG, "Got NULL: "+ weather_url)
                        country.text = "ERROR"
                        desc.text = "ERROR"
                        temp.text = "ERROR"
                        weatherimg.setImageResource(R.drawable.error)
                    }
                }
        }
    }


    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.BODY_SENSORS
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.INTERNET
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_NETWORK_STATE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }




}