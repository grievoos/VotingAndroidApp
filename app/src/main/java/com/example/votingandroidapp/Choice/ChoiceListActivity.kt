package com.example.votingandroidapp.Choice

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.SingleFragmentActivity

class ChoiceListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, ChoiceListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = ChoiceListFragment()
}