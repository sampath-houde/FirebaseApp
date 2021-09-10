package com.task.solutiondeveloper.main.ui

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.task.solutiondeveloper.databinding.ActivityTaskBinding
import com.task.solutiondeveloper.utils.Constants
import com.task.solutiondeveloper.utils.toastShort

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var todoDatabase: DatabaseReference
    private lateinit var list: MutableList<String>
    private lateinit var todoSnapshot: DataSnapshot
    private lateinit var auth: FirebaseAuth
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)

        val bottomSheet: RelativeLayout = binding.LayoutBottom
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        list = mutableListOf()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        id = Constants.removeSpecialCharacters(auth.currentUser?.email!!)
        todoDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_TODO_REF)

        binding.doneButton.setOnClickListener {
            val task = binding.todoTask.text.toString().trim()
            if(task.isNotBlank()) addTaskToDatabase(task)
            else toastShort(applicationContext, "Task cannot be empty")
        }

        todoDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoSnapshot = snapshot
                list.clear()
                for(snap in snapshot.children) {
                    if(snap.key == id) {
                        todoSnapshot = snap
                        for(todo in snap.children) {
                            val toDO = todo.getValue(String::class.java)
                            if(toDO!=null) {
                                list.add(toDO)
                            }

                        }
                    }
                }
                if(todoSnapshot!=null) {
                    val adapter = TodoAdapter(list, applicationContext, todoSnapshot)
                    binding.recyclerView.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.toString())
            }

        })

    }

    private fun addTaskToDatabase(task: String) {
        todoDatabase.child(id).push().setValue(task)
            .addOnCompleteListener {
                binding.todoTask.setText("")
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                toastShort(applicationContext, "Task added")
            }
            .addOnFailureListener {
                toastShort(applicationContext, "Error, try again")
            }


    }
}