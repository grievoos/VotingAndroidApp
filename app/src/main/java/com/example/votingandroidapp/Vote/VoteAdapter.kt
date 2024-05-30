package com.example.votingandroidapp.Vote

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.votingandroidapp.R

class VoteAdapter(context: Context, list: List<Vote>) : ArrayAdapter<Vote>(context, 0 , list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vote = getItem(position)
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.selector, parent, false)
        if (vote != null){
            val name = view.findViewById<TextView>(R.id.name)
            name.text = vote.title
        }
        return view
    }
}