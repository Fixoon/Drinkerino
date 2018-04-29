package com.example.oskar.drinkerino.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.*
import android.widget.CheckBox
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.adapters.CheckBoxAdapter
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_filter, container)

        temporaryCheckBoxState = true

        view!!.cancelButton.setOnClickListener {
            temporaryCheckBoxState = false
            dismiss()
        }

        view.actionButton.setOnClickListener {
            val checkBoxes = getCheckedBoxes()
            @Suppress("UNCHECKED_CAST")
            mCallback.filterClick(checkBoxes[0] as ArrayList<String>, checkBoxes[1] as ArrayList<String>, (gridViewBottom.adapter.getItem(5) as CheckBox).isChecked)
            presenter!!.updateCheckedBoxes(checkBoxes[2] as BooleanArray, checkBoxes[3] as BooleanArray)
            temporaryCheckBoxState = false
            dismiss()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.supportLoaderManager.initLoader(1003, null, this)
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
        if(temporaryCheckBoxState) {
            val checkedBoxes = getCheckedBoxes()
            presenter!!.setTemporaryState(checkedBoxes[2] as BooleanArray, checkedBoxes[3] as BooleanArray)
        }
        presenter!!.detachView()
        super.onPause()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<FilterDialogPresenter> {
        return PresenterLoader(context!!, FilterDialogPresenter())
    }

    override fun onLoadFinished(loader: Loader<FilterDialogPresenter>, data: FilterDialogPresenter?) {
        this.presenter = data
    }

    override fun onLoaderReset(loader: Loader<FilterDialogPresenter>) {

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

    override fun addCheckboxes(topCheckBoxNames: Array<String>, bottomCheckBoxNames: Array<String>, topCheckedBoxes: BooleanArray, bottomCheckedBoxes: BooleanArray) {
        gridViewTop.adapter = CheckBoxAdapter(context!!, topCheckBoxNames, topCheckedBoxes)
        gridViewBottom.adapter = CheckBoxAdapter(context!!, bottomCheckBoxNames, bottomCheckedBoxes)
    }

    private fun getCheckedBoxes(): Array<Any> {
        val topCheckedBoxesNames: ArrayList<Any> = arrayListOf()
        val bottomCheckedBoxesNames: ArrayList<Any> = arrayListOf()

        val topCheckBoxStates = BooleanArray(gridViewTop.adapter.count)
        val bottomCheckBoxStates = BooleanArray(gridViewBottom.adapter.count)


        for (i in 0 until gridViewTop.adapter.count){
            val checkBox = gridViewTop.adapter.getItem(i) as CheckBox
            topCheckBoxStates[i] = checkBox.isChecked
            if(checkBox.isChecked){
                topCheckedBoxesNames.add(checkBox.text.toString())
            }
        }

        for (i in 0 until gridViewBottom.adapter.count){
            val checkBox = gridViewBottom.adapter.getItem(i) as CheckBox
            bottomCheckBoxStates[i] = checkBox.isChecked
            if(checkBox.isChecked){
                bottomCheckedBoxesNames.add(checkBox.text.toString())
            }
        }

        return arrayOf(topCheckedBoxesNames, bottomCheckedBoxesNames, topCheckBoxStates, bottomCheckBoxStates)
    }

    fun resetDialogFragment() {
        if (presenter != null){
            presenter!!.resetCheckedBoxes()
        }else{
            resetOnResume = true
        }
    }
}