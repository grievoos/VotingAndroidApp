package com.example.votingandroidapp.Vote

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class VoteEditActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, VoteEditActivity::class.java)
            intent.putExtra("voteKey", id)
            return intent
        }
    }

    override fun createFragment(): VoteEditFragment {
        val id = intent.getSerializableExtra("voteKey") as Int?
        return VoteEditFragment.newInstance(id)
    }
}