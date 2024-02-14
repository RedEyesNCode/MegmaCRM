package com.redeyesncode.moneyview.dagger



import com.redeyesncode.crmfinancegs.dagger.ViewModelModule
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.crmfinancegs.ui.activity.LoginActivity
import dagger.Component

@Component(modules = [RetrofitInstance::class, ViewModelModule::class, RepoModule::class])
interface DaggerComp {



    fun injectLoginActivity(loginActivity: LoginActivity)




}