package com.example.votingandroidapp.Users

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class UsersCreateActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, UsersCreateActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): UsersCreateFragment {
        return UsersCreateFragment.newInstance()
    }
}