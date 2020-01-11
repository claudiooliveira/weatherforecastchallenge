package com.example.weatherforecastchallenge.utils

import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

inline fun AppCompatActivity.transact(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction().apply {
        action()
    }.commit()
}