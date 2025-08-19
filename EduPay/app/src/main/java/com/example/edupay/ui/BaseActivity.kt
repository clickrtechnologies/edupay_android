package com.example.edupay.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.conatctsync.utils.LocaleHelper
import com.example.edupay.loader.Loader
import java.util.Locale

/***
 *
 */
open class BaseActivity : AppCompatActivity() {
    var containerID = 0

    /***
     * Sip Declaration
     */

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.base_activity_root_view);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /***
     * Replace Current fragment with added fragment
     * @param fragment
     * Fragment need to be add
     * @param containerId
     * Container ID
     */


    fun showProgressDialog() {
        Loader.getInstance().showCustomDialog(this, "")
    }

    fun dismissProgressDialog() {
        Loader.getInstance().cancelCustomDialog()
    }

    fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideKeyboard(view: View?) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


















    /***
     * Replace Current fragment with added fragment and add current fragment to backstack
     * @param fragment
     * Fragment need to be add
     * @param containerId
     * Container ID
     */




    override fun onResume() {
        super.onResume()
        //   uploadReceiver.register(BaseActivity.this);
    }

    override fun onPause() {
        super.onPause()
        // uploadReceiver.unregister(BaseActivity.this);
    }







    override fun onDestroy() {
        super.onDestroy()

    }

    override fun finish() {
        super.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onStart() {
        super.onStart()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
    }







    interface DialogListener {
        fun onOkClick()
    }

    interface DialogMessageInterface {
        fun onDoneClick()
    }

    interface YesNoDialogueInterface {
        fun onYesClicked(position: Int)
        fun onNoClicked(position: Int)
    }

    companion object {
        fun replaceFragmentAddToBackStack(
            fragmentManager: FragmentManager,
            fragment: Fragment, frameId: Int, Tag: String?
        ) {
            //   checkNotNull(fragmentManager);
            //  checkNotNull(fragment);
            val transaction = fragmentManager.beginTransaction()


            //   transaction.setCustomAnimations(R.anim.frag_slide_in,
            //         R.anim.frag_slide_out,R.anim.frag_reverse_slide_out,R.anim.frag_reverse_slide_in);
            transaction.replace(frameId, fragment, Tag).addToBackStack(Tag)
            transaction.commit()
        }
    }

    // Add this extension function or in a BaseActivity
    fun Context.updateLocale(languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = this.resources
        val configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            return createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        return this
    }
}