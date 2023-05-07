package com.example.fypfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch

class ChangeDetails  : Fragment(){

    lateinit var name_Settings: TextView
    lateinit var age_Settings: TextView
    lateinit var weight_Settings: TextView
    lateinit var height_Settings: TextView
    lateinit var genderSpinner: Spinner
    lateinit var injurySpinner: Spinner
    lateinit var change_details_button: Button
     var height_boolean: Boolean = false
     var weight_boolean:Boolean = false
     var age_boolean:Boolean = false
     var gender_boolean:Boolean = false
     var name_boolean:Boolean = false
     lateinit var submit_button:Button
     var counter = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.fypfinal.R.layout.change_details, container, false)




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genderSpinner = view.findViewById(R.id.genderSpinner)
        name_Settings= view.findViewById(R.id.editName)
        age_Settings= view.findViewById(R.id.editAge)
        height_Settings= view.findViewById(R.id.editHeight)
        weight_Settings= view.findViewById(R.id.editWeight)
        submit_button = view.findViewById(R.id.button2)


        val genderSettingsList = listOf("Unchanged", "Male", "Female", "Other")

        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genderSettingsList)

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        genderSpinner.adapter = genderAdapter


        submit_button.setOnClickListener{
            val calendar_db = Room.databaseBuilder(requireContext(),
                CalendarDatabase::class.java, "calendar_db").build()
            val dao_access_calendar = calendar_db.calendarDao()


            val user_db = Room.databaseBuilder(requireContext(),
                UserDatabase::class.java, "userDB").build()
            val dao_access_user = user_db.UserDao()

            lifecycleScope.launch {
                val old_name = dao_access_user.getName()
                val old_age = dao_access_user.getAge()
                val old_height = dao_access_user.getHeight()
                val old_weight = dao_access_user.getWeight()
                val old_gender = dao_access_user.getGender()



                genderSpinner.selectedItem.toString()


                val changeBuilder = StringBuilder()
                val errorBuilder = StringBuilder()


                val name = name_Settings.text.toString()
                if(name.isNotBlank()) {
                    changeBuilder.append(old_name + "Will be updated to:  " + name)
                    name_boolean = true
                    counter += 1
                }
                val age = age_Settings.text.toString().toIntOrNull()
                if(age != null && age > 15){
                    changeBuilder.append(old_age.toString() + "Will be updated to:" + age)
                    age_boolean = true
                    counter += 1

                }else if(age != null && age < 16){
                    errorBuilder.append("This application is 16+")
                }
                val height = height_Settings.text.toString().toIntOrNull()
                if (height != null) {
                    if(height != null && height > 240 || height < 90){
                        errorBuilder.append("Enter a valid height")
                    }else if(height.toString() != ""){
                        changeBuilder.append(old_height.toString() + "cm will be updated to: "
                                + height)
                         height_boolean = true
                        counter += 1

                    }
                }
                val weight = weight_Settings.text.toString().toIntOrNull()
                if (weight != null) {
                    if (weight != null && weight > 500 || weight < 23){
                        errorBuilder.append("Enter a valid weight")
                    }else if (weight != null){
                        changeBuilder.append(old_weight.toString() + "kg will be updated to: "
                                + weight)
                        weight_boolean = true
                        counter += 1

                    }
                }


                val gender = genderSpinner.selectedItem.toString()
                if (gender != "Unchanged"){
                    changeBuilder.append(old_gender.toString() + "gender will updated to: "
                            + gender)
                    gender_boolean = true
                    counter += 1

                }


                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Strength Guide")
                builder.setMessage(changeBuilder)


                builder.setPositiveButton("Confirm") { dialogInterface, i ->


                    lifecycleScope.launch {


                    if(height_boolean){
                        //Update height
                        dao_access_user.insertnewHeight(height)
                    }
                    else if(weight_boolean){
                        //Update Weight
                        dao_access_user.insertnewWeight(weight)
                    }
                    else if(name_boolean){
                    dao_access_user.insertnewName(name)
                    }
                    else if(age_boolean){
                        dao_access_user.insertNewAge(age)

                    }
                    else if(gender_boolean){
                        dao_access_user.insertNewGender(gender)

                    }


                    }


                }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                    // handle cancel button click
                }

                    val messageScroll = ScrollView(requireContext())
                    val messageTextView = TextView(requireContext())
                    messageTextView.setPadding(32, 32, 32, 32)
                    messageScroll.addView(messageTextView)


                if (counter > 0) {
                    val alertDialog = builder.create()
                    alertDialog.show()

                }else{
                    Toast.makeText(requireContext(), "Nothing to update", Toast.LENGTH_SHORT).show()

                }


            }


        }




    }










}