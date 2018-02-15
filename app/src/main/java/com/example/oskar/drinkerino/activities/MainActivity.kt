package com.example.oskar.drinkerino.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.example.oskar.drinkerino.fragments.LikesFragment
import com.example.oskar.drinkerino.fragments.MainFragment
import com.example.oskar.drinkerino.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainFragment = MainFragment()
    private val likesFragment = LikesFragment()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentManager.beginTransaction().replace(R.id.frameLayout, mainFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_liked -> {
                fragmentManager.beginTransaction().replace(R.id.frameLayout, likesFragment).commit()
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

        fragmentManager.beginTransaction().replace(R.id.frameLayout, mainFragment).commit()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}