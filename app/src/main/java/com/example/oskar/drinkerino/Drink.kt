package com.example.oskar.drinkerino

data class Drink(var name: String, var baseSpirit: String, var ingredients: Array<String>,
                 var measurements: Array<String>, var properties: Array<String>, var tools: Array<String>,
                 var instructions: String, var drinkGlass: DrinkGlass, var likeState: LikeState = LikeState.NOT_LIKED)
