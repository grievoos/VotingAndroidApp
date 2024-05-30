package com.example.votingandroidapp.Vote

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
import com.example.votingandroidapp.Choice.ChoiceListActivity
import com.example.votingandroidapp.Question.QuestionListActivity
import com.example.votingandroidapp.R
import com.example.votingandroidapp.Users.UsersListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class VoteListFragment: Fragment() {
    private var recView: RecyclerView? = null
    private var voteAdapter: VoteAdapter? = null
    private val httpClient = OkHttpClient()
    private var votesList = mutableListOf<Vote>()
    private lateinit var createButton: Button
    private lateinit var usersButton: Button
    private lateinit var questionButton: Button
    private lateinit var choiceButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.vote_list_fragment, container, false)
        recView = view.findViewById(R.id.choiceListContainer)
        recView!!.layoutManager = LinearLayoutManager(activity)
        createButton = view.findViewById(R.id.createBtn)
        usersButton = view.findViewById(R.id.usersBtn)
        questionButton = view.findViewById(R.id.questionBtn)
        choiceButton = view.findViewById(R.id.choiceBtn)

        createButton.setOnClickListener{
            val intent = VoteCreateActivity.newIntent(context)
            context?.startActivity(intent)
        }

        usersButton.setOnClickListener {
            val intent = UsersListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        questionButton.setOnClickListener {
            val intent = QuestionListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        choiceButton.setOnClickListener {
            val intent = ChoiceListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.60:5000/Vote").build()
        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                votesList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    votesList.add(Vote(step.getInt("id"), step.getString("title"),
                        step.getString("start_date"), step.getString("finish_date"),
                        step.getString("status")))
                }
            }

        })

        if (voteAdapter == null){
            voteAdapter = VoteAdapter(votesList)
            recView!!.adapter = voteAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.item_name)

        private lateinit var vote: Vote

        @SuppressLint("SetTextI18n")
        fun binding(vote1: Vote){
            this.vote = vote1
            itemNameView?.text = vote1.title
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = VoteEditActivity.newIntent(context, vote.id)
            context.startActivity(intent)
        }

    }


    private class VoteAdapter(voteList: List<Vote>?): RecyclerView.Adapter<Holder?>(){
        private var votes: List<Vote>? = null
        init {this.votes = voteList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val post = votes!![position]
            holder.binding(post)
        }

        override fun getItemCount(): Int {
            return votes!!.size
        }


    }
}