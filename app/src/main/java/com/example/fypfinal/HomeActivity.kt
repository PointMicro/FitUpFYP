package com.example.fypfinal


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fypfinal.databinding.ActivityHomeBinding



//To do
    //Add return codes that append the last hr recording to the home page
    //Fix the back button to the HR Text
    //Fix the Test HR button on the homepage
    //Fix the ppg functions
    //Fix the permission check


class HomeActivity : AppCompatActivity() {


    private lateinit var binding : ActivityHomeBinding

   /*Variables for the heart rate counter */
    //Here
    //here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())



            binding.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.plan -> replaceFragment(PlanFragment())
                    R.id.settings -> replaceFragment(SettingsFragment())
                    R.id.train -> replaceFragment(TrainFragment())
                    R.id.stats -> replaceFragment(StatsFragment())

                    else ->{

                    }
                }
                    true

            }



        }

        private fun replaceFragment(fragment: Fragment){
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_layout, fragment)
            fragmentTransaction.commit()
    }


















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


//  Calendar might mimicks an array or database


//current tasks
    //Permissions
        // Step Counter
        //HR Monitor
        //GPS
        //Weather and Prediction based on the temperature
    //App Requirements Listed (2GB Ram etc)
    //Terms and Conditions
    //Survey Page
        //Database recordings


    //
    //Implement Dark Mode

//References:
//https://developer.android.com/guide/topics/sensors/sensors_overview
//https://developer.android.com/guide/topics/sensors
//https://developer.android.com/reference/android/hardware/SensorManager
