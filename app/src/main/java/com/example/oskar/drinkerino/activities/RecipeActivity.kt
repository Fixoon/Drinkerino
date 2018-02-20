package com.example.oskar.drinkerino.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Menu
import android.widget.TextView
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.enums.LikeState
import kotlinx.android.synthetic.main.activity_detailed_drink.*


class RecipeActivity : AppCompatActivity() {
    private lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_drink)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.recipe_menu, menu)
        this.menu = menu
        displayFullRecipe(intent.getIntExtra("drink_id", 0))
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun displayFullRecipe(drinkID: Int) {
        val db = DBHelper(this)

        val recipe = db.getFullRecipe(drinkID)

        title = recipe.name

        drinkName.text = recipe.name

        if(recipe.likeState == LikeState.LIKED){
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
