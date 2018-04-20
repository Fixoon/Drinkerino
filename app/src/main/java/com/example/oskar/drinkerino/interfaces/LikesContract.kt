package com.example.oskar.drinkerino.interfaces

import com.example.oskar.drinkerino.objects.SimpleDrink

interface LikesContract {
    interface View{
        fun setDrinkList(drinkList: ArrayList<SimpleDrink>)
        fun showNoLikesText()
        fun hideNoLikesText()
        fun navigateToRecipe(drinkID: Int)
    }
    interface Presenter
}