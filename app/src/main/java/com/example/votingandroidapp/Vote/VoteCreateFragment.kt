package com.example.votingandroidapp.Vote

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class VoteCreateFragment: Fragment() {
    companion object{
        fun newInstance() = VoteCreateFragment()
    }

    private val okHttpClient = OkHttpClient()
    private val today = Calendar.getInstance()
    private var dateStart = "None"
    private var dateFinish = "None"
    private lateinit var title: EditText
    private lateinit var status: EditText
    private lateinit var startDatePicker: DatePicker
    private lateinit var finishDatePicker: DatePicker
    private lateinit var addBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vote_create_fragment, container, false)
        addBtn = view.findViewById(R.id.addBtn)
        backBtn = view.findViewById(R.id.backBtn)
        title = view.findViewById(R.id.title)
        status = view.findViewById(R.id.status)
        startDatePicker = view.findViewById(R.id.startDatePicker)
        finishDatePicker = view.findViewById(R.id.finishDatePicker)

        startDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, monthOfYear, dayOfMonth ->
            dateStart = "${year}.${monthOfYear}.${dayOfMonth}"
        }

        finishDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, monthOfYear, dayOfMonth ->
            dateFinish = "${year}.${monthOfYear}.${dayOfMonth}"
        }


        addBtn.setOnClickListener{
            if (title.text.toString().isEmpty() && status.text.toString().isEmpty()
                && dateStart == "None" && dateFinish == "None"){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("title", title.text.toString())
                    .addFormDataPart("start_date", dateStart)
                    .addFormDataPart("finish_date", dateFinish)
                    .addFormDataPart("status", status.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Vote").post(body).build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(context, "Небольшие технические шоколадки Т_Т", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        backward()
                    }

                })
            }
        }

        backBtn.setOnClickListener{ backward() }
        return view
    }

    fun backward(){
        val intent = VoteListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}