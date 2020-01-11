package com.example.weatherforecastchallenge.home

import java.lang.ref.WeakReference


object HomeConfigurator {

    fun configureFragment(fragment: HomeFragment) {

        val presenter = HomePresenter()
        presenter.output = WeakReference(fragment)

        val interactor = HomeInteractor()
        interactor.output = presenter

        fragment.output = interactor
    }
}