package com.example.votingandroidapp.Choice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.votingandroidapp.Question.Question
import com.example.votingandroidapp.Question.QuestionAdapter
import com.example.votingandroidapp.R
import com.example.votingandroidapp.Users.Users
import com.example.votingandroidapp.Users.UsersAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChoiceCreateFragment: Fragment() {
    companion object{
        fun newInstance() = ChoiceCreateFragment()
    }
    private val httpClient = OkHttpClient()
    private var questionsList = mutableListOf<Question>()
    private var usersList = mutableListOf<Users>()
    private var questionId = 0
    private var userId = 0
    private lateinit var questionSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var userChoiceField: EditText
    private lateinit var createBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val questionRequest = Request.Builder().url("http://192.168.3.60:5000/Question").build()
        httpClient.newCall(questionRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
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
        val postRequest = Request.Builder().url("http://192.168.3.60:5000/Users").build()
        httpClient.newCall(postRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.choice_create_fragment, container, false)
        createBtn = view.findViewById(R.id.createBtn)
        backBtn = view.findViewById(R.id.backBtn)
        questionSpinner = view.findViewById(R.id.spinnerQuestion)
        userSpinner = view.findViewById(R.id.spinnerUsers)
        userChoiceField = view.findViewById(R.id.userChoice)

        questionSpinner.adapter = QuestionAdapter(requireContext(), questionsList)
        questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                questionId = questionsList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        userSpinner.adapter = UsersAdapter(requireContext(), usersList)
        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                userId = usersList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        createBtn.setOnClickListener{
            if (questionId.toString().isEmpty() && userId.toString().isEmpty() &&
                userChoiceField.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("question_id", questionId.toString())
                    .addFormDataPart("user_id", userId.toString())
                    .addFormDataPart("user_choice", userChoiceField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Choice").post(body).build()

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
        val intent = ChoiceListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}