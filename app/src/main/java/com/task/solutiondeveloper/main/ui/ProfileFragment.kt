package com.task.solutiondeveloper.main.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.task.solutiondeveloper.AuthActivity
import com.task.solutiondeveloper.R
import com.task.solutiondeveloper.auth.model.User
import com.task.solutiondeveloper.databinding.FragmentProfileBinding
import com.task.solutiondeveloper.utils.CalendarBuilder
import com.task.solutiondeveloper.utils.Constants
import com.task.solutiondeveloper.utils.showError
import com.task.solutiondeveloper.utils.toastShort
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        binding = FragmentProfileBinding.bind(view)

        auth = Firebase.auth

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        userDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USER_REF)

        binding.birthdayEditText.setOnClickListener {

            val builder = CalendarBuilder.getMaterialDateBuilder()
            val materialDatePicker = builder.build()

            materialDatePicker.addOnPositiveButtonClickListener {

                binding.birthdayEditText.setText(materialDatePicker.headerText)
                binding.birthdayInputLayout.error = null
            }


            materialDatePicker.show(requireFragmentManager(), "tag")
        }
        showUI()

        binding.updateButton.setOnClickListener {
            val name = binding.nameText.text.toString().trim()
            val birthday = binding.birthdayEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()

            val bool = checkValidity(name, birthday, age)

            if(bool) {
                val user = User(birthday, name, age, auth.currentUser?.email!!)
                userDatabase.child(Constants.removeSpecialCharacters(auth.currentUser?.email!!))
                    .setValue(user)
                    .addOnCompleteListener {
                        toastShort(requireContext(), "Updated Successfully")
                    }
                    .addOnFailureListener {
                        toastShort(requireContext(), "Error, try again")
                    }
            }

        }

        binding.logout.setOnClickListener {
            auth.signOut()
            toastShort(requireContext(), "Logout successful")
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun showUI() {
        userDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    if(snap.key == Constants.removeSpecialCharacters(auth.currentUser?.email!!)) {
                        val user = snap.getValue(User::class.java)
                        binding.nameText.setText(user?.name)
                        binding.birthdayEditText.setText(user?.birthday)
                        binding.ageEditText.setText(user?.age)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.message)
            }

        })
    }

    private fun checkValidity(name: String, birthday: String, age: String): Boolean {

        var isAllFieldsValid = false

        if(age.isEmpty()) {
            binding.ageInputLayout.error = "Age should not be blank"
        } else if ((Calendar.getInstance().get(Calendar.YEAR) - Constants.getYear(birthday)) != age.toInt()) {
            showError(binding.birthdayInputLayout, "Birth year is wrong")
        } else if(name.isEmpty()) {
            binding.nameEditextLayout.error = "Nmae should not be empty"
        } else isAllFieldsValid = true

        return isAllFieldsValid
    }

}