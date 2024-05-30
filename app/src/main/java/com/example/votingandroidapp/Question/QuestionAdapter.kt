package com.example.votingandroidapp.Question

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.votingandroidapp.R

class QuestionAdapter(context: Context, list: List<Question>) : ArrayAdapter<Question>(context, 0 , list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    @SuppressLint("SetTextI18n")
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val question = getItem(position)
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.selector, parent, false)
        if (question != null){
            val name = view.findViewById<TextView>(R.id.name)
            name.text = question.content
        }
        return view
    }
}