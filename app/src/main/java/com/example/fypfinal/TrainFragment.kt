package com.example.fypfinal

import SliderAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TrainFragment : Fragment() {

    lateinit var tipView: TextView
    lateinit var calorieView: TextView
    lateinit var yoga_button: Button
    lateinit var strength_button: Button
    lateinit var cardio_button: Button
    lateinit var start_button: Button
    lateinit var complete_button: Button


    /*Carousel variables    */

    lateinit var trainVideos: ArrayList<Int>
    lateinit var sliderView: SliderView
    lateinit var sliderAdapter: SliderAdapter




    //QUOTE TEXT could range from both inspirational and notifications
    //Home carousel
        //Weather
        //A page like a game (eg. FGO) where it gives you a tip
        //Inspirational text with a fitness image


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_train, container, false)



        tipView = view.findViewById(R.id.tips_TY)
        calorieView = view.findViewById(R.id.calories_TV)
        yoga_button = view.findViewById(R.id.yoga_button)
        strength_button = view.findViewById(R.id.strength_button)
        cardio_button = view.findViewById(R.id.cardio_button)
        start_button = view.findViewById(R.id.startwko_button)
        complete_button = view.findViewById(R.id.complete_button)

        tipView.text = randomText()





        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = currentDate.format(formatter)
        val finalDateString = formattedDate.toString()



        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
        val dao_access = calendar_db.calendarDao()

        lifecycleScope.launch {
        var caloriesCount = dao_access.getCaloriesBurned(finalDateString)
        calorieView.text = "Calories Burned Today: (30 min estimation):\n" + caloriesCount
        }
        var yoga_counter = 0
        var strength_counter = 0
        var cardio_counter = 0


        sliderView = view.findViewById(R.id.video_carousel)

        trainVideos = ArrayList()
        trainVideos.add(R.raw.cardio_second)
        trainVideos.add(R.raw.strength_first)
        trainVideos.add(R.raw.yoga_dolphin)

        sliderAdapter = SliderAdapter( trainVideos)
        sliderView.setSliderAdapter(sliderAdapter)


        start_button.setOnClickListener {

            start_button.isEnabled = false
            yoga_button.isEnabled = true
            strength_button.isEnabled = true
            cardio_button.isEnabled = true

            if(yoga_counter > 0 || strength_counter > 0 || cardio_counter > 0){
                yoga_button.setBackgroundColor(Color.MAGENTA)


            }
            startAlert()
        }



        yoga_button.setOnClickListener{

            trainVideos.clear()

            strength_button.isEnabled = false
            cardio_button.isEnabled = false
            start_button.isEnabled = false

            if (yoga_counter > 0){

                /* Just shows the AlertDialog */
                yogaAlert()

            }else{

                yogaAlert()
                yoga_button.setBackgroundColor(Color.parseColor("#FFD700"))
                trainVideos.add(R.raw.yoga_dolphin)
                trainVideos.add(R.raw.yoga_lizard)
                trainVideos.add(R.raw.yoga_mountain)

                sliderAdapter = SliderAdapter( trainVideos)
                sliderView.setSliderAdapter(sliderAdapter)

                sliderView.scrollTimeInSec = 3
                sliderView.isAutoCycle = true


                yoga_counter += 1

                complete_button.isEnabled = true
            }
        }

        strength_button.setOnClickListener{

            trainVideos.clear()

            cardio_button.isEnabled = false
            yoga_button.isEnabled = false
            start_button.isEnabled = false

            if(strength_counter > 0){
                strength_button.setBackgroundColor(Color.parseColor("#FFD700"))
                strengthAlert()
            }else{

                strengthAlert()
                trainVideos.add(R.raw.strength_first)
                trainVideos.add(R.raw.strength_second)
                trainVideos.add(R.raw.strength_third)

                sliderAdapter = SliderAdapter( trainVideos)
                sliderView.setSliderAdapter(sliderAdapter)

                sliderView.scrollTimeInSec = 3
                sliderView.isAutoCycle = true

                strength_counter += 1
            }


            complete_button.isEnabled = true

        }

        cardio_button.setOnClickListener{

            trainVideos.clear()

            strength_button.isEnabled = false
            yoga_button.isEnabled = false
            start_button.isEnabled = false

            if(cardio_counter > 0){

                cardio_button.setBackgroundColor(Color.parseColor("#FFD700"))
                cardioAlert()

            }else{

                cardioAlert()

                trainVideos.add(R.raw.cardio_first)
                trainVideos.add(R.raw.cardio_second)
                trainVideos.add(R.raw.cardio_third)

                sliderAdapter = SliderAdapter( trainVideos)
                sliderView.setSliderAdapter(sliderAdapter)

                sliderView.scrollTimeInSec = 3
                sliderView.isAutoCycle = true

                cardio_counter += 1
            }



            complete_button.isEnabled = true
        }

        complete_button.setOnClickListener {


            strength_button.isEnabled = false
            complete_button.isEnabled = false
            yoga_button.setBackgroundColor(Color.parseColor("#696969"))
            strength_button.setBackgroundColor(Color.parseColor("#696969"))
            cardio_button.setBackgroundColor(Color.parseColor("#696969"))
            cardio_button.isEnabled = false
            yoga_button.isEnabled = false
            start_button.isEnabled = true

            val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()
            val dao_access = calendar_db.calendarDao()

                val result = checkWorkouts(strength_counter, yoga_counter, cardio_counter)

            lifecycleScope.launch {


                val strength_calories = 120
                val cardio_calories = 180
                val yoga_calories = 160



                when (result) {
                    "S" -> {
                        dao_access.incrementStrengthCount(finalDateString)
                        dao_access.addCaloriesBurned(finalDateString,strength_calories)
                    }
                    "C" -> {
                        dao_access.incrementCardioCount(finalDateString)
                        dao_access.addCaloriesBurned(finalDateString,cardio_calories)
                    }
                    "Y" -> {
                        dao_access.incrementYogaCount(finalDateString)
                        dao_access.addCaloriesBurned(finalDateString,yoga_calories)
                    }
                }
            }

            yoga_counter = 0
            cardio_counter = 0
            strength_counter = 0

            val text = "Database updated"
            val duration: Int = Toast.LENGTH_SHORT
            val toast: Toast = Toast.makeText(context, text, duration)
            toast.show()
        }


    }

    fun checkWorkouts(str: Int, yga: Int, crd: Int ): String {

        return if(str == 1){
            "S"
        }else if(yga == 1){
            "Y"
        }else if(crd == 1){
            "C"
        }else{
            "null"
        }


    }


    fun yogaAlert(){

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.change_details, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        builder.setTitle("Yoga Guide")
        val messageText = "You have selected Yoga training. Here are some yoga related workouts:\n" +
                "\n" +
                "Dolphin Pose (Ardha Pincha Mayurasana):\n" +
                "Start on your hands and knees, then lower down onto your forearms.\n" +
                "Walk your feet back and lift your hips up to come into a modified Downward-Facing Dog pose.\n" +
                "Keep your head and neck relaxed, and press your forearms into the mat to lengthen through your spine.\n" +
                "Hold for a few breaths, then release.\n" +
                "\n" +
                "Lizard Pose (Utthan Pristhasana):\n" +
                "From Downward-Facing Dog, step your right foot forward between your hands.\n" +
                "Lower your left knee down to the mat and walk your right foot out to the edge of the mat.\n" +
                "Bring your hands to the inside of your right foot and lower your elbows down to the mat.\n" +
                "Keep your hips low and breathe deeply into your hips and groin.\n" +
                "Repeat on the other side.\n" +
                "\n" +
                "Mountain Pose (Tadasana):\n" +
                "Stand with your feet together or hip-width apart.\n" +
                "Ground down through your feet and lift up through the crown of your head.\n" +
                "Roll your shoulders back and down.\n" +
                "Relax your arms by your sides."


        val messageScrollView = ScrollView(requireContext())
        val messageTextView = TextView(requireContext())
        messageTextView.text = messageText
        messageTextView.setPadding(32, 32, 32, 32)
        messageScrollView.addView(messageTextView)

        builder.setView(messageScrollView)

        builder.setPositiveButton("OK", null)

        val dialog = builder.create()
        dialog.show()
    }

    fun strengthAlert(){

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Strength Guide")
        val messageText = "You have selected Strength training. Here are some strength related " +
                "workouts:\n\n" + "- Pushups:\n" + "Get on the floor on all fours, positioning your" +
                " hands slightly wider than your shoulders. Keep your elbows bent. Extend your legs " +
                "back so you are balanced on your hands and toes, your feet hip-width apart. " +
                "After, inhale as you lower yourself by bending your elbows.\n\n" + "- Pull ups:\n" +
                "Beginning by hanging from the bar, the body is pulled up vertically. From the" +
                " top position, the participant lowers their body until the arms and shoulders " +
                "are fully extended. The end range of motion at the top end may be chin over bar" +
                " or higher, such as chest to bar.\n\n" + "- Dumbbell exercises:\n" + "- " +
                "Dumbbell bicep curls: Stand with feet hip-width apart and hold dumbbells at arm's" +
                " length by your sides, palms facing forward. Curl the dumbbells up to your " +
                "shoulders by bending your elbows, then lower them back down.\n" +
                "- Dumbbell bench press: Lie on your back on a bench with your feet flat on " +
                "the floor. Hold dumbbells at chest level, with palms facing forward. Push the " +
                "dumbbells up and together until your arms are fully extended, then lower them back down.\n" +
                "- Dumbbell shoulder press: Stand with feet shoulder-width apart and hold " +
                "dumbbells at shoulder level, palms facing forward. Press the dumbbells up overhead" +
                " until your arms are fully extended, then lower them back down.\n"


        val messageScroll = ScrollView(requireContext())
        val messageTextView = TextView(requireContext())
        messageTextView.text = messageText
        messageTextView.setPadding(32, 32, 32, 32)
        messageScroll.addView(messageTextView)


        builder.setView(messageScroll)

        builder.setPositiveButton("OK", null)

        val dialog = builder.create()
        dialog.show()

    }

    fun cardioAlert(){


        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Cardiovascular Guide")
       val messageText =  "You have selected Cardiovascular training. Here are some cardio related" +
               " workouts:\n\n" +
                "Cycling:\n\n" +
                "Cycling is a great way to improve cardiovascular fitness. You can either cycle " +
               "outdoors or indoors using a stationary bike. " +
                "Aim for at least 30 minutes of continuous cycling at a moderate to high " +
               "intensity.\n\n" +
                "Jump Rope:\n\n" +
                "Jumping rope is a fun and effective way to improve cardiovascular fitness. " +
               "Start with short intervals, such as 30 seconds, " +
                "and gradually increase the duration and intensity of your workouts. Aim for " +
               "at least 10 minutes of continuous jumping.\n\n" +
                "Running:\n\n" +
                "Running is a classic cardio exercise that can be done anywhere, anytime. Start " +
               "with a short warm-up, then run at a moderate " +
                "to high intensity for at least 20 minutes. Gradually increase the duration and " +
               "intensity of your runs as you build endurance."


        val messageScroll = ScrollView(requireContext())
        val messageTextView = TextView(requireContext())
        messageTextView.text = messageText
        messageTextView.setPadding(32, 32, 32, 32)
        messageScroll.addView(messageTextView)


        builder.setView(messageScroll)

        builder.setPositiveButton("OK", null)

        val dialog = builder.create()
        dialog.show()


    }

    fun startAlert(){

        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Cardiovascular Guide")

        builder.setMessage("You have started the workout!\n\n" +"Pick a workout:\n" +
                "(Once you choose a workout, you cannot change it.)")

        builder.setPositiveButton("I acknowledge", null)

        val dialog = builder.create()
        dialog.show()

    }

    fun randomText(): String {
        // Create a list of sentences to choose from
        val tips = arrayListOf<String>(
                    "Start small and build up gradually.",
                    "Stay hydrated throughout the day.",
                    "Mix up your workouts for better results.",
                    "Prioritize rest and recovery to prevent injury.",
                    "Eat a balanced diet with plenty of protein.",
                    "Set achievable goals to stay motivated.",
                    "Make fitness a habit by scheduling it into your routine."
        )
        val r = kotlin.random.Random.nextInt(tips.size)
        val randomString = tips[r]

        return randomString
    }



}