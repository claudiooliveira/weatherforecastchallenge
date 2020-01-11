package com.example.weatherforecastchallenge

import com.example.weatherforecastchallenge.home.HomeFragment
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class HomeFragmentUnitTest {
    @Test
    fun fragment_ShouldNOT_be_Null() {
        val fragment = HomeFragment()
        assertNotNull(fragment)
    }
}
