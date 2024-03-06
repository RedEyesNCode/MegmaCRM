package com.redeyesncode.crmfinancegs.dagger

import com.redeyesncode.crmfinancegs.repository.DefaultDashboardRepo
import com.redeyesncode.crmfinancegs.ui.loanadmin.DefaultDashboardLoanAdmin
import dagger.Module
import dagger.Provides


@Module
class RepoModule {

    @Provides
    fun provideDashboardRepo(): DefaultDashboardRepo {

        return DefaultDashboardRepo()
    }

    @Provides
    fun provideLoanAdminRepo(): DefaultDashboardLoanAdmin {

        return DefaultDashboardLoanAdmin()
    }

}