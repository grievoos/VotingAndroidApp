package com.example.votingandroidapp.Users

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.SingleFragmentActivity

class UsersListActivity: SingleFragmentActivity(){
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, UsersListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = UsersListFragment()
}