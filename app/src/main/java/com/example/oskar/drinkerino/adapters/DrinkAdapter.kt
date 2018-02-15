package com.example.oskar.drinkerino.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.oskar.drinkerino.enums.LikeState
import com.example.oskar.drinkerino.interfaces.OnLikeClick
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.objects.SimpleDrink
import kotlinx.android.synthetic.main.item_drink.view.*


class DrinkAdapter constructor(context: Context, val drinks: ArrayList<SimpleDrink>, private val callback: OnLikeClick) : ArrayAdapter<SimpleDrink>(context, 0, drinks) {
    private val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val heartImageArray:ArrayList<Drawable> = arrayListOf(context.getDrawable(R.drawable.ic_heart_outline), context.getDrawable(R.drawable.ic_heart))
    private val glassImageArray:ArrayList<Drawable> = arrayListOf(context.getDrawable(R.drawable.ic_glass_lowball), context.getDrawable(R.drawable.ic_glass_highball),
            context.getDrawable(R.drawable.ic_glass_tulip),context.getDrawable(R.drawable.ic_glass_flute))


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val retView: View

        if(convertView == null){
            retView = layoutInflater.inflate(R.layout.item_drink, parent, false)
        }else{
            retView = convertView
        }

        val drink = getItem(position)
        val drinkName = retView.drinkName as TextView
        val drinkProps = retView.drinkProps as TextView
        var drinkPropertyNames = drink.properties.replace(",", ", ")

        drinkPropertyNames = drinkPropertyNames.substring(0, 1).toUpperCase() + drinkPropertyNames.substring(1).toLowerCase()

        drinkName.text = (drink.name)
        drinkProps.text = (drinkPropertyNames)

        retView.likePicture.setOnClickListener {
            callback.likeToggle(position, retView)
            notifyDataSetChanged()
        }


        retView.likePicture.setImageDrawable(heartImageArray[drink.likeState.boolInt!!])
        if(drink.likeState == LikeState.LIKED) {
            retView.likePicture.setColorFilter(ContextCompat.getColor(context, R.color.heart_red), android.graphics.PorterDuff.Mode.SRC_IN)
        }else{
            retView.likePicture.setColorFilter(ContextCompat.getColor(context, R.color.heart_gray), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        retView.glassImage.setImageDrawable(glassImageArray[drink.drinkGlass.glassID])

        return retView
    }

}