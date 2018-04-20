package com.example.oskar.drinkerino

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        private lateinit var instance: MainApplication

        fun getContext() : Context {
            return instance
        }
    }
}