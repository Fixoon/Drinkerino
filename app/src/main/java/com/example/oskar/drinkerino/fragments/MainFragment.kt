package com.example.oskar.drinkerino.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import com.example.oskar.drinkerino.FilterDialogFragment
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.DrinkAdapterLikeAction
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import com.example.oskar.drinkerino.objects.Filter
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_drink.view.*


class MainFragment : Fragment(), DrinkAdapterLikeAction, FilterDialogAction {
    private lateinit var adapter: DrinkAdapter
    private lateinit var filterDialogFragment: FilterDialogFragment
    private var isInstantiated = false
    private var currentFilter:Filter? = null
    private val propertiesList = arrayOf("Söt", "Sur", "Besk", "Syrlig", "Salt")
    private val baseSpiritsList = arrayOf("Rom", "Vodka", "Tequila", "Gin", "Whiskey", "Övriga")
    private var activeDrinkPosition = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isInstantiated){
            filterDialogFragment = FilterDialogFragment()
            filterDialogFragment.newInstance(propertiesList, baseSpiritsList)
            filterDialogFragment.setTargetFragment(this, 0)

            isInstantiated = true
        }

        initializeListView(getDrinksFromDB(LikeState.IGNORE, currentFilter))

        this.setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initializeListView(drinkList: ArrayList<SimpleDrink>){

        adapter = DrinkAdapter(activity, drinkList, this)

        mainDrinkList.adapter = adapter
        mainDrinkList.setOnItemClickListener { parent, view, position, id ->
            activeDrinkPosition = position
            val drinkID = adapter.getItem(position).id
            val intent = newIntent(activity, drinkID)
            startActivityForResult(intent, 1)
        }
    }

    private fun getDrinksFromDB(isLiked: LikeState, filter: Filter? = null) : ArrayList<SimpleDrink> {
        val db = DBHelper(activity)
        return db.getDrinksByFilter(isLiked, filter)
    }

    private fun updateListView(drinkList: ArrayList<SimpleDrink>){
        adapter.clear()

        if(drinkList.isNotEmpty()){
            adapter.addAll(drinkList)
            noMatchText.visibility = View.INVISIBLE
        }else{
            noMatchText.visibility = View.VISIBLE
        }
    }

    fun resetFragment(){
        filterDialogFragment.resetDialogFragment()
        updateListView(getDrinksFromDB(LikeState.IGNORE))
        mainDrinkList.setSelectionAfterHeaderView()
    }

    override fun likeToggle(position:Int, retView:View){
        val drink: SimpleDrink = adapter.getItem(position)
        val db = DBHelper(activity)
        val likeAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)

        if(drink.likeState == LikeState.NOT_LIKED){
            db.setLikeState(drink.id, LikeState.LIKED)
            drink.likeState = LikeState.LIKED
            retView.likePicture.startAnimation(likeAnimation)
        }else{
            db.setLikeState(drink.id, LikeState.NOT_LIKED)
            drink.likeState = LikeState.NOT_LIKED
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            filterDialogFragment.show(fragmentManager, "FilterDialog")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun filterClick(propertiesDialogCheckboxes: ArrayList<String>,
                             baseSpiritsDialogCheckboxes: ArrayList<String>,
                             checkOther:Boolean){
        currentFilter = Filter(propertiesDialogCheckboxes,
                                baseSpiritsDialogCheckboxes,
                                checkOther)

        updateListView(getDrinksFromDB(LikeState.IGNORE, currentFilter))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result:LikeState = data!!.getSerializableExtra("LikeState") as LikeState
            val drink: SimpleDrink = adapter.getItem(activeDrinkPosition)
            drink.likeState = result
            adapter.notifyDataSetChanged()
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
