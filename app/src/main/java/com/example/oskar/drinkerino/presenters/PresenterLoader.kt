package com.example.oskar.drinkerino.presenters

import android.content.Context
import android.support.v4.content.Loader
import com.example.oskar.drinkerino.interfaces.BasePresenter

class PresenterLoader<T:BasePresenter<V>, V> (context: Context, private var presenter: T) : Loader<T>(context) {
    override fun onStartLoading() {
        deliverResult(presenter)
    }
}