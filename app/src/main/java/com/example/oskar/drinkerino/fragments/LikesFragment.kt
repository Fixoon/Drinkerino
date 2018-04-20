package com.example.oskar.drinkerino.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.activities.RecipeActivity
import com.example.oskar.drinkerino.adapters.DrinkAdapter
import com.example.oskar.drinkerino.interfaces.DrinkAdapterLikeAction
import com.example.oskar.drinkerino.interfaces.LikesContract
import com.example.oskar.drinkerino.objects.SimpleDrink
import com.example.oskar.drinkerino.presenters.LikesPresenter
import com.example.oskar.drinkerino.presenters.PresenterLoader
import kotlinx.android.synthetic.main.fragment_likes.*


class LikesFragment : Fragment(), DrinkAdapterLikeAction, LikesContract.View, LoaderManager.LoaderCallbacks<LikesPresenter> {
    private lateinit var adapter: DrinkAdapter
    private var presenter: LikesPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_likes, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.title = getString(R.string.title_liked)

        adapter = DrinkAdapter(activity, arrayListOf(), this)
        likeDrinkList.adapter = adapter
        likeDrinkList.setOnItemClickListener { parent, view, position, id ->
            presenter!!.onItemClicked(adapter.getItem(position))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.supportLoaderManager.initLoader(1002, null, this)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attachView(this)
    }

    override fun onPause() {
        presenter!!.detachView()
        super.onPause()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<LikesPresenter> {
        return PresenterLoader(context, LikesPresenter())
    }

    override fun onLoadFinished(loader: Loader<LikesPresenter>?, data: LikesPresenter?) {
        this.presenter = data
    }

    override fun onLoaderReset(loader: Loader<LikesPresenter>?) {
        this.presenter = null
    }

    override fun setDrinkList(drinkList: ArrayList<SimpleDrink>) {
        adapter.clear()
        adapter.addAll(drinkList)
    }

    override fun showNoLikesText() {
        noLikedText.visibility = View.VISIBLE
    }

    override fun hideNoLikesText() {
        noLikedText.visibility = View.INVISIBLE
    }

    override fun navigateToRecipe(drinkID: Int){
        val intent = newIntent(activity, drinkID)
        startActivityForResult(intent, 1)
    }

    override fun likeToggle(position: Int) {
        val drink: SimpleDrink = adapter.getItem(position)

        presenter!!.likeToggle(drink, position)

        val snackbar = Snackbar.make(view!!,drink.name + " " + getString(R.string.snackbar_drink_removed), Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.snackbar_drink_undo, {
            presenter!!.likeToggle(drink, position)
        })
        snackbar.show()
    }

    fun resetFragment() {
        likeDrinkList.setSelectionAfterHeaderView()
    }


    companion object {
        fun newIntent(context: Context, drinkID: Int): Intent {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("drink_id", drinkID)
            return intent
        }
    }
}
