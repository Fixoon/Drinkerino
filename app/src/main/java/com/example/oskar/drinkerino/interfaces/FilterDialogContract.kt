package com.example.oskar.drinkerino.interfaces

interface FilterDialogContract {
    interface View{
        fun addCheckboxes(propertiesList: Array<String>, ingredientsList: Array<String>, mCheckedProperties: BooleanArray, mCheckedBaseSpirits: BooleanArray)
    }
    interface Presenter
}