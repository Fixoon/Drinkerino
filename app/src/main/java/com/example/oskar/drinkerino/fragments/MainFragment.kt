package com.example.oskar.drinkerino.fragments


import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.LinearLayout
import com.example.oskar.drinkerino.*
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.data.DBHandler
import com.example.oskar.drinkerino.objects.FilterObject
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.OnLikeClick
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_drink.view.*


/**
 * Fragment for main tab
 */
class MainFragment : Fragment(), OnLikeClick {
    private lateinit var adapter: DrinkAdapter
    private lateinit var filterDialog: Dialog
    private val propertiesList = arrayOf("Söt", "Sur", "Besk", "Syrlig", "Salt")
    private val ingredientsList = arrayOf("Rom", "Vodka", "Tequila", "Gin", "Whiskey", "Övriga")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListView(getDrinksFromDB(LikeState.IGNORE))

        filterDialog = FilterDialog(propertiesList, ingredientsList, activity)

        this.setHasOptionsMenu(true)
    }

    /**
     * Toggle drinks like state
     *
     * @param position Position of drink in current list in adapter
     * @param retView Drinks main View
     */
    override fun likeToggle(position:Int, retView:View){
        val drink: SimpleDrink = adapter.getItem(position)
        val db = DBHandler(activity)
        val likeAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)

        if(drink.likeState == LikeState.NOT_LIKED){
            db.setLikeState(drink.drinkID, LikeState.LIKED)
            drink.likeState = LikeState.LIKED
            retView.likePicture.startAnimation(likeAnimation)
        }else{
            db.setLikeState(drink.drinkID, LikeState.NOT_LIKED)
            drink.likeState = LikeState.NOT_LIKED
        }
    }

    private fun getDrinksFromDB(isLiked: LikeState, filter: FilterObject? = null) : ArrayList<SimpleDrink> {
        val db = DBHandler(activity)
        return db.getDrinksByFilter(isLiked, filter)
    }

    private fun initializeListView(drinkList: ArrayList<SimpleDrink>){

        adapter = DrinkAdapter(activity, drinkList, this)

        mainDrinkList.adapter = adapter
        mainDrinkList.setOnItemClickListener { parent, view, position, id ->
            val drinkID = adapter.getItem(position).drinkID
            val intent = newIntent(activity, drinkID)
            startActivity(intent)
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
            openFilterDialog()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun openFilterDialog(){
        filterDialog.show()

        filterDialog.cancelButton.setOnClickListener {
            filterDialog.dismiss()
        }

        filterDialog.actionButton.setOnClickListener {
            filterDrinks()
            filterDialog.dismiss()
        }
    }

    private fun filterDrinks(){
        val propertyViewList:Array<LinearLayout> = arrayOf(filterDialog.propertyLayoutLeft, filterDialog.propertyLayoutRight)
        val ingredientViewList:Array<LinearLayout> = arrayOf(filterDialog.drinkBaseLayoutLeft, filterDialog.drinkBaseLayoutRight)

        val filter = FilterObject(getCheckedBoxes(propertyViewList),
                getCheckedBoxes(ingredientViewList),
                (filterDialog.drinkBaseLayoutRight.getChildAt(2) as CheckBox).isChecked)

        updateListView(getDrinksFromDB(LikeState.IGNORE, filter))
    }

    /**
     * Get text value of all checked checkboxes within views provided
     *
     * @param parentViews Parent views of checkboxes
     */
    private fun getCheckedBoxes(parentViews:Array<LinearLayout>) : ArrayList<String>{
        val checkedBoxes:ArrayList<String> = arrayListOf()

        parentViews.forEachIndexed{ index, l ->
            (0 until l.childCount)
                    .map { l.getChildAt(it) as CheckBox }
                    .filter { it.isChecked }
                    .mapTo(checkedBoxes) { it.text.toString() }
        }

        return checkedBoxes
    }

    companion object {
        fun newIntent(context: Context, drinkID: Int): Intent {

            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("drink_id" , drinkID)
            return intent
        }
    }

}
