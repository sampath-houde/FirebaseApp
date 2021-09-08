package com.task.solutiondeveloper.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.solutiondeveloper.auth.model.User
import com.task.solutiondeveloper.databinding.ViewUserBinding

class UserAdapter(val list: List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: ViewUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.userName.text = this.name
                binding.userAge.text = "Age: ${this.age} Years"
                binding.userBirthday.text = "Birthday: ${this.birthday}"
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}