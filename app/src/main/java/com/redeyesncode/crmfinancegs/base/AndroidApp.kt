package com.redeyesncode.moneyview.base

import android.app.Application
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.moneyview.dagger.DaggerComp
import com.redeyesncode.moneyview.dagger.DaggerDaggerComp

class AndroidApp: Application() {

    lateinit var daggerComp: DaggerComp
    override fun onCreate() {
        super.onCreate()
        // need to create all the modules in the base application class.

        daggerComp = DaggerDaggerComp.builder().retrofitInstance(RetrofitInstance()).build()


    }

    fun getDaggerComponent(): DaggerComp {
        return daggerComp

    }
}