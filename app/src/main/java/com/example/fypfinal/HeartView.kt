package com.example.fypfinal

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
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HeartView : Fragment(), SensorEventListener {

    val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }


    //Creating the variables for the PPG Heart Rate Monitor
    private  var ppgSensor: Sensor? = null
    private lateinit var sensorManager: SensorManager
    private lateinit var hr_text: TextView
    private lateinit var startButton: Button
    private lateinit var backButton: Button

    //Permission
    private val REQUEST_SENSORS = 1

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // Permission granted
            start()
        } else {
            // Permission denied
        }
    }

    //Timer, Measure Boolean, Permissions
    private var timer_start: Long = 0
    private var timer_end: Long = 0
    private var measuring = false

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_heartrate, container, false)

        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        ppgSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)








        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_heartrate, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

 //        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        ppgSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        hr_text = view.findViewById(R.id.textViewHeartRate)



        /*  Starts the Heart Rate monitor */
        startButton = view.findViewById(R.id.start_button)
        startButton.setOnClickListener {
            if(checkPermission()){
                start()
            } else {
                requestPermission()
            }
        }


        /* Goes back to the HomeFragment */
        backButton = view.findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            sharedViewModel.updateHrButtonVisibility(true)
            requireFragmentManager().popBackStack()
        }



    }



    override fun onSensorChanged(event: SensorEvent?) {
        val sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as SensorManager
        ppgSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)


        if (measuring) {
            val hr_value = event?.values?.get(0)?.toInt() ?: -1
            if (hr_value != -1) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - timer_start >= 10000) {
                    measuring = false
                    timer_end = System.currentTimeMillis()
                    sensorManager.unregisterListener(this)
                    hr_text.text = "Heart rate: $hr_value BPM"
                    startButton.isEnabled = true
                }
            }

        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Does nothing
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Update the value of isHrButtonVisible to true when the HeartView fragment is destroyed
        sharedViewModel.updateHrButtonVisibility(true)
    }


    /*This function starts allows the heart rate monitor to start
    * It allows the measurement of the HR and sets the timer to the current time
    * to allow for the timer function to work*/

    private fun start() {

        //Sets the flag for measure to true
        measuring = true

        //Starts the timer by setting it to the current time.
        timer_start = System.currentTimeMillis()

        //Registers the listener for the PPG sensor, while delivering the data as quickly as it can
        sensorManager.registerListener(this, ppgSensor, SensorManager.SENSOR_DELAY_FASTEST)
        startButton.isEnabled = false
    }


    /*
    Detects whether the user has the Photoplethysmography Sensor and disables if they don't
     */
    private fun noPPG(){
        //If the user doesnt have the PPG then grey out the buttons.
            //HomeFragment should probably call this so it can grey out HR button
    }



    /*
    Permission Checker - Enables the permission check that asks the user to only use this time
     * or for them to allow all of the time
     */
    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.BODY_SENSORS
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_SENSORS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start()
            }
        }
    }






}


