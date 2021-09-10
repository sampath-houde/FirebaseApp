package com.task.solutiondeveloper.utils

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

object CalendarBuilder {
     fun getMaterialDateBuilder(): MaterialDatePicker.Builder<Long> {
        val materialDateBuilder = MaterialDatePicker.Builder.datePicker()

        materialDateBuilder.setTitleText("Select Birth Date")

        //Makes only dates from today backward selectable.
        val constraintsBuilder = CalendarConstraints.Builder().setValidator(
            DateValidatorPointBackward.now())

        materialDateBuilder.setCalendarConstraints(constraintsBuilder.build())

        return materialDateBuilder

    }
}