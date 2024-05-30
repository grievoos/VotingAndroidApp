package com.example.votingandroidapp.Users

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
import com.example.votingandroidapp.Vote.VoteListActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class UsersListFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var usersAdapter: UsersAdapter? = null

    private val httpClient = OkHttpClient()
    private var usersList = mutableListOf<Users>()

    private lateinit var createBtn: Button
    private lateinit var voteBtn: Button
    private lateinit var questionBtn: Button
    private lateinit var choiceBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.users_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.usersListContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        createBtn = view.findViewById(R.id.createBtn)
        voteBtn = view.findViewById(R.id.voteBtn)
        questionBtn = view.findViewById(R.id.questionBtn)
        choiceBtn = view.findViewById(R.id.choiceBtn)

        createBtn.setOnClickListener{
            val intent = UsersCreateActivity.newIntent(context)
            context?.startActivity(intent)
        }

        voteBtn.setOnClickListener {
            val intent = VoteListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        questionBtn.setOnClickListener {
            val intent = QuestionListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        choiceBtn.setOnClickListener {
            val intent = ChoiceListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.60:5000/Users").build()
        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                usersList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    usersList.add(Users(step.getInt("id"), step.getString("first_name"),
                        step.getString("last_name"), step.getString("email"),
                        step.getString("phone"), step.getString("status"),))
                }
            }

        })

        if (usersAdapter == null){
            usersAdapter = UsersAdapter(usersList)
            recyclerView!!.adapter = usersAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.item_name)

        private lateinit var users: Users

        @SuppressLint("SetTextI18n")
        fun binding(users1: Users){
            this.users = users1
            itemNameView?.text = users.firstName + " " + users.lastName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = UsersEditActivity.newIntent(context, users.id)
            context.startActivity(intent)
        }

    }


    private class UsersAdapter(usersList: List<Users>?): RecyclerView.Adapter<Holder?>(){
        private var users: List<Users>? = null
        init {this.users = usersList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val user = users!![position]
            holder.binding(user)
        }

        override fun getItemCount(): Int {
            return users!!.size
        }


    }
}