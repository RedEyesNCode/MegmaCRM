package com.redeyesncode.moneyview.dagger

import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import dagger.Module
import dagger.Provides


@Module
class RepoModule {

    @Provides
    fun provideDashboardRepo(): DefaultDashboardRepo {

        return DefaultDashboardRepo()
    }

}