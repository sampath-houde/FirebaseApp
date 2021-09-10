package com.task.solutiondeveloper.utils

object Constants {
    const val DATABASE_USER_REF = "User"
    const val INTENT_USER_NAME = "user_name"
    const val DATABASE_TODO_REF = "Todo"
    const val shared_pref = "com.lifeSkills.key2IQ"

    fun removeSpecialCharacters(string: String): String {
        return string.replace(Regex("[^A-Za-z0-9 ]"),"")
    }

    fun getYear(string: String) : Int {

        return if(string.length == 10) {
            string.substring(6,10).toInt()
        } else {
            string.substring(7,11).toInt()
        }

    }
}