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
import java.util.Calendar

class QuestionCreateFragment: Fragment() {
    companion object{
        fun newInstance() = QuestionCreateFragment()
    }
    private val httpClient = OkHttpClient()
    private var voteList = mutableListOf<Vote>()
    private var voteId = 0
    private val today = Calendar.getInstance()
    private var dateVote = "None"
    private lateinit var voteField: Spinner
    private lateinit var contentField: EditText
    private lateinit var voteDatePicker: DatePicker
    private lateinit var createBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val view = inflater.inflate(R.layout.question_create_fragment, container, false)
        createBtn = view.findViewById(R.id.createBtn)
        backBtn = view.findViewById(R.id.backBtn)
        voteField = view.findViewById(R.id.spinner)
        contentField = view.findViewById(R.id.content)
        voteDatePicker = view.findViewById(R.id.voteDatePicker)

        voteDatePicker.init(today.get(android.icu.util.Calendar.YEAR), today.get(android.icu.util.Calendar.MONTH), today.get(
            android.icu.util.Calendar.DAY_OF_MONTH)) { view, year, monthOfYear, dayOfMonth ->
            dateVote = "${year}.${monthOfYear}.${dayOfMonth}"
        }

        voteField.adapter = VoteAdapter(requireContext(), voteList)
        voteField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                voteId = voteList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        createBtn.setOnClickListener{
            if (voteId.toString().isEmpty() && contentField.text.toString().isEmpty() && dateVote == "None"){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("vote_id", voteId.toString())
                    .addFormDataPart("content", contentField.text.toString())
                    .addFormDataPart("vote_date", dateVote)
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Question").post(body).build()

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

        backBtn.setOnClickListener{
            backward()
        }

        return view
    }

    fun backward(){
        val intent = QuestionListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}