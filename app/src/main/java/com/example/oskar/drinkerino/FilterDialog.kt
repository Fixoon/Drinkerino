package com.example.oskar.drinkerino

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import kotlinx.android.synthetic.main.custom_dialog.*

class FilterDialog(private val propertiesList:Array<String>, private val ingredientsList:Array<String>, private val activity:Activity) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        addCheckboxes()
    }

    private fun addCheckboxes(){
        propertiesList.forEachIndexed{ index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.text = s
            if(index % 2 == 1){
                propertyLayoutRight.addView(newCheckBox)
            }else{
                propertyLayoutLeft.addView(newCheckBox)
            }
        }
        ingredientsList.forEachIndexed{ index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.text = s
            if(index % 2 == 1){
                drinkBaseLayoutRight.addView(newCheckBox)
            }else{
                drinkBaseLayoutLeft.addView(newCheckBox)
            }
        }
    }
}