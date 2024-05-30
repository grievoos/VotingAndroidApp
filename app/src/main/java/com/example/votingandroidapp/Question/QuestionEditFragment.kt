package com.example.votingandroidapp.Question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.R
import com.example.votingandroidapp.Vote.Vote
import com.example.votingandroidapp.Vote.VoteAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class QuestionEditFragment: Fragment() {
    companion object{
        fun newInstance(questionId: Int?) =
            QuestionEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("questionKey", questionId)
                }
            }
    }
    private val httpClient = OkHttpClient()
    private var questionId = 0
    private var question: Question? = null
    private var voteList = mutableListOf<Vote>()
    private var dateVote = "None"
    private lateinit var voteField: Spinner
    private lateinit var contentField: EditText
    private lateinit var voteDatePicker: DatePicker
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        questionId = (requireActivity().intent.getSerializableExtra("questionKey") as Int?)!!
        val request = Request.Builder().url("http://192.168.0.104:5000/Question/${questionId}").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                question = Question(jsonItem.getInt("id"), jsonItem.getInt("vote_id"),
                    jsonItem.getString("content"), jsonItem.getString("vote_date"),
                    jsonItem.getString("title"))
            }
        })
        val voteRequest = Request.Builder().url("http://192.168.0.104:5000/Vote").build()
        httpClient.newCall(voteRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                voteList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    voteList.add(Vote(step.getInt("id"), step.getString("title"),
                        step.getString("start_date"), step.getString("finish_date"),
                        step.getString("status")))
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.question_edit_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        voteField = view.findViewById(R.id.spinner)
        voteDatePicker = view.findViewById(R.id.voteDatePicker)

        voteField.adapter = VoteAdapter(requireContext(), voteList)
        voteField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                question?.voteId = voteList[position].id
                question?.title = voteList[position].title
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        voteField.setSelection(voteList.indexOfFirst { it.id == question?.voteId })

        editBtn.setOnClickListener{
            if (contentField.text.toString().isEmpty() && dateVote == "None"){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("vote_id", question?.voteId.toString())
                    .addFormDataPart("content", contentField.text.toString())
                    .addFormDataPart("vote_date", dateVote)
                    .build()
                val request = Request.Builder().url("http://192.168.0.104:5000/Question/${questionId}").put(body).build()

                httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(context, "Небольшие технические шоколадки Т_Т", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call, response: Response) {
                        backward()
                    }

                })
            }
        }

        deleteBtn.setOnClickListener{
            val request = Request.Builder().url("http://192.168.3.60:5000/Question/${questionId}").delete().build()
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    backward()
                }
            })
        }

        backBtn.setOnClickListener{
            backward()
        }

        val voteDateList = question?.voteDate?.split(".")

        voteDatePicker.init(voteDateList!![0].toInt(), voteDateList[1].toInt(),
            voteDateList[2].toInt()){ view, year, monthOfYear, dayOfMonth ->
            dateVote = "${year}.${monthOfYear}.${dayOfMonth}"
        }


        contentField = view.findViewById(R.id.content)
        contentField.setText(question?.content)
        return view
    }

    fun backward(){
        val intent = QuestionListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}