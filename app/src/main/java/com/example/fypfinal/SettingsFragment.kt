package com.example.fypfinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.concurrent.TimeoutException


class SettingsFragment : Fragment() {

    val sharedViewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    lateinit var details_Button: Button
    lateinit var delete_Button: Button
    lateinit var name_display_view: View
    lateinit var name_settings: TextView
    lateinit var privacy_button: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.fypfinal.R.layout.fragment_settings, container, false)

        delete_Button = view.findViewById(R.id.deleteButton)
        details_Button = view.findViewById(R.id.detailsButton)
        name_display_view = view.findViewById(R.id.name_display)
        name_settings = view.findViewById(R.id.settings_nameTV)
        privacy_button = view.findViewById(R.id.privacy_policy)


        val user_db = Room.databaseBuilder(requireContext(), UserDatabase::class.java, "userDB").build()
        val dao_access = user_db.UserDao()

        lifecycleScope.launch {
            name_settings.text = dao_access.getName()
        }





        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        privacy_button.setOnClickListener {
            val messageText = "At Fit-Up-Fit-Now, we take your privacy seriously. " +
                    "The data collected involves: location data for weather, biometrics for " +
                    "the heart rate monitor," + " and tracking data for the steps tracker. We also" +
                    " collect your name, age, " + "gender, height, and weight for the survey you had" +
                    " answered and stats for calculations." + " We do not share your data with " +
                    "third parties. All data is kept " + "confidential and is used only for " +
                    "improving our" + " services and providing " + "you with a better fitness " +
                    "experience. " + "By using Fit-Up-Fit-Now, " + "you agree to our privacy policy."
            val alertDialog = AlertDialog.Builder(requireContext())
                .setTitle("Privacy Policy")

                .setMessage(messageText)
                .setPositiveButton("OK") { _, _ ->
                    // Handle OK button click
                }
                .create()
            alertDialog.show()

        }

// Create instances of the user and calendar databases
        val user_db = Room.databaseBuilder(requireContext(), UserDatabase::class.java, "userDB").build()
        val dao_access = user_db.UserDao()
        val calendar_db = Room.databaseBuilder(requireContext(), CalendarDatabase::class.java, "calendar_db").build()

// Set an on-click listener for the delete button
        delete_Button.setOnClickListener {
            // Display a dialog to warn the user before deleting the database
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete User Data")
            builder.setMessage("Are you sure you want to delete data? This action cannot be undone.")
            builder.setPositiveButton("Delete") { _, _ ->
                // Get the user's name to confirm the deletion
                val input = EditText(requireContext())
                input.hint = "Type your name to confirm."
                val confirmBuilder = AlertDialog.Builder(requireContext())
                confirmBuilder.setView(input)
                confirmBuilder.setPositiveButton("Confirm") { _, _ ->

                    // Check if the user entered their name correctly
                    val name = input.text.toString().trim()
                    lifecycleScope.launch {
                    if (name.equals(dao_access.getName() , ignoreCase = true)) {
                        // If the name is correct, delete the database
                        context?.deleteDatabase("userDB")
                        context?.deleteDatabase("calendar_db")
                        Toast.makeText(requireContext(), "Data has been deleted", Toast.LENGTH_SHORT).show()
                        requireActivity().finishAffinity()
                        System.exit(0)
                    } else {
                        // If the name is incorrect, show an error message
                        Toast.makeText(requireContext(), "Name does not match. Deletion cancelled.", Toast.LENGTH_SHORT).show()
                    }
                    }
                }
                confirmBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
                confirmBuilder.show()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()


        }





        sharedViewModel.isCardViewVisible.observe(viewLifecycleOwner,{ isVisible ->
            details_Button.isVisible = isVisible
            delete_Button.isVisible = isVisible
            name_display_view.isVisible = isVisible
            name_settings.isVisible = isVisible
            privacy_button.isVisible = isVisible
        })

        details_Button.setOnClickListener{

            val fragment = ChangeDetails()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.settings_container, fragment)
            transaction.addToBackStack(null)
            sharedViewModel.updateCardViewVisibility(false)
            transaction.commit()

        }


    }

}




