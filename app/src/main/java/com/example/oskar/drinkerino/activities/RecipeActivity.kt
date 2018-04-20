package com.example.oskar.drinkerino.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.objects.Drink
import kotlinx.android.synthetic.main.activity_recipe.*


class RecipeActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    private lateinit var recipe: Drink

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val db = DBHelper()

        recipe = db.getFullRecipe(intent.getIntExtra("drink_id", 0))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        this.menu = menu

        displayRecipe()

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            likeToggle()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun likeToggle() {
        val db = DBHelper()
        if (recipe.likeState == LikeState.LIKED) {
            db.setLikeState(recipe.id, LikeState.NOT_LIKED)
            menu.findItem(R.id.action_filter).icon = ContextCompat.getDrawable(this, R.drawable.ic_heart_outline)
            recipe.likeState = LikeState.NOT_LIKED
        } else {
            db.setLikeState(recipe.id, LikeState.LIKED)
            menu.findItem(R.id.action_filter).icon = ContextCompat.getDrawable(this, R.drawable.ic_heart)
            recipe.likeState = LikeState.LIKED
        }
    }

    private fun displayRecipe() {
        title = recipe.name

        drinkName.text = recipe.name

        if (recipe.likeState == LikeState.LIKED) {
            menu.findItem(R.id.action_filter).icon = ContextCompat.getDrawable(this, R.drawable.ic_heart)
        }

        for (i in recipe.ingredients) {
            val textView = TextView(this)
            textView.text = i
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
            ingredientsLayout.addView(textView)
        }

        for (i in recipe.measurements) {
            val textView = TextView(this)
            textView.text = i
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
            measurementLayout.addView(textView)
        }

        val instrArray: List<String> = recipe.instructions.split(("~ "))
        instrArray.forEachIndexed { index, e ->
            val textView = TextView(this)
            textView.text = ((index + 1).toString() + " " + e)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
            instructionsLayout.addView(textView)
        }

    }
}
