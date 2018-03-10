package com.example.oskar.drinkerino.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.DrinkAdapterLikeAction
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.fragment_likes.*
import java.util.*


class LikesFragment : Fragment(), DrinkAdapterLikeAction {
    private lateinit var adapter: DrinkAdapter
    private var activeDrinkPosition = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_likes, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListView(getDrinksFromDB(LikeState.LIKED))
        toggleText()
    }

    fun resetFragment(){
        likeDrinkList.setSelectionAfterHeaderView()
    }

    override fun likeToggle(position: Int, retView:View){
        val drink: SimpleDrink = adapter.getItem(position)
        val db = DBHelper(context)

        db.setLikeState(drink.id, LikeState.NOT_LIKED)
        drink.likeState = LikeState.NOT_LIKED
        adapter.drinks.removeAt(position)
        toggleText()

        val undoSnackbar = Snackbar.make(view!!, drink.name + " " +
                getString(R.string.snackbar_drink_removed), Snackbar.LENGTH_LONG)

                .setAction(R.string.snackbar_drink_undo, { view ->
                    adapter.drinks.add(position, drink)
                    drink.likeState = LikeState.LIKED
                    db.setLikeState(drink.id, LikeState.LIKED)
                    adapter.notifyDataSetChanged()
                    toggleText()
                })
        undoSnackbar.show()
    }

    private fun toggleText(){
        if(!adapter.isEmpty){
            noLikedText.visibility = View.INVISIBLE
        }else{
            noLikedText.visibility = View.VISIBLE
        }
    }

    private fun getDrinksFromDB(isLiked: LikeState) : ArrayList<SimpleDrink> {
        val db = DBHelper(activity)
        return db.getDrinksByFilter(isLiked)
    }

    private fun initializeListView(drinkList: ArrayList<SimpleDrink>){
        adapter = DrinkAdapter(activity, drinkList, this)

        val listView = likeDrinkList as ListView

        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            activeDrinkPosition = position
            val drinkID = adapter.getItem(position).id
            val intent = newIntent(activity, drinkID)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result:LikeState = data!!.getSerializableExtra("LikeState") as LikeState
            if(result == LikeState.NOT_LIKED){
                adapter.drinks.removeAt(activeDrinkPosition)
                toggleText()
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newIntent(context: Context, drinkID: Int): Intent {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("drink_id" , drinkID)
            return intent
        }
    }
}
