package com.example.oskar.drinkerino.presenters

import com.example.oskar.drinkerino.interfaces.BasePresenter
import com.example.oskar.drinkerino.interfaces.FilterDialogContract

class FilterDialogPresenter : FilterDialogContract.Presenter, BasePresenter<FilterDialogContract.View> {
    private var filterDialogFragment: FilterDialogContract.View? = null
    private var mPropertiesList: Array<String> = arrayOf("Söt", "Sur", "Besk", "Syrlig", "Salt")
    private var mBaseSpiritsList: Array<String> = arrayOf("Rom", "Vodka", "Tequila", "Gin", "Whiskey", "Övriga")
    private var mCheckedProperties: BooleanArray = BooleanArray(mPropertiesList.size)
    private var mCheckedBaseSpirits: BooleanArray = BooleanArray(mBaseSpiritsList.size)

    private var mCheckedPropertiesTemp: BooleanArray = BooleanArray(mPropertiesList.size)
    private var mCheckedBaseSpiritsTemp: BooleanArray = BooleanArray(mBaseSpiritsList.size)

    var tempList = false

    override fun attachView(view: FilterDialogContract.View) {
        filterDialogFragment = view
        initialize()
    }

    override fun detachView() {
        filterDialogFragment = null
    }

    private fun initialize(){
        if(filterDialogFragment != null){
            if(tempList){
                filterDialogFragment!!.addCheckboxes(mPropertiesList, mBaseSpiritsList, mCheckedPropertiesTemp, mCheckedBaseSpiritsTemp)
            }else{
                filterDialogFragment!!.addCheckboxes(mPropertiesList, mBaseSpiritsList, mCheckedProperties, mCheckedBaseSpirits)
            }
        }
    }

    fun updateCheckedBoxes(checkedProperties: BooleanArray, checkedBaseSpirits: BooleanArray){
        this.mCheckedProperties = checkedProperties
        this.mCheckedBaseSpirits = checkedBaseSpirits
        tempList = false
    }

    fun resetCheckedBoxes(){
        mCheckedProperties = BooleanArray(mPropertiesList.size)
        mCheckedBaseSpirits = BooleanArray(mBaseSpiritsList.size)
        initialize()
    }

    fun setTemporaryState(checkedProperties: BooleanArray, checkedBaseSpirits: BooleanArray){
        mCheckedPropertiesTemp = checkedProperties
        mCheckedBaseSpiritsTemp = checkedBaseSpirits
        tempList = true
    }

    fun removeTemporaryState(){
        mCheckedPropertiesTemp = BooleanArray(mPropertiesList.size)
        mCheckedBaseSpiritsTemp = BooleanArray(mBaseSpiritsList.size)
        tempList = false
    }
}