package com.example.oskar.drinkerino.interfaces

import com.example.oskar.drinkerino.objects.FilterDialogCheckboxes

interface FilterDialogAction {
    fun filterClick(propertiesDialogCheckboxes: FilterDialogCheckboxes, drinkBaseDialogCheckboxes: FilterDialogCheckboxes, checkOther:Boolean){

    }
}