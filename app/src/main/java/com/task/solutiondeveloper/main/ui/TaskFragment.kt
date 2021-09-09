package com.task.solutiondeveloper.main.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.task.solutiondeveloper.R
import com.task.solutiondeveloper.databinding.FragmentTaskBinding
import com.task.solutiondeveloper.main.model.Todo
import com.task.solutiondeveloper.utils.Constants
import com.task.solutiondeveloper.utils.toastShort

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private val args by navArgs<TaskFragmentArgs>()
    private lateinit var todoDatabase: DatabaseReference
    private lateinit var list: MutableList<String>
    private lateinit var todoSnapshot: DataSnapshot


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        binding = FragmentTaskBinding.bind(view)
        val bottomSheet: RelativeLayout = binding.LayoutBottom
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        list = mutableListOf()



        return binding.root
    }

    override fun onStart() {
        super.onStart()
        todoDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_TODO_REF)

        binding.doneButton.setOnClickListener {
            val task = binding.todoTask.text.toString().trim()
            if(task.isNotBlank()) addTaskToDatabase(task)
            else toastShort(requireContext(), "Task cannot be empty")
        }

        todoDatabase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                todoSnapshot = snapshot
                list.clear()
                for(snap in snapshot.children) {
                    if(snap.key == args.id) {
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
                    val adapter = TodoAdapter(list, requireContext(), todoSnapshot)
                    binding.recyclerView.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.toString())
            }

        })

    }

    private fun addTaskToDatabase(task: String) {
        todoDatabase.child(args.id).push().setValue(task)
            .addOnCompleteListener {
                binding.todoTask.setText("")
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                toastShort(requireContext(), "Task added")
            }
            .addOnFailureListener {
                toastShort(requireContext(), "Error, try again")
            }


    }
}