package com.example.oskar.drinkerino

enum class DrinkGlass (val glassID:Int) {
    LOWBALL(0),
    HIGHBALL(1),
    FLUTE(3);

    companion object {
        private val map = DrinkGlass.values().associateBy(DrinkGlass::glassID);
        fun fromInt(type: Int) = map[type]
    }
}