package com.task.solutiondeveloper.utils

object Constants {
    const val DATABASE_USER_REF = "User"
    const val INTENT_USER_NAME = "user_name"
    const val DATABASE_TODO_REF = "Todo"

    fun removeSpecialCharacters(string: String): String {
        return string.replace(Regex("[^A-Za-z0-9 ]"),"")
    }
}