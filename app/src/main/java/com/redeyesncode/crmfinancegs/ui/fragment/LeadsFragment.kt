package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.base.BaseFragment
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.FragmentLeadsBinding
import com.redeyesncode.crmfinancegs.ui.activity.CreateLeadActivity
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
 * Use the [LeadsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeadsFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentLeadsBinding

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

        binding = FragmentLeadsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        (activity?.application as AndroidApp).getDaggerComponent().injectLeadFragment(this@LeadsFragment)



        binding.fabAddLead.setOnClickListener{

            val intentCreateLead = Intent(fragmentContext,CreateLeadActivity::class.java)
            startActivity(intentCreateLead)

        }

        initialApiCall()
        attachObservers()


        return binding.root
    }

    private fun attachObservers() {

        mainViewModel.userLeadResponse.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {
                        showLoadingDialog()

            },
            onError = {
                      dismissLoadingDialog()

            },
            onSuccess = {
                dismissLoadingDialog()
                binding.recvVisit.apply {
                    adapter = UserLeadAdapter(requireContext(),it.data)
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                }


            }


        ))


    }

    private fun initialApiCall() {
        val user = AppSession(requireContext()).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse

        val visitUserMap = HashMap<String,String>()
        visitUserMap.put("userId", user.data?.userId.toString())

        mainViewModel.getUserLeads(visitUserMap)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LeadsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LeadsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}