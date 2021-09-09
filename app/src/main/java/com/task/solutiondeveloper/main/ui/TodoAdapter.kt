package com.task.solutiondeveloper.main.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.task.solutiondeveloper.R
import com.task.solutiondeveloper.databinding.SingleTodoViewBinding
import com.task.solutiondeveloper.main.model.Todo

class TodoAdapter(
    val list: List<String>,
    val context: Context,
    val todoSnapshot: DataSnapshot,
): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder (val binding: SingleTodoViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = SingleTodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {

                binding.titleTodo.text = this

                binding.deleteButton.setOnClickListener {
                    var i = 0
                    for(snap in todoSnapshot.children) {
                        if(i == position) {
                            val ref = snap.ref
                            ref.removeValue()
                                .addOnCompleteListener {
                                    Toast.makeText(context, "Task Deleted", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error, try again", Toast.LENGTH_SHORT).show()
                                }
                        }
                        i++
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}