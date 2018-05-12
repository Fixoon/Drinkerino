package com.example.oskar.drinkerino.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.*
import android.widget.TextView
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.dialogs.FilterDialogFragment
import com.example.oskar.drinkerino.interfaces.DrinkAdapterLikeAction
import com.example.oskar.drinkerino.interfaces.FilterDialogAction
import com.example.oskar.drinkerino.interfaces.MainContract
import com.example.oskar.drinkerino.objects.SimpleDrink
import com.example.oskar.drinkerino.presenters.MainPresenter
import com.example.oskar.drinkerino.presenters.PresenterLoader
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), DrinkAdapterLikeAction, FilterDialogAction, MainContract.View, LoaderManager.LoaderCallbacks<MainPresenter> {
    private lateinit var adapter: DrinkAdapter
    private lateinit var filterDialogFragment: FilterDialogFragment
    private var presenter: MainPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setHasOptionsMenu(true)
        activity!!.title = getString(R.string.title_home)

        attachAdapter()
        addFilterDialog()

        activeFilterCloseButton.setOnClickListener { view ->
            resetFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            filterDialogFragment.show(fragmentManager, "FilterDialog")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.supportLoaderManager.initLoader(1001, null, this)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachView(this)
    }

    override fun onPause() {
        presenter!!.detachView()
        super.onPause()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<MainPresenter> {
        return PresenterLoader(context!!, MainPresenter())
    }

    override fun onLoadFinished(loader: Loader<MainPresenter>, data: MainPresenter?) {
        this.presenter = data
    }

    override fun onLoaderReset(loader: Loader<MainPresenter>) {
        this.presenter = null
    }

    override fun setDrinkList(drinkList: ArrayList<SimpleDrink>) {
        adapter.clear()
        adapter.addAll(drinkList)
    }

    override fun showNoResultText() {
        noMatchText.visibility = View.VISIBLE
    }

    override fun hideNoResultText() {
        noMatchText.visibility = View.INVISIBLE
    }

    override fun navigateToRecipe(drinkID: Int){
        val intent = newIntent(activity!!, drinkID)
        startActivity(intent)
    }

    override fun filterClick(propertiesDialogCheckboxes: ArrayList<String>,
                             baseSpiritsDialogCheckboxes: ArrayList<String>,
                             checkOther: Boolean) {
        presenter!!.onFilterClicked(propertiesDialogCheckboxes, baseSpiritsDialogCheckboxes, checkOther)
    }

    override fun setActiveFilter(properties: String?, baseSpirits: String?){
        activeFilterLayout.visibility = View.VISIBLE
        bottomLinearLayout.removeAllViews()
        topLinearLayout.removeAllViews()

        if(properties != null){
            val propertiesTitleTextView = TextView(context)
            propertiesTitleTextView.text = getString(R.string.active_filter_properties) + ": "
            propertiesTitleTextView.setTextColor(Color.rgb(255, 255, 255))
            propertiesTitleTextView.setTypeface(null, Typeface.BOLD)

            val propertiesTextView = TextView(context)
            propertiesTextView.text = properties + " "
            propertiesTextView.setTextColor(Color.rgb(255, 255, 255))

            topLinearLayout.addView(propertiesTitleTextView)
            topLinearLayout.addView(propertiesTextView)
        }
        if(baseSpirits != null){
            val baseSpiritTitleTextView = TextView(context)
            baseSpiritTitleTextView.text = getString(R.string.active_filter_basespirit) + ": "
            baseSpiritTitleTextView.setTextColor(Color.rgb(255, 255, 255))
            baseSpiritTitleTextView.setTypeface(null, Typeface.BOLD)

            val baseSpiritTextView = TextView(context)
            baseSpiritTextView.text = baseSpirits
            baseSpiritTextView.setTextColor(Color.rgb(255, 255, 255))

            if(properties != null && properties.length + baseSpirits.length > 24){
                bottomLinearLayout.addView(baseSpiritTitleTextView)
                bottomLinearLayout.addView(baseSpiritTextView)
            }else{
                topLinearLayout.addView(baseSpiritTitleTextView)
                topLinearLayout.addView(baseSpiritTextView)
            }

        }
    }

    override fun hideActiveFilter() {
        activeFilterLayout.visibility = View.GONE
    }

    private fun attachAdapter(){
        adapter = DrinkAdapter(activity!!, ArrayList(), this)
        mainDrinkList.adapter = adapter
        mainDrinkList.setOnItemClickListener{parent, view, position, id ->
            presenter!!.onItemClicked(adapter.getItem(position))
        }
    }

    private fun addFilterDialog(){
        filterDialogFragment = FilterDialogFragment()
        filterDialogFragment.setTargetFragment(this, 0)
    }

    fun resetFragment() {
        filterDialogFragment.resetDialogFragment()
        presenter!!.resetDrinkList()
        mainDrinkList.setSelectionAfterHeaderView()
    }

    override fun likeToggle(position: Int) {
        presenter!!.likeToggle(adapter.getItem(position))
    }

    companion object {
        fun newIntent(context: Context, drinkID: Int): Intent {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("drink_id", drinkID)
            return intent
        }
    }

}
