package com.example.oskar.drinkerino.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.view.animation.AnimationUtils
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.DrinkAdapterLikeAction
import com.example.oskar.drinkerino.objects.Filter
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_drink.view.*
import android.app.Activity
import android.util.Log
import com.example.oskar.drinkerino.FilterDialogFragment
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import com.example.oskar.drinkerino.objects.FilterDialogCheckboxes



class MainFragment : Fragment(), DrinkAdapterLikeAction, FilterDialogAction {
    private lateinit var adapter: DrinkAdapter
    private lateinit var filterDialogFrag: FilterDialogFragment
    private val propertiesList = arrayOf("Söt", "Sur", "Besk", "Syrlig", "Salt")
    private val ingredientsList = arrayOf("Rom", "Vodka", "Tequila", "Gin", "Whiskey", "Övriga")
    private var checkedProperties = arrayListOf(false, false, false, false, false)
    private var checkedDrinkBases = arrayListOf(false, false, false, false, false, false)
    private var activeDrinkPos = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListView(getDrinksFromDB(LikeState.IGNORE))

        filterDialogFrag = FilterDialogFragment()
        filterDialogFrag.newInstance(propertiesList, ingredientsList, checkedProperties, checkedDrinkBases)
        filterDialogFrag.setTargetFragment(this, 0)

        Log.d("this is", "a test")
        this.setHasOptionsMenu(true)
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

    private fun getDrinksFromDB(isLiked: LikeState, filter: Filter? = null) : ArrayList<SimpleDrink> {
        val db = DBHelper(activity)
        return db.getDrinksByFilter(isLiked, filter)
    }

    private fun initializeListView(drinkList: ArrayList<SimpleDrink>){

        adapter = DrinkAdapter(activity, drinkList, this)

        mainDrinkList.adapter = adapter
        mainDrinkList.setOnItemClickListener { parent, view, position, id ->
            activeDrinkPos = position
            val drinkID = adapter.getItem(position).id
            val intent = newIntent(activity, drinkID)
            startActivityForResult(intent, 1)
        }
    }

    private fun updateListView(drinkList: ArrayList<SimpleDrink>){
        adapter.clear()
        adapter.addAll(drinkList)

        if(!adapter.isEmpty){
            noMatchText.visibility = View.INVISIBLE
        }else{
            noMatchText.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            filterDialogFrag.show(fragmentManager, "Test")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun filterClick(propertiesDialogCheckboxes: FilterDialogCheckboxes, drinkBaseDialogCheckboxes: FilterDialogCheckboxes, checkOther:Boolean){
        val filter = Filter(propertiesDialogCheckboxes.checkedBoxesNames, drinkBaseDialogCheckboxes.checkedBoxesNames, checkOther)
        checkedProperties = propertiesDialogCheckboxes.checkedBoxes
        checkedDrinkBases = drinkBaseDialogCheckboxes.checkedBoxes

        filterDialogFrag.newInstance(propertiesList, ingredientsList, checkedProperties, checkedDrinkBases)

        updateListView(getDrinksFromDB(LikeState.IGNORE, filter))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result:LikeState = data!!.getSerializableExtra("LikeState") as LikeState
                val drink: SimpleDrink = adapter.getItem(activeDrinkPos)
                drink.likeState = result
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
