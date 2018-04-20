package com.example.oskar.drinkerino.interfaces

import com.example.oskar.drinkerino.objects.SimpleDrink

interface MainContract {
    interface View{
        fun setDrinkList(drinkList: ArrayList<SimpleDrink>)
        fun navigateToRecipe(drinkID: Int)
        fun showNoResultText()
        fun hideNoResultText()
    }

    interface Presenter{
        fun onItemClicked(drink: SimpleDrink)
        fun onFilterClicked(propertiesDialogCheckboxes: ArrayList<String>,
                            baseSpiritsDialogCheckboxes: ArrayList<String>,
                            checkOther: Boolean)
        fun likeToggle(drink: SimpleDrink)
        fun resetDrinkList()
    }
}