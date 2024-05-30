package com.example.votingandroidapp.Question

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class QuestionCreateActivity: SingleFragmentActivity() {
    companion object {
        fun newIntent(packageContext: Context?): Intent? {
            val intent = Intent(packageContext, QuestionCreateActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): QuestionCreateFragment {
        return QuestionCreateFragment.newInstance()
    }
}