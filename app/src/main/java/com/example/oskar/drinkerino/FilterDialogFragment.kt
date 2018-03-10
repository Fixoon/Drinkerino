package com.example.oskar.drinkerino

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import com.example.oskar.drinkerino.objects.FilterDialogCheckboxes
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_dialog.view.*


class FilterDialogFragment : DialogFragment(), FilterDialogAction {
    fun newInstance(propertiesList:Array<String>,
                    ingredientsList:Array<String>,
                    checkedProperties:ArrayList<Boolean>,
                    checkedIngredients:ArrayList<Boolean>): FilterDialogFragment {

        mPropertiesList = propertiesList
        mCheckedProperties = checkedProperties
        mIngredientsList = ingredientsList
        mCheckedIngredients = checkedIngredients
        return FilterDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mCallback = targetFragment as FilterDialogAction
        }catch (e:ClassCastException){

        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.custom_dialog, container)

        addCheckboxes(mPropertiesList, mIngredientsList, view)

        view.cancelButton.setOnClickListener{
            dismiss()
        }
        view.actionButton.setOnClickListener{
            mCallback.filterClick(getCheckedBoxes(arrayOf(propertyLayoutLeft, propertyLayoutRight)),
                                getCheckedBoxes(arrayOf(drinkBaseLayoutLeft, drinkBaseLayoutRight)),
                                (drinkBaseLayoutRight.getChildAt(2) as CheckBox).isChecked)
            dismiss()
        }

        return view
    }

    private fun addCheckboxes(propertiesList:Array<String>, ingredientsList:Array<String>, view:View){
        propertiesList.forEachIndexed{ index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedProperties[index]
            newCheckBox.text = s
            if(index % 2 == 1){
                view.propertyLayoutRight.addView(newCheckBox)
            }else{
                view.propertyLayoutLeft.addView(newCheckBox)
            }
        }
        ingredientsList.forEachIndexed{ index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedIngredients[index]
            newCheckBox.text = s
            if(index % 2 == 1){
                view.drinkBaseLayoutRight.addView(newCheckBox)
            }else{
                view.drinkBaseLayoutLeft.addView(newCheckBox)
            }
        }
    }

    private fun getCheckedBoxes(parentViews:Array<LinearLayout>) : FilterDialogCheckboxes {
        val checkedBoxesNames:ArrayList<String> = arrayListOf()
        val checkedBoxes:ArrayList<Boolean> = arrayListOf()

        var index = 0
        for(i in 0 until (parentViews[0].childCount + parentViews[1].childCount)){
            if(i % 2 == 0){
                checkedBoxes.add((parentViews[0].getChildAt(index) as CheckBox).isChecked)
            }else{
                checkedBoxes.add((parentViews[1].getChildAt(index) as CheckBox).isChecked)
                index++
            }
        }

        parentViews.forEachIndexed{ _, l ->
            (0 until l.childCount)
                    .map { l.getChildAt(it) as CheckBox }
                    .filter { it.isChecked }
                    .mapTo(checkedBoxesNames) { it.text.toString() }
        }

        return FilterDialogCheckboxes(checkedBoxesNames, checkedBoxes)
    }

    companion object {
        private lateinit var mPropertiesList: Array<String>
        private lateinit var mCheckedProperties: ArrayList<Boolean>
        private lateinit var mIngredientsList: Array<String>
        private lateinit var mCheckedIngredients: ArrayList<Boolean>
        private lateinit var mCallback: FilterDialogAction
    }
}