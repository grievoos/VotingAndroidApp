package com.example.votingandroidapp.Vote

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class VoteEditFragment: Fragment() {
    companion object{
        fun newInstance(voteId: Int?) =
            VoteEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("voteKey", voteId)
                }
            }
    }
    private val httpClient = OkHttpClient()
    private var voteId = 0
    private var vote: Vote? = null
    private var dateStart = "None"
    private var dateFinish = "None"
    private lateinit var title: EditText
    private lateinit var startDatePicker: DatePicker
    private lateinit var finishDatePicker: DatePicker
    private lateinit var status: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voteId = (requireActivity().intent.getSerializableExtra("voteKey") as Int?)!!
        val request = Request.Builder().url("http://192.168.0.104:5000/Vote/${voteId}").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                vote = Vote(jsonItem.getInt("id"), jsonItem.getString("title"),
                    jsonItem.getString("start_date"), jsonItem.getString("finish_date"),
                    jsonItem.getString("status"))
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vote_edit_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        startDatePicker = view.findViewById(R.id.startDatePicker)
        finishDatePicker = view.findViewById(R.id.finishDatePicker)

        editBtn.setOnClickListener{
            if (title.text.toString().isEmpty() && status.text.toString().isEmpty() &&
                dateStart == "None" && dateFinish == "None"){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("title", title.text.toString())
                    .addFormDataPart("start_date", dateStart)
                    .addFormDataPart("finish_date", dateFinish)
                    .addFormDataPart("status", status.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Vote/${voteId}").put(body).build()

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
            val request = Request.Builder().url("http://192.168.3.60:5000/Vote/${voteId}").delete().build()
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

        val startDateList = vote?.startDate?.split(".")
        val finishDateList = vote?.finishDate?.split(".")

        startDatePicker.init(startDateList!![0].toInt(), startDateList[1].toInt(),
            startDateList[2].toInt()){ view, year, monthOfYear, dayOfMonth ->
            dateStart = "${year}.${monthOfYear}.${dayOfMonth}"
        }

        finishDatePicker.init(finishDateList!![0].toInt(), finishDateList[1].toInt(),
            finishDateList[2].toInt()){ view, year, monthOfYear, dayOfMonth ->
            dateFinish = "${year}.${monthOfYear}.${dayOfMonth}"
        }

        title = view.findViewById(R.id.title)
        title.setText(vote?.title)
        status = view.findViewById(R.id.status)
        status.setText(vote?.status)
        return view
    }

    fun backward(){
        val intent = VoteListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}