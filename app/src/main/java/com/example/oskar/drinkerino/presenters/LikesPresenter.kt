package com.example.oskar.drinkerino.presenters

import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.BasePresenter
import com.example.oskar.drinkerino.interfaces.LikesContract
import com.example.oskar.drinkerino.objects.Filter
import com.example.oskar.drinkerino.objects.SimpleDrink

class LikesPresenter : LikesContract.Presenter, BasePresenter<LikesContract.View> {
    private var drinkListView: LikesContract.View? = null
    private val db = DBHelper()
    private var drinkList = getDrinksFromDB(LikeState.LIKED)

    private fun getDrinksFromDB(isLiked: LikeState, filter: Filter? = null): ArrayList<SimpleDrink> {
        return db.getDrinksByFilter(isLiked, filter)
    }

    override fun attachView(view: LikesContract.View) {
        drinkListView = view
        drinkList = getDrinksFromDB(LikeState.LIKED)
        updateListView()
    }

    override fun detachView() {
        drinkListView = null
    }

    private fun updateListView() {
        drinkListView!!.setDrinkList(drinkList)
        if(drinkList.isEmpty()){
            drinkListView!!.showNoLikesText()
        } else {
            drinkListView!!.hideNoLikesText()
        }
    }

    fun likeToggle(drink: SimpleDrink, position: Int){
        if(drink.likeState == LikeState.LIKED){
            db.setLikeState(drink.id, LikeState.NOT_LIKED)
            drink.likeState = LikeState.NOT_LIKED
            drinkList.removeAt(position)
        }else{
            db.setLikeState(drink.id, LikeState.LIKED)
            drink.likeState = LikeState.LIKED
            drinkList.add(position, drink)
        }
        updateListView()
    }

    fun onItemClicked(drink: SimpleDrink){
        drinkListView!!.navigateToRecipe(drink.id)
    }
}