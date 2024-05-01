package com.redeyesncode.crmfinancegs.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.base.BaseFragment
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.data.UserLeadResponse
import com.redeyesncode.crmfinancegs.databinding.FragmentApproveBinding
import com.redeyesncode.crmfinancegs.ui.adapter.UserLeadAdapter
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ApproveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApproveFragment : BaseFragment(),UserLeadAdapter.onClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentApproveBinding

    override fun onLeadEMI(data: UserLeadResponse.Data) {
        val leadEmiSheet = LeadEmiBottomSheet(requireContext(),data)
        leadEmiSheet.show(requireFragmentManager(),"LEAD-EMI")

    }

    override fun onLeadInfo(data: UserLeadResponse.Data) {

        val createVisitBottomSheet = LeadInfoBottomSheet(requireContext(),data)
        createVisitBottomSheet.show(requireFragmentManager(),"LEAD-INFO")
    }

    @Inject
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentApproveBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        (activity?.application as AndroidApp).getDaggerComponent().injectApproveFragment(this@ApproveFragment)
        attachObservers()
        initialApiCall()

        return binding.root
    }

    private fun initialApiCall() {
        val user = AppSession(requireContext()).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse
        val userLeadMap = hashMapOf<String,String>()
        userLeadMap.put("userId",user.data?.userId.toString())
        mainViewModel.getUserApprovedLeads(userLeadMap)

    }

    private fun attachObservers() {
        mainViewModel.responseUserApprovedLead.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                dismissLoadingDialog()
                showToast(it)
                binding.ivNoData.visibility = View.VISIBLE
                binding.recvLeadsApproved.visibility = View.GONE
            },
            onSuccess = {
                dismissLoadingDialog()


                binding.ivNoData.visibility = View.GONE
                binding.recvLeadsApproved.visibility = View.VISIBLE
                binding.recvLeadsApproved.apply {
                    adapter = UserLeadAdapter(fragmentContext,it.data,this@ApproveFragment)
                }


            }

        ))



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ApproveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ApproveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}