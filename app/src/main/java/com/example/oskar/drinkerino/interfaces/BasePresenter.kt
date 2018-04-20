package com.example.oskar.drinkerino.interfaces

interface BasePresenter<V> {
    fun attachView(view: V)
    fun detachView()
}