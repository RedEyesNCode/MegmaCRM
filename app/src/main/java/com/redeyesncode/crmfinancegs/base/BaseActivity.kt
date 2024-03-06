package com.redeyesncode.crmfinancegs.base

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.snackbar.Snackbar
import com.redeyesncode.crmfinancegs.R
import com.redeyesncode.gsfinancenbfc.base.ContextUtils
import java.util.Locale

open class BaseActivity: AppCompatActivity() {
    private var loadingDialog: AlertDialog? = null
    private var noInternetDialog: AlertDialog? = null

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase!!, Locale("hn"))

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("hn-hn")
        AppCompatDelegate.setApplicationLocales(appLocale)

        super.attachBaseContext(newBase)

    }


//    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        setupNetworkCallBack()
    }

//    private fun setupNetworkCallBack() {
//
//        // Initialize network callback
//        runOnUiThread {
//            networkCallback = object : ConnectivityManager.NetworkCallback() {
//                override fun onAvailable(network: Network) {
//                    super.onAvailable(network)
//                    // Internet connection is available, dismiss the dialog if it's showing
////                    dismissNoInternetDialog()
//                    showToast("Internet Available!")
//
//                }
//
//                override fun onLost(network: Network) {
//                    super.onLost(network)
//                    // Internet connection is lost, show the dialog
////                    showNoInternetDialog()
//                    showSnackbar("Internet Not Available !")
//                }
//            }
//            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val networkRequest = NetworkRequest.Builder().build()
//            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//        }
//
//
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.unregisterNetworkCallback(networkCallback)
//    }





    private fun dismissNoInternetDialog() {
        noInternetDialog?.dismiss()
        noInternetDialog = null
    }


    fun showToast(message: String) {
        // Show a toast message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(message: String) {
        // Show a Snackbar message
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showCustomDialog(title: String, message: String) {
        // Show a custom dialog with an OK button
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun showTopSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        val snackbar = Snackbar.make(view, message, duration)
        val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackbar.view.layoutParams = params
        snackbar.show()
    }

    fun showTopSnackBarFanX(message: String,view:View){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        val snackbarView = snackbar.view
        val layoutParams = snackbarView.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.BOTTOM
//        layoutParams.gravity = Gravity.TOP
        snackbarView.layoutParams = layoutParams
        snackbar.setAction("Dismiss") { snackbar.dismiss() }
        val snackbarText = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.maxLines = 5 // set the maximum number of lines to be displayed

        snackbar.show()

    }
    fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_loading_dialog)
        loadingDialog = builder.create()
        if(!loadingDialog?.isShowing!!){
            loadingDialog?.show()

        }
    }

    fun hideLoadingDialog() {
        loadingDialog?.dismiss()
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}