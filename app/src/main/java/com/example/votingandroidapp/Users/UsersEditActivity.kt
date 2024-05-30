package com.example.votingandroidapp.Users

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class UsersEditActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, UsersEditActivity::class.java)
            intent.putExtra("usersKey", id)
            return intent
        }
    }

    override fun createFragment(): UsersEditFragment {
        val id = intent.getSerializableExtra("usersKey") as Int?
        return UsersEditFragment.newInstance(id)
    }
}