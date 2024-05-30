package com.example.votingandroidapp.Question

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

class QuestionListFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var questionsAdapter: QuestionsAdapter? = null

    private val httpClient = OkHttpClient()
    private var questionsList = mutableListOf<Question>()

    private lateinit var createBtn: Button
    private lateinit var voteBtn: Button
    private lateinit var usersBtn: Button
    private lateinit var choiceBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.question_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.choiceListContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        createBtn = view.findViewById(R.id.createBtn)
        voteBtn = view.findViewById(R.id.voteBtn)
        usersBtn = view.findViewById(R.id.usersBtn)
        choiceBtn = view.findViewById(R.id.choiceBtn)

        createBtn.setOnClickListener{
            val intent = QuestionCreateActivity.newIntent(context)
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

        choiceBtn.setOnClickListener {
            val intent = ChoiceListActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.60:5000/Question").build()
        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                questionsList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    questionsList.add(Question(step.getInt("id"), step.getInt("vote_id"),
                        step.getString("content"), step.getString("vote_date"),
                        step.getString("title")))
                }
            }

        })

        if (questionsAdapter == null){
            questionsAdapter = QuestionsAdapter(questionsList)
            recyclerView!!.adapter = questionsAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.item_name)

        private lateinit var question: Question

        @SuppressLint("SetTextI18n")
        fun binding(question1: Question){
            this.question = question1
            itemNameView?.text = question1.content
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = QuestionEditActivity.newIntent(context, question.id)
            context.startActivity(intent)
        }

    }


    private class QuestionsAdapter(questionList: List<Question>?): RecyclerView.Adapter<Holder?>(){
        private var questions: List<Question>? = null
        init {this.questions = questionList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val question = questions!![position]
            holder.binding(question)
        }

        override fun getItemCount(): Int {
            return questions!!.size
        }


    }
}