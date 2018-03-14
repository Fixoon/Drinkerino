package com.example.oskar.drinkerino.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.view.*


class FilterDialogFragment : DialogFragment(), FilterDialogAction {
    private lateinit var mPropertiesList: Array<String>
    private lateinit var mCheckedProperties: BooleanArray
    private lateinit var mBaseSpiritsList: Array<String>
    private lateinit var mCheckedBaseSpirits: BooleanArray
    private lateinit var mCallback: FilterDialogAction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mCallback = targetFragment as FilterDialogAction
        } catch (e: ClassCastException) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.dialog_filter, container)

        addCheckboxes(mPropertiesList, mBaseSpiritsList, view)

        view.cancelButton.setOnClickListener {
            dismiss()
        }

        view.actionButton.setOnClickListener {
            val checkBoxes = getCheckedBoxes()
            mCallback.filterClick(checkBoxes[0], checkBoxes[1], (baseSpiritLayoutRight.getChildAt(2) as CheckBox).isChecked)
            dismiss()
        }

        return view
    }

    fun newInstance(propertiesList: Array<String>,
                    ingredientsList: Array<String>): FilterDialogFragment {

        mPropertiesList = propertiesList
        mBaseSpiritsList = ingredientsList

        mCheckedProperties = BooleanArray(propertiesList.size)
        mCheckedBaseSpirits = BooleanArray(ingredientsList.size)

        return FilterDialogFragment()
    }

    private fun addCheckboxes(propertiesList: Array<String>, ingredientsList: Array<String>, view: View) {
        propertiesList.forEachIndexed { index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedProperties[index]
            newCheckBox.text = s
            if (index % 2 == 1) {
                view.propertyLayoutRight.addView(newCheckBox)
            } else {
                view.propertyLayoutLeft.addView(newCheckBox)
            }
        }
        ingredientsList.forEachIndexed { index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedBaseSpirits[index]
            newCheckBox.text = s
            if (index % 2 == 1) {
                view.baseSpiritLayoutRight.addView(newCheckBox)
            } else {
                view.baseSpiritLayoutLeft.addView(newCheckBox)
            }
        }
    }

    private fun getCheckedBoxes(): Array<ArrayList<String>> {
        val properties: ArrayList<String> = arrayListOf()
        val baseSpirits: ArrayList<String> = arrayListOf()


        var propertyIndex = 0
        for (i in 0 until (propertyLayoutLeft.childCount + propertyLayoutRight.childCount)) {
            if (i % 2 == 0) {
                val checkBox = propertyLayoutLeft.getChildAt(propertyIndex) as CheckBox
                mCheckedProperties[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    properties.add(checkBox.text.toString())
                }
            } else {
                val checkBox = propertyLayoutRight.getChildAt(propertyIndex) as CheckBox
                mCheckedProperties[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    properties.add(checkBox.text.toString())
                }
                propertyIndex++
            }
        }

        var baseSpiritIndex = 0
        for (i in 0 until (baseSpiritLayoutLeft.childCount + baseSpiritLayoutRight.childCount)) {
            if (i % 2 == 0) {
                val checkBox = baseSpiritLayoutLeft.getChildAt(baseSpiritIndex) as CheckBox
                mCheckedBaseSpirits[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    baseSpirits.add(checkBox.text.toString())
                }
            } else {
                val checkBox = baseSpiritLayoutRight.getChildAt(baseSpiritIndex) as CheckBox
                mCheckedBaseSpirits[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    baseSpirits.add(checkBox.text.toString())
                }
                baseSpiritIndex++
            }
        }

        return arrayOf(properties, baseSpirits)
    }

    fun resetDialogFragment() {
        mCheckedProperties = BooleanArray(mPropertiesList.size)
        mCheckedBaseSpirits = BooleanArray(mBaseSpiritsList.size)
    }
}