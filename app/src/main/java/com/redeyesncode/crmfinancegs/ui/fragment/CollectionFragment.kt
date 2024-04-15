package com.redeyesncode.crmfinancegs.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.crmfinancegs.base.BaseFragment
import com.redeyesncode.crmfinancegs.data.LoginUserResponse
import com.redeyesncode.crmfinancegs.databinding.FragmentCollectionBinding
import com.redeyesncode.crmfinancegs.ui.activity.CollectionActivity
import com.redeyesncode.crmfinancegs.ui.adapter.CollectionAdapter
import com.redeyesncode.crmfinancegs.ui.viewmodel.MainViewModel
import com.redeyesncode.gsfinancenbfc.base.Event
import com.redeyesncode.moneyview.base.AndroidApp
import com.redeyesncode.redbet.session.AppSession
import com.redeyesncode.redbet.session.Constant
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [CollectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectionFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    lateinit var binding:FragmentCollectionBinding

    @Inject
    lateinit var mainViewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCollectionBinding.inflate(layoutInflater)
        (activity?.application as AndroidApp).getDaggerComponent().injectCollectionFragment(this@CollectionFragment)

        initClicks()
        attachObservers()
        initialApiCall()


        return binding.root
    }

    private fun initialApiCall() {

        val map = hashMapOf<String,String>()
        val user = AppSession(requireContext()).getObject(
            Constant.USER_LOGIN,
            LoginUserResponse::class.java) as LoginUserResponse
        map.put("user_id",user.data?.userId.toString())
        map.put("userId",user.data?.userId.toString())
        mainViewModel.getUserCollection(map)

    }

    private fun initClicks() {

        binding.fabAddCollection.setOnClickListener {
            val intentCollectionFragment = Intent(requireContext(),CollectionActivity::class.java)
            startActivity(intentCollectionFragment)
        }


    }
    private fun attachObservers(){

        mainViewModel.getUserCollectionResponse.observe(viewLifecycleOwner,Event.EventObserver(

            onLoading = {
                showLoadingDialog()
            },
            onError = {
                dismissLoadingDialog()
                showToast(it)
                binding.ivNoData.visibility = View.VISIBLE
                binding.recvVisit.visibility = View.GONE
            },
            onSuccess = {
                dismissLoadingDialog()
                binding.recvVisit.adapter = CollectionAdapter(requireContext(),it.data)
                binding.recvVisit.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

                binding.ivNoData.visibility = View.GONE
                binding.recvVisit.visibility = View.VISIBLE
            }

        ))


    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CollectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): CollectionFragment {
            val fragment = CollectionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }
}