package com.example.oskar.drinkerino.objects

import com.example.oskar.drinkerino.enums.DrinkGlass
import com.example.oskar.drinkerino.enums.LikeState
import java.util.*

data class Drink(var name: String, var id: Int, var baseSpirit: String,
                 var ingredients: Array<String>, var measurements: Array<String>,
                 var properties: Array<String>, var tools: Array<String>, var instructions: String,
                 var drinkGlass: DrinkGlass, var likeState: LikeState) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Drink

        if (name != other.name) return false
        if (baseSpirit != other.baseSpirit) return false
        if (!Arrays.equals(ingredients, other.ingredients)) return false
        if (!Arrays.equals(measurements, other.measurements)) return false
        if (!Arrays.equals(properties, other.properties)) return false
        if (!Arrays.equals(tools, other.tools)) return false
        if (instructions != other.instructions) return false
        if (drinkGlass != other.drinkGlass) return false
        if (likeState != other.likeState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + baseSpirit.hashCode()
        result = 31 * result + Arrays.hashCode(ingredients)
        result = 31 * result + Arrays.hashCode(measurements)
        result = 31 * result + Arrays.hashCode(properties)
        result = 31 * result + Arrays.hashCode(tools)
        result = 31 * result + instructions.hashCode()
        result = 31 * result + drinkGlass.hashCode()
        result = 31 * result + likeState.hashCode()
        return result
    }
}