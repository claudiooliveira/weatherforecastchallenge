package com.example.weatherforecastchallenge.utils

import android.content.Context
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.example.weatherforecastchallenge.R

inline fun AppCompatActivity.transact(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction().apply {
        action()
    }.commit()
}