package com.redeyesncode.crmfinancegs.dagger

import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideMainViewModel(repository: DefaultDashboardRepo): MainViewModel {
        return MainViewModel(dashboardRepo = repository)
    }
}