package com.task.solutiondeveloper

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.task.solutiondeveloper.auth.model.User
import com.task.solutiondeveloper.databinding.FragmentRegisterBinding
import com.task.solutiondeveloper.utils.*

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val ERROR = "This field is required."
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.bind(view)

        loadingDialog = LoadingDialog(requireActivity())

        auth = Firebase.auth

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showUI()
    }

    private fun showUI() {

        binding.createAccountButton.setOnClickListener {

            val name = binding.firstNameEditText.text.toString().trim()
            val age = binding.ageEditText.text.toString().trim()
            val email = binding.emailEditText2.text.toString().trim()
            val password = binding.passwordEditText2.text.toString().trim()
            val birthday = binding.birthdayEditText.text.toString()

            binding.birthdayEditText.setOnClickListener {

                val builder = CalendarBuilder.getMaterialDateBuilder()
                val materialDatePicker = builder.build()

                materialDatePicker.addOnPositiveButtonClickListener {
                    binding.birthdayEditText.setText(materialDatePicker.headerText)
                    binding.birthdayInputLayout.error = null
                }


                materialDatePicker.show(requireFragmentManager(), "tag")
            }

            val bool = checkValidityOfRegisterFields(name, age, email, password, birthday)

            if (bool) {
                loadingDialog.startLoading()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        loadingDialog.stopLoading()
                        if(it.isSuccessful) {
                            generateUser(name, email, age, birthday)
                            val current = auth.currentUser!!
                            current.sendEmailVerification()
                            toastShort(requireContext(), "Verification email sent to $email" )
                        } else {
                            toastShort(requireContext(), "Error")
                        }
                    }
            }

        }
    }

    private fun generateUser(name: String, email: String, age: String, birthday: String) {
        val user = User(birthday, name, age, email)


        FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USER_REF)
            .child(Constants.removeSpecialCharacters(email))
            .setValue(user)
            .addOnCompleteListener {
                findNavController().navigateUp()
            }
            .addOnFailureListener {
                toastShort(requireContext(), "Error")
            }
    }

    fun checkValidityOfRegisterFields(
        name: String,
        age: String,
        email: String,
        password: String,
        birthday: String
    ): Boolean {

        var isAllFieldsValid = false

        if (email.isEmpty()) {
            showError(binding.emailInputLayout2, ERROR)
            isAllFieldsValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(binding.emailInputLayout2, "Email Invalid")
            isAllFieldsValid = false
        } else if (password.isEmpty()) {
            showError(binding.passwordInputLayout2, ERROR)
            isAllFieldsValid = false
        } else if (password.length < 5) {
            showError(binding.passwordInputLayout2, "Password length should be greater than 5")
            isAllFieldsValid = false
        } else if (name.isEmpty()) {
            showError(binding.firstNameInputLayout, ERROR)
            isAllFieldsValid = false
        } else if (age.isEmpty()) {
            showError(binding.ageInputLayout, ERROR)
            isAllFieldsValid = false
        } else if (birthday.isEmpty()) {
            showError(binding.birthdayInputLayout, ERROR)
            isAllFieldsValid = false
        }
        else isAllFieldsValid = true

        return isAllFieldsValid
    }


}