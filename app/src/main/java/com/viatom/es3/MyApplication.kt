package com.viatom.es3

import android.app.Application
import android.content.res.Resources

class MyApplication: Application() {
    companion object{
        lateinit var myresources: Resources
    }

    override fun onCreate() {
        super.onCreate()
       myresources=resources
    }
}