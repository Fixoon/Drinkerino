package com.example.oskar.drinkerino.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import com.example.oskar.drinkerino.interfaces.FilterDialogContract
import com.example.oskar.drinkerino.presenters.FilterDialogPresenter
import com.example.oskar.drinkerino.presenters.PresenterLoader
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.view.*


class FilterDialogFragment : DialogFragment(), FilterDialogAction, FilterDialogContract.View, LoaderManager.LoaderCallbacks<FilterDialogPresenter> {
    private lateinit var mCallback: FilterDialogAction
    private var presenter: FilterDialogPresenter? = null
    private var resetOnResume = false
    private var temporaryCheckBoxState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mCallback = targetFragment as FilterDialogAction
        } catch (e: ClassCastException) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.dialog_filter, container)

        temporaryCheckBoxState = true

        view.cancelButton.setOnClickListener {
            temporaryCheckBoxState = false
            dismiss()
        }

        view.actionButton.setOnClickListener {
            val checkBoxes = getCheckedBoxes()
            mCallback.filterClick(checkBoxes[0] as ArrayList<String>, checkBoxes[1] as ArrayList<String>, (baseSpiritLayoutRight.getChildAt(2) as CheckBox).isChecked)
            presenter!!.updateCheckedBoxes(checkBoxes[2] as BooleanArray, checkBoxes[3] as BooleanArray)
            temporaryCheckBoxState = false
            dismiss()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.supportLoaderManager.initLoader(1003, null, this)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachView(this)
        if(resetOnResume){
            presenter!!.resetCheckedBoxes()
            resetOnResume = false
        }
    }

    override fun onPause() {
        presenter!!.detachView()
        super.onPause()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<FilterDialogPresenter> {
        return PresenterLoader(context, FilterDialogPresenter())
    }

    override fun onLoadFinished(loader: Loader<FilterDialogPresenter>?, data: FilterDialogPresenter?) {
        this.presenter = data
    }

    override fun onLoaderReset(loader: Loader<FilterDialogPresenter>?) {

    }

    //Discard checkbox changes if user presses outside of dialog to dismiss
    override fun onCancel(dialog: DialogInterface?) {
        temporaryCheckBoxState = false
        super.onCancel(dialog)
    }

    override fun onDestroyView() {
        if(temporaryCheckBoxState) {
            val checkedBoxes = getCheckedBoxes()
            presenter!!.setTemporaryState(checkedBoxes[2] as BooleanArray, checkedBoxes[3] as BooleanArray)
        }else{
            presenter!!.removeTemporaryState()
        }
        super.onDestroyView()
    }

    override fun addCheckboxes(propertiesList: Array<String>, ingredientsList: Array<String>, mCheckedProperties: BooleanArray, mCheckedBaseSpirits: BooleanArray) {
        propertyLayoutLeft.removeAllViews()
        propertyLayoutRight.removeAllViews()
        baseSpiritLayoutLeft.removeAllViews()
        baseSpiritLayoutRight.removeAllViews()

        propertiesList.forEachIndexed { index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedProperties[index]
            newCheckBox.text = s
            if (index % 2 == 1) {
                view!!.propertyLayoutRight.addView(newCheckBox)
            } else {
                view!!.propertyLayoutLeft.addView(newCheckBox)
            }
        }
        ingredientsList.forEachIndexed { index, s ->
            val newCheckBox = CheckBox(activity)
            newCheckBox.isChecked = mCheckedBaseSpirits[index]
            newCheckBox.text = s
            if (index % 2 == 1) {
                view!!.baseSpiritLayoutRight.addView(newCheckBox)
            } else {
                view!!.baseSpiritLayoutLeft.addView(newCheckBox)
            }
        }
    }

    private fun getCheckedBoxes(): Array<Any> {
        val properties: ArrayList<Any> = arrayListOf()
        val baseSpirits: ArrayList<Any> = arrayListOf()

        val propertyChildren = propertyLayoutLeft.childCount + propertyLayoutRight.childCount
        val baseSpiritChildren = baseSpiritLayoutLeft.childCount + baseSpiritLayoutRight.childCount

        val checkedProperties = BooleanArray(propertyChildren)
        val checkedBaseSpirits = BooleanArray(baseSpiritChildren)

        var propertyIndex = 0
        for (i in 0 until propertyChildren) {
            if (i % 2 == 0) {
                val checkBox = propertyLayoutLeft.getChildAt(propertyIndex) as CheckBox
                checkedProperties[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    properties.add(checkBox.text.toString())
                }
            } else {
                val checkBox = propertyLayoutRight.getChildAt(propertyIndex) as CheckBox
                checkedProperties[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    properties.add(checkBox.text.toString())
                }
                propertyIndex++
            }
        }

        var baseSpiritIndex = 0
        for (i in 0 until baseSpiritChildren) {
            if (i % 2 == 0) {
                val checkBox = baseSpiritLayoutLeft.getChildAt(baseSpiritIndex) as CheckBox
                checkedBaseSpirits[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    baseSpirits.add(checkBox.text.toString())
                }
            } else {
                val checkBox = baseSpiritLayoutRight.getChildAt(baseSpiritIndex) as CheckBox
                checkedBaseSpirits[i] = checkBox.isChecked
                if (checkBox.isChecked) {
                    baseSpirits.add(checkBox.text.toString())
                }
                baseSpiritIndex++
            }
        }

        return arrayOf(properties, baseSpirits, checkedProperties, checkedBaseSpirits)
    }

    fun resetDialogFragment() {
        if (presenter != null){
            presenter!!.resetCheckedBoxes()
        }else{
            resetOnResume = true
        }
    }
}