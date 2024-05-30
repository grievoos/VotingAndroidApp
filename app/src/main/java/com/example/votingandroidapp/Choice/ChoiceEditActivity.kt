package com.example.votingandroidapp.Choice

import android.content.Context
import android.content.Intent
import com.example.votingandroidapp.SingleFragmentActivity

class ChoiceEditActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, ChoiceEditActivity::class.java)
            intent.putExtra("choiceKey", id)
            return intent
        }
    }

    override fun createFragment(): ChoiceEditFragment {
        val id = intent.getSerializableExtra("choiceKey") as Int?
        return ChoiceEditFragment.newInstance(id)
    }
}