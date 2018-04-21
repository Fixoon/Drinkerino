package com.example.oskar.drinkerino.presenters

import com.example.oskar.drinkerino.data.StringProvider
import com.example.oskar.drinkerino.interfaces.BasePresenter
import com.example.oskar.drinkerino.interfaces.FilterDialogContract

class FilterDialogPresenter : FilterDialogContract.Presenter, BasePresenter<FilterDialogContract.View> {
    private var filterDialogFragment: FilterDialogContract.View? = null
    private var stringProvider = StringProvider()
    private var mPropertiesList: Array<String> = stringProvider.getPropertiesStrings()
    private var mBaseSpiritsList: Array<String> = stringProvider.getSpiritStrings()

    private var mTopCheckedBoxes: BooleanArray = BooleanArray(mPropertiesList.size)
    private var mBottomCheckedBoxes: BooleanArray = BooleanArray(mBaseSpiritsList.size)

    private var mTopCheckedBoxesTemp: BooleanArray = BooleanArray(mPropertiesList.size)
    private var mBottomCheckedBoxesTemp: BooleanArray = BooleanArray(mBaseSpiritsList.size)

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
                filterDialogFragment!!.addCheckboxes(mPropertiesList, mBaseSpiritsList, mTopCheckedBoxesTemp, mBottomCheckedBoxesTemp)
            }else{
                filterDialogFragment!!.addCheckboxes(mPropertiesList, mBaseSpiritsList, mTopCheckedBoxes, mBottomCheckedBoxes)
            }
        }
    }

    fun updateCheckedBoxes(checkedProperties: BooleanArray, checkedBaseSpirits: BooleanArray){
        this.mTopCheckedBoxes = checkedProperties
        this.mBottomCheckedBoxes = checkedBaseSpirits
        tempList = false
    }

    fun resetCheckedBoxes(){
        mTopCheckedBoxes = BooleanArray(mPropertiesList.size)
        mBottomCheckedBoxes = BooleanArray(mBaseSpiritsList.size)
        initialize()
    }

    fun setTemporaryState(checkedProperties: BooleanArray, checkedBaseSpirits: BooleanArray){
        mTopCheckedBoxesTemp = checkedProperties
        mBottomCheckedBoxesTemp = checkedBaseSpirits
        tempList = true
    }

    fun removeTemporaryState(){
        mTopCheckedBoxesTemp = BooleanArray(mPropertiesList.size)
        mBottomCheckedBoxesTemp = BooleanArray(mBaseSpiritsList.size)
        tempList = false
    }
}