package com.example.votingandroidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class SingleFragmentActivity: FragmentActivity() {
    protected abstract fun createFragment(): Fragment
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.frameContainer)
        if (fragment == null){
            fragment = createFragment()
            fm.beginTransaction().add(R.id.frameContainer, fragment).commit()
        }

    }
}