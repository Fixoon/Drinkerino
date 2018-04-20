package com.example.oskar.drinkerino.presenters

import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.MainContract
import com.example.oskar.drinkerino.interfaces.BasePresenter
import com.example.oskar.drinkerino.objects.Filter
import com.example.oskar.drinkerino.objects.SimpleDrink

class MainPresenter : MainContract.Presenter, BasePresenter<MainContract.View> {
    private var drinkListView: MainContract.View? = null
    private val db = DBHelper()
    private var filter: Filter? = null
    private var drinkList = getDrinksFromDB(LikeState.IGNORE, filter)

    override fun attachView(view: MainContract.View) {
        drinkListView = view
        drinkList = getDrinksFromDB(LikeState.IGNORE, filter)
        updateListView()
    }

    override fun detachView() {
        drinkListView = null
    }

    private fun getDrinksFromDB(isLiked: LikeState, filter: Filter? = null): ArrayList<SimpleDrink> {
        return db.getDrinksByFilter(isLiked, filter)
    }

    private fun updateListView() {
        drinkListView!!.setDrinkList(drinkList)
        if(drinkList.isEmpty()){
            drinkListView!!.showNoResultText()
        } else {
            drinkListView!!.hideNoResultText()
        }
    }

    override fun resetDrinkList() {
        filter = null
        drinkList = getDrinksFromDB(LikeState.IGNORE, filter)
        updateListView()
    }

    override fun likeToggle(drink: SimpleDrink) {
        if(drink.likeState == LikeState.NOT_LIKED){
            db.setLikeState(drink.id, LikeState.LIKED)
            drink.likeState = LikeState.LIKED
        } else {
            db.setLikeState(drink.id, LikeState.NOT_LIKED)
            drink.likeState = LikeState.NOT_LIKED
        }
    }

    override fun onItemClicked(drink: SimpleDrink) {
        drinkListView!!.navigateToRecipe(drink.id)
    }

    override fun onFilterClicked(propertiesDialogCheckboxes: ArrayList<String>,
                                 baseSpiritsDialogCheckboxes: ArrayList<String>,
                                 checkOther: Boolean) {
        filter = Filter(propertiesDialogCheckboxes, baseSpiritsDialogCheckboxes, checkOther)
        drinkList = getDrinksFromDB(LikeState.IGNORE, filter)

        updateListView()
    }
}