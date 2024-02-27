package com.redeyesncode.moneyview.dagger



import com.redeyesncode.crmfinancegs.dagger.ViewModelModule
import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.crmfinancegs.ui.activity.DashboardActivity
import com.redeyesncode.crmfinancegs.ui.activity.LeadDocumentActivity
import com.redeyesncode.crmfinancegs.ui.activity.LeadSearchActivity
import com.redeyesncode.crmfinancegs.ui.activity.LoginActivity
import com.redeyesncode.crmfinancegs.ui.activity.VisitSearchActivity
import com.redeyesncode.crmfinancegs.ui.fragment.ApproveFragment
import com.redeyesncode.crmfinancegs.ui.fragment.CreateLoanUserBottonSheet
import com.redeyesncode.crmfinancegs.ui.fragment.CreateVisitBottomSheet
import com.redeyesncode.crmfinancegs.ui.fragment.LeadsFragment
import com.redeyesncode.crmfinancegs.ui.fragment.VisitFragment
import dagger.Component

@Component(modules = [RetrofitInstance::class, ViewModelModule::class, RepoModule::class])
interface DaggerComp {



    fun injectLoginActivity(loginActivity: LoginActivity)
    fun injectLeadDocumentActivity(loginActivity: LeadDocumentActivity)



    fun injectLeadSearch(leadSearchActivity: LeadSearchActivity)
    fun injectFragmentVisit(fragment: VisitFragment)


    fun injectLeadFragment(fragment:LeadsFragment)

    fun injectApproveFragment(fragment:ApproveFragment)

    fun injectVisitSearch(visitSearchActivity: VisitSearchActivity)

    fun injectCreateLoanUser(createLoanUserBottonSheet: CreateLoanUserBottonSheet)


    fun injectCreateVisitBottomSheet(createVisitBottomSheet: CreateVisitBottomSheet)


    fun injectDashboardActivity(dashboardActivity: DashboardActivity)


}