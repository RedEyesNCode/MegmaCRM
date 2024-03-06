package com.redeyesncode.crmfinancegs.base

import android.app.Application
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.crmfinancegs.dagger.DaggerComp
import com.redeyesncode.crmfinancegs.dagger.DaggerDaggerComp

class AndroidApp: Application() {

    lateinit var daggerComp: DaggerComp
//    lateinit var daggerCompLoanAdmin : DaggerDaggerCompLoanAdmin
    override fun onCreate() {
        super.onCreate()
        // need to create all the modules in the base application class.

    daggerComp = DaggerDaggerComp.builder().retrofitInstance(RetrofitInstance()).build()

//        daggerCompLoanAdmin = DaggerDaggerCompLoanAdmin.builder().retrofitInstance(RetrofitInstanceLoanAdmin()).build()

    }

    fun getDaggerComponent(): DaggerComp {
        return daggerComp

    }
}