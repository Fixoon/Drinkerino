package com.example.oskar.drinkerino.interfaces

interface FilterDialogContract {
    interface View{
        fun addCheckboxes(topCheckBoxNames: Array<String>, bottomCheckBoxNames: Array<String>, topCheckedBoxes: BooleanArray, bottomCheckedBoxes: BooleanArray)
    }
    interface Presenter
}