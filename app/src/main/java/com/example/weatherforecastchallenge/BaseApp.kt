package com.example.weatherforecastchallenge

import android.app.Application
import android.content.Context
import com.birbit.android.jobqueue.JobManager
import com.birbit.android.jobqueue.config.Configuration

class BaseApp : Application() {

    companion object {

        private var instance: BaseApp? = null
        private var jobManager: JobManager? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun configureJobManager() {
            var config : Configuration.Builder = Configuration.Builder(instance!!.baseContext)
                .consumerKeepAlive(Configuration.DEFAULT_THREAD_KEEP_ALIVE_SECONDS)
                .maxConsumerCount(Configuration.MAX_CONSUMER_COUNT)
                .minConsumerCount(Configuration.MIN_CONSUMER_COUNT)

            jobManager = JobManager(config.build())
        }

        fun getCurrentJobManager() : JobManager? {
            if (jobManager == null)
                configureJobManager()
            return jobManager
        }


    }

    init {
        instance = this
    }


}