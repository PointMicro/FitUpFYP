package com.example.fypfinal


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fypfinal.databinding.ActivityHomeBinding



class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

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




//References:
//https://developer.android.com/guide/topics/sensors/sensors_overview
//https://developer.android.com/guide/topics/sensors
//https://developer.android.com/reference/android/hardware/SensorManager
