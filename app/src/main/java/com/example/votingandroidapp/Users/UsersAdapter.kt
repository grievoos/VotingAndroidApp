package com.example.votingandroidapp.Users

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.votingandroidapp.R

class UsersAdapter(context: Context, list: List<Users>) : ArrayAdapter<Users>(context, 0 , list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val users = getItem(position)
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.selector, parent, false)
        if (users != null){
            val name = view.findViewById<TextView>(R.id.name)
            name.text = users.firstName + " " + users.lastName
        }
        return view
    }
}