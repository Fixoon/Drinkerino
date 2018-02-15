package com.example.oskar.drinkerino.objects

import com.example.oskar.drinkerino.enums.DrinkGlass
import com.example.oskar.drinkerino.enums.LikeState

data class SimpleDrink constructor(var name: String, var properties: String, var drinkID:Int,
                                   var likeState: LikeState, var drinkGlass: DrinkGlass)