package com.task.solutiondeveloper.main.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.task.solutiondeveloper.R
import com.task.solutiondeveloper.auth.model.User
import com.task.solutiondeveloper.databinding.FragmentHomeBinding
import com.task.solutiondeveloper.utils.Constants
import com.task.solutiondeveloper.utils.LoadingDialog

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var list: MutableList<User>
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        binding = FragmentHomeBinding.bind(view)


        loadingDialog = LoadingDialog(requireActivity())
        binding.mainLayout.visibility = View.GONE

        list = mutableListOf()


        auth = Firebase.auth

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null) {
            loadingDialog.startLoading()
            updateUI(auth.currentUser?.email!!)
        }



    }

    private fun updateUI(email: String) {

        database = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_USER_REF)


        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                binding.mainLayout.visibility = View.VISIBLE
                loadingDialog.stopLoading()
                for(i in snapshot.children) {

                    if(Constants.removeSpecialCharacters(email) != i.key!!) list.add(i.getValue(User::class.java)!!)  //To not show the logged in user again in this list.

                    if(Constants.removeSpecialCharacters(email) == i.key!!) {
                        val user = i.getValue(User::class.java)
                        binding.loggedInUser.apply {
                            if (user != null) {
                                userAge.text = "Age: ${user.age} Years"
                                userName.text = user.name
                                userBirthday.text = "Birthday: ${i.child("birthday").value}"
                            }
                        }
                    }


                }

                val adapter = UserAdapter(list)
                binding.recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.toString())
            }

        })


    }
}