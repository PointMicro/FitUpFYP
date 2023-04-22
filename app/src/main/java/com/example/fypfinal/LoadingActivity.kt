package com.example.fypfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity: AppCompatActivity() {

    lateinit var inspir_text: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startsplash)

        inspir_text = findViewById<TextView>(R.id.quoteText)
        inspir_text.text = ""
        inspir_text.text = randomText()


        Handler().postDelayed({
            val intent = Intent(this@LoadingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000L)
       /* GlobalScope.launch(Dispatchers.Main) {
            delay(4000L)
            val intent = Intent(this@LoadingActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        */

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
}