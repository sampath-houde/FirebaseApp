package com.task.solutiondeveloper.main.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.solutiondeveloper.R
import com.task.solutiondeveloper.databinding.ActivityMainBinding
import com.task.solutiondeveloper.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.statusBarColor = ContextCompat.getColor(applicationContext, R.color.colorPrimaryVariant)

        auth = Firebase.auth
        binding.bottomNavigationView.background = null

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.)
        window?.statusBarColor = ContextCompat.getColor(applicationContext, R.color.black)



        val bottomNav = binding.bottomNavigationView
        val navController: NavController = Navigation.findNavController(this, R.id.fragment)

        NavigationUI.setupWithNavController(bottomNav, navController)

        binding.addBtn.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
    }
}