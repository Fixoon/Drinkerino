package com.example.oskar.drinkerino

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_detailed_drink.*


class RecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_drink)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        displayFullRecipe(intent.getIntExtra("drink_id", 0))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun displayFullRecipe(drinkID: Int) {
        val db = DBHandler(this)

        val recipe = db.getFullRecipe(drinkID)

        title = recipe.name

        drinkName.text = recipe.name

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
            textView.text = ((index + 1).toString() + ". " + e)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f)
            instructionsLayout.addView(textView)
        }

    }
}
