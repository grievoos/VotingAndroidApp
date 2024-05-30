package com.example.votingandroidapp.Vote

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.SingleFragmentActivity

class VoteListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, VoteListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = VoteListFragment()
}