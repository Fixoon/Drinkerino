package com.example.oskar.drinkerino.data

import com.example.oskar.drinkerino.MainApplication
import com.example.oskar.drinkerino.R

class StringProvider {
    private val globalContext = MainApplication.getContext()

    fun getPropertiesStrings() : Array<String>{
        return arrayOf(globalContext.getString(R.string.property_sweet),
                globalContext.getString(R.string.property_sour),
                globalContext.getString(R.string.property_bitter),
                globalContext.getString(R.string.property_acidic),
                globalContext.getString(R.string.property_salt))
    }

    fun getSpiritStrings() : Array<String>{
        return arrayOf(globalContext.getString(R.string.spirit_rum),
                globalContext.getString(R.string.spirit_vodka),
                globalContext.getString(R.string.spirit_tequila),
                globalContext.getString(R.string.spirit_gin),
                globalContext.getString(R.string.spirit_whiskey),
                globalContext.getString(R.string.spirit_others))
    }
}