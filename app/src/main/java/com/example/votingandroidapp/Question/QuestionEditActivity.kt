package com.example.votingandroidapp.Question

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class QuestionEditActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, QuestionEditActivity::class.java)
            intent.putExtra("questionKey", id)
            return intent
        }
    }

    override fun createFragment(): QuestionEditFragment {
        val id = intent.getSerializableExtra("questionKey") as Int?
        return QuestionEditFragment.newInstance(id)
    }
}