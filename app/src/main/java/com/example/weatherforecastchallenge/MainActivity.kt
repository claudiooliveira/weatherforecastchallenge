package com.example.weatherforecastchallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.weatherforecastchallenge.home.HomeFragment
import com.example.weatherforecastchallenge.utils.transact
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.hide()
        showFragment(HomeFragment())
    }

    private fun showFragment(fragment: Fragment) {
        transact {
            replace(R.id.frame, fragment)
            setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
        }
    }

}