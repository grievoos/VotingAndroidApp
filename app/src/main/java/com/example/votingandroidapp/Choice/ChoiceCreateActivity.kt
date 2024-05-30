package com.example.votingandroidapp.Choice

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class ChoiceCreateActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, ChoiceCreateActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): ChoiceCreateFragment {
        return ChoiceCreateFragment.newInstance()
    }
}