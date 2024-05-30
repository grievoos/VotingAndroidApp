package com.example.votingandroidapp.Vote

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class VoteCreateActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, VoteCreateActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): VoteCreateFragment {
        return VoteCreateFragment.newInstance()
    }
}