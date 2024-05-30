package com.example.votingandroidapp.Choice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingandroidapp.Question.QuestionListActivity
import com.example.votingandroidapp.R
import com.example.votingandroidapp.Users.UsersListActivity
import com.example.votingandroidapp.Vote.VoteListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChoiceListFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var choiceAdapter: ChoiceAdapter? = null

    private val httpClient = OkHttpClient()
    private var choiceList = mutableListOf<Choice>()

    private lateinit var createBtn: Button
    private lateinit var voteBtn: Button
    private lateinit var usersBtn: Button
    private lateinit var questionBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.choice_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.choiceListContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        createBtn = view.findViewById(R.id.createBtn)
        voteBtn = view.findViewById(R.id.voteBtn)
        usersBtn = view.findViewById(R.id.usersBtn)
        questionBtn = view.findViewById(R.id.questionBtn)

        createBtn.setOnClickListener{
            val intent = ChoiceCreateActivity.newIntent(context)
            context?.startActivity(intent)
        }

        voteBtn.setOnClickListener {
            val intent = VoteListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        usersBtn.setOnClickListener {
            val intent = UsersListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        questionBtn.setOnClickListener {
            val intent = QuestionListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.60:5000/Choice").build()
        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                choiceList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    choiceList.add(
                        Choice(step.getInt("id"), step.getInt("question_id"),
                            step.getInt("user_id"), step.getString("user_choice"),
                            step.getString("content"), step.getString("fullname"))
                    )
                }
            }

        })

        if (choiceAdapter == null){
            choiceAdapter = ChoiceAdapter(choiceList)
            recyclerView!!.adapter = choiceAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.item_name)

        private lateinit var choice: Choice

        @SuppressLint("SetTextI18n")
        fun binding(choice1: Choice){
            this.choice = choice1
            itemNameView?.text = choice1.fullname + " " + choice1.userChoice
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = ChoiceEditActivity.newIntent(context, choice.id)
            context.startActivity(intent)
        }

    }


    private class ChoiceAdapter(choiceList1: List<Choice>?): RecyclerView.Adapter<Holder?>(){
        private var choices: List<Choice>? = null
        init {this.choices = choiceList1}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val choice = choices!![position]
            holder.binding(choice)
        }

        override fun getItemCount(): Int {
            return choices!!.size
        }


    }
}