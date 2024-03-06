package com.redeyesncode.crmfinancegs.dagger




import com.redeyesncode.crmfinancegs.network.RetrofitInstance
import com.redeyesncode.crmfinancegs.network.RetrofitInstanceLoanAdmin
import com.redeyesncode.crmfinancegs.ui.activity.LeadDocumentActivity
import com.redeyesncode.crmfinancegs.ui.activity.LeadSearchActivity
import com.redeyesncode.crmfinancegs.ui.activity.LoginActivity
import com.redeyesncode.crmfinancegs.ui.activity.VisitSearchActivity
import com.redeyesncode.crmfinancegs.ui.fragment.ApproveFragment
import com.redeyesncode.crmfinancegs.ui.fragment.CreateLoanUserBottonSheet
import com.redeyesncode.crmfinancegs.ui.fragment.CreateVisitBottomSheet
import com.redeyesncode.crmfinancegs.ui.fragment.LeadsFragment
import com.redeyesncode.crmfinancegs.ui.fragment.VisitFragment

import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.DashboardActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.EMandateActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.LoanEmiActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.LoanReviewActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.PackageAssignActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.SignupActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.UpiViewActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.UploadDocumentActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.VerifyIncomeActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.fragment.OtpVerificationFragment
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.PendingActivity
import com.redeyesncode.crmfinancegs.ui.loanadmin.activity.SelectBankActivity
import dagger.Component

@Component(modules = [RetrofitInstance::class, RetrofitInstanceLoanAdmin::class,ViewModelModule::class, RepoModule::class])
interface DaggerComp {



    fun injectLoginActivity(loginActivity: LoginActivity)
    fun injectLeadDocumentActivity(loginActivity: LeadDocumentActivity)



    fun injectLeadSearch(leadSearchActivity: LeadSearchActivity)
    fun injectFragmentVisit(fragment: VisitFragment)


    fun injectLeadFragment(fragment: LeadsFragment)

    fun injectApproveFragment(fragment: ApproveFragment)

    fun injectVisitSearch(visitSearchActivity: VisitSearchActivity)

    fun injectCreateLoanUser(createLoanUserBottonSheet: CreateLoanUserBottonSheet)


    fun injectCreateVisitBottomSheet(createVisitBottomSheet: CreateVisitBottomSheet)


    fun injectDashboardActivityLOANADMIN(dashboardActivity: DashboardActivity)

    fun injectSignupActivity(signupActivity: SignupActivity)


    fun injectOtpFragment(otpFragment: OtpVerificationFragment)

    fun injectDashboardActivity(dashboardActivity: com.redeyesncode.crmfinancegs.ui.activity.DashboardActivity)

    fun injectPendingActivity(pendingActivity: PendingActivity)


    fun injectUploadDocsActivity(uploadDocumentActivity: UploadDocumentActivity)

    fun injectVerifyBank(injectVerifyIncomeActivity: VerifyIncomeActivity)


    fun injectPackageAssignActivity(packageAssignActivity: PackageAssignActivity)


    fun injectLoanReviewScreen(injectLoanReviewActivity: LoanReviewActivity)

    fun injectEMandateActivity(eMandateActivity: EMandateActivity)


    fun injectBankActivity(addSelectBankActivity: SelectBankActivity)
    fun injectLoamEmiActivity(loanEmiActivity: LoanEmiActivity)
    fun injectUpiViewActivity(loanEmiActivity: UpiViewActivity)
}