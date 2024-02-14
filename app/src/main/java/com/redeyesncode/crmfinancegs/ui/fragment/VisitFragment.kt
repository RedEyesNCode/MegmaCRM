package com.redeyesncode.crmfinancegs.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.FragmentVisitBinding
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
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
 * Use the [VisitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VisitFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentVisitBinding

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
        // Inflate the layout for this fragment

        binding = FragmentVisitBinding.inflate(layoutInflater)
        (activity?.application as AndroidApp).getDaggerComponent().injectFragmentVisit(this@VisitFragment)


        initClicks()
        initialApiCall()

        return binding.root
    }

    private fun initialApiCall() {
        val user = AppSession(requireContext()).getObject(Constant.USER_LOGIN,LoginUserResponse::class.java) as LoginUserResponse

        val visitUserMap = HashMap<String,String>()
        visitUserMap.put("userId", user.data?.userId.toString())


    }

    private fun initClicks() {


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VisitFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VisitFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}