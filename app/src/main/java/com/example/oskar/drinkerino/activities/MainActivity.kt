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

class MainActivity : AppCompatActivity(){

    private var mainFragment = MainFragment()
    private var likesFragment = LikesFragment()

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        var mainFragmentByTag = supportFragmentManager.findFragmentByTag("mainFragment") as MainFragment?
                        if (mainFragmentByTag != null && mainFragmentByTag.isVisible) {
                            mainFragmentByTag.resetFragment()
                        }
                        if(mainFragmentByTag == null){
                            mainFragmentByTag = mainFragment
                        }
                        supportFragmentManager.beginTransaction().replace(
                                R.id.frameLayout,
                                mainFragmentByTag,
                                "mainFragment").commit()

                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_liked -> {
                        var likesFragmentByTag = supportFragmentManager.findFragmentByTag("likesFragment") as LikesFragment?
                        if (likesFragmentByTag != null && likesFragmentByTag.isVisible) {
                            likesFragmentByTag.resetFragment()
                        }
                        if(likesFragmentByTag == null){
                            likesFragmentByTag = likesFragment
                        }
                        supportFragmentManager.beginTransaction().replace(
                                R.id.frameLayout,
                                likesFragmentByTag,
                                "likesFragment").commit()

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

        val db = DBHelper()

        if (!db.checkDBExist()) {
            try {
                db.copyDataBase()
            } catch (ioe: IOException) {
                throw Error("Unable to create database")
            }
        }

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().add(R.id.frameLayout, mainFragment, "mainFragment").commit()
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
