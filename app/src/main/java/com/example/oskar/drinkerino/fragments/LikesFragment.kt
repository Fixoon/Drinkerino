package com.example.oskar.drinkerino.fragments


import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.oskar.drinkerino.*
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.data.DBHandler
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.OnLikeClick
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.fragment_likes.*
import java.util.*


class LikesFragment : Fragment(), OnLikeClick {
    lateinit var adapter: DrinkAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_likes, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var drinkList = getDrinksFromDB(LikeState.LIKED)
        initializeListView(drinkList)
        toggleText()
    }

    /**
     * Toggle drinks like state
     *
     * @param position Position of drink in current list in adapter
     * @param retView Drinks main View
     */
    override fun likeToggle(position: Int, retView:View){
        var drink: SimpleDrink = adapter.getItem(position)
        val db = DBHandler(context)

        db.setLikeState(drink.drinkID, LikeState.NOT_LIKED)
        drink.likeState = LikeState.NOT_LIKED
        adapter.drinks.removeAt(position)
        toggleText()

        val undoSnackbar = Snackbar.make(view, drink.name + " " + getString(R.string.snackbar_drink_removed), Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_drink_undo, { view ->
                    adapter.drinks.add(position, drink)
                    drink.likeState = LikeState.LIKED
                    db.setLikeState(drink.drinkID, LikeState.LIKED)
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
        val db = DBHandler(activity)
        return db.getDrinksByFilter(isLiked)
    }

    private fun initializeListView(drinkList: ArrayList<SimpleDrink>){

        adapter = DrinkAdapter(activity, drinkList, this)

        val listView = likeDrinkList as ListView

        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val drinkID = adapter.getItem(position).drinkID
            val intent = newIntent(activity, drinkID)
            startActivity(intent)
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
