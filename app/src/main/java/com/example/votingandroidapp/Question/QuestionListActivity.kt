package com.example.votingandroidapp.Question

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.SingleFragmentActivity

class QuestionListActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, QuestionListActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = QuestionListFragment()
}