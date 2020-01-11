package com.example.weatherforecastchallenge

import com.example.weatherforecastchallenge.home.HomeFragment
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeFragmentUnitTest {
    @Test
    fun fragment_ShouldNOT_be_Null() {
        val fragment = HomeFragment()
        assertNotNull(fragment)
    }
}
