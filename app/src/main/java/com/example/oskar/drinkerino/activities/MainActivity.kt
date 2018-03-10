package com.example.oskar.drinkerino.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.oskar.drinkerino.R
import com.example.oskar.drinkerino.data.DBHelper
import com.example.oskar.drinkerino.fragments.LikesFragment
import com.example.oskar.drinkerino.fragments.MainFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mainFragment = MainFragment()
    private val likesFragment = LikesFragment()

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayout,
                        mainFragment,
                        "mainFragment").commit()
                val mainFragmentByTag = mainFragment.fragmentManager.findFragmentByTag("mainFragment")
                if(mainFragmentByTag != null && mainFragmentByTag.isVisible){
                    mainFragment.resetFragment()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_liked -> {
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout,
                                                                likesFragment,
                                                                "likesFragment").commit()
                val likesFragmentByTag = likesFragment.fragmentManager.findFragmentByTag("likesFragment")
                if(likesFragmentByTag != null && likesFragmentByTag.isVisible){
                    likesFragment.resetFragment()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DBHelper(this)

        if(!db.checkDBExist()){
            try {
                db.copyDataBase()
            } catch (ioe: IOException) {
                throw Error("Unable to create database")
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, mainFragment).commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
