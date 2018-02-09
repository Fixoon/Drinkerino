package com.example.oskar.drinkerino

import android.view.View

interface OnLikeClick {
    /**
     * Toggle like state of a drink
     *
     * @param position Position of drink in current list in adapter
     * @param retView Main view of drink
     */
    fun likeToggle(position: Int, retView: View){

    }
}