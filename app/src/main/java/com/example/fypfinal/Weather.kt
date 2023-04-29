package com.example.fypfinal

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.location.LocationServices
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import org.json.JSONObject
import com.android.volley.Response
import com.google.android.gms.location.FusedLocationProviderClient
import org.w3c.dom.Text
import java.util.*

class Weather: AppCompatActivity() {


    //Either put this code into Home Fragment
        //or make this run in the background and
    //or can call it in homefragment or loading screen and convert it before it reaches homepage

    //Weatherbit API
    var api_key = "e1c4fb38c9cf4140b8c40e053e6de991"
    //URL for JSON
    var weather_url = ""
    private lateinit var city: TextView
    private lateinit var temp: TextView
    private lateinit var desc: TextView
    private lateinit var country: TextView
    private lateinit var weatherimg: ImageView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_tab)
        //link the textView in which the temperature and city
        city = findViewById(R.id.city_name_textView)
        temp = findViewById(R.id.temperature_textView)
        desc = findViewById(R.id.description_textView)
        country = findViewById(R.id.country_name_textView)
        weatherimg = findViewById(R.id.weather_img)

        //Instance of the Fused Location Provider Client is created
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url)
        //on clicking this button function to get the coordinates will be called
        obtainLocation()

    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        Log.e("lat", "function")
        //get the last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                weather_url = "https://api.weatherbit.io/v2.0/current?" + "lat=" +
                        location?.latitude +"&lon="+ location?.longitude + "&key="+ api_key
                getInfo()
            }
    }




    // This function retrieves the temperature from a weather API and displays it on a TextView.
    fun getInfo() {
        // Instantiate the RequestQueue using the Volley library.
        val requestQueue = Volley.newRequestQueue(this)
        // Define the URL of the weather API.
        val url: String = weather_url
        // Create a request for a string response from the API.
        val stringRequest = StringRequest(Request.Method.GET, url,
            // If the request succeeds, this function is called with the response string.
            { response ->
                // Parse the response string as a JSON object.
                val weatherObj = JSONObject(response)
                // Extract the "data" array from the JSON object.
                val weatherArr = weatherObj.getJSONArray("data")
                // Extract the first object from the "data" array.
                val weatherObj2 = weatherArr.getJSONObject(0)
                // Set the city name on the TextView
                var countrycode = "${weatherObj2.getString("country_code")}"
                city.text = "${weatherObj2.getString("city_name")}"
                country.text = getCountryName(countrycode)
                desc.text = "${weatherObj2.getString("description")}"
                temp.text = "${weatherObj2.getString("temp")}" + "C"
            },
            // If the request fails, this function is called with an error message.
            { error ->
                // Set error messages for all the relevant TextViews
                country.text = "ERROR"
                desc.text = "ERROR"
                temp.text = "ERROR"
                weatherimg.setImageResource(R.drawable.green_icon) //change green icon to error img
            })
        // Add the request to the RequestQueue to execute it.
        requestQueue.add(stringRequest)
    }

    fun getCountryName(countryCode: String): String? {
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }

    fun weatherImage(description: String): Int {
        return when (description) {
            "Thunderstorm with light rain" -> R.drawable.thunder
            "Thunderstorm with rain" -> R.drawable.thunder
            "Thunderstorm with heavy rain" -> R.drawable.thunder
            "Thunderstorm with light drizzle" -> R.drawable.thunder
            "Thunderstorm with drizzle" -> R.drawable.thunder
            "Thunderstorm with heavy drizzle" -> R.drawable.thunder
            "Thunderstorm with hail" -> R.drawable.thunder
            "Light drizzle" -> R.drawable.rain
            "Drizzle" -> R.drawable.rain
            "Heavy drizzle" -> R.drawable.rain
            "Light rain" -> R.drawable.rain
            "Moderate rain" -> R.drawable.rain
            "Heavy rain" -> R.drawable.rain
            "Freezing rain" -> R.drawable.rain
            "Light shower rain" ->R.drawable.rain
            "Shower rain" -> R.drawable.rain
            "Heavy shower rain" -> R.drawable.rain
            "Light snow" -> R.drawable.snow
            "Snow" -> R.drawable.snow
            "Heavy snow" -> R.drawable.snow
            "Mix snow/rain" -> R.drawable.snow
            "Sleet" -> R.drawable.snow
            "Heavy sleet" -> R.drawable.snow
            "Snow shower" -> R.drawable.snow
            "Heavy snow shower" -> R.drawable.snow
            "Flurries" -> R.drawable.snow
            "Mist" -> R.drawable.fog
            "Smoke" -> R.drawable.fog
            "Haze" -> R.drawable.fog
            "Sand/dust" -> R.drawable.fog
            "Fog" -> R.drawable.fog
            "Freezing fog" -> R.drawable.fog
            "Clear sky" -> R.drawable.clear_sky
            "Few clouds" -> R.drawable.cloudy
            "Scattered clouds" -> R.drawable.cloudy
            "Broken clouds" -> R.drawable.cloudy
            "Overcast clouds" -> R.drawable.cloudy
            else -> R.drawable.error
        }
    }





}