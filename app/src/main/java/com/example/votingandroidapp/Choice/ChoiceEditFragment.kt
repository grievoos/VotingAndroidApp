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

class ChoiceEditFragment: Fragment() {
    companion object{
        fun newInstance(choiceId: Int?) =
            ChoiceEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("choiceKey", choiceId)
                }
            }
    }
    private val httpClient = OkHttpClient()
    private var choiceId = 0
    private var choice: Choice? = null
    private var questionsList = mutableListOf<Question>()
    private var usersList = mutableListOf<Users>()
    private lateinit var questionSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var userChoiceField: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        choiceId = (requireActivity().intent.getSerializableExtra("choiceKey") as Int?)!!
        val request = Request.Builder().url("http://192.168.0.104:5000/Choice/${choiceId}").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                choice = Choice(jsonItem.getInt("id"), jsonItem.getInt("question_id"),
                    jsonItem.getInt("user_id"), jsonItem.getString("user_choice"),
                    jsonItem.getString("content"), jsonItem.getString("fullname"),)
            }
        })
        val questionRequest = Request.Builder().url("http://192.168.0.104:5000/Question").build()
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
        val postRequest = Request.Builder().url("http://192.168.0.104:5000/Users").build()
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
        val view = inflater.inflate(R.layout.choice_edit_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        questionSpinner = view.findViewById(R.id.spinnerQuestion)
        userSpinner = view.findViewById(R.id.spinnerUsers)

        questionSpinner.adapter = QuestionAdapter(requireContext(), questionsList)
        questionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                choice?.questionId = questionsList[position].id
                choice?.content = questionsList[position].content
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
                choice?.userId = usersList[position].id
                choice?.fullname = usersList[position].firstName + " " + usersList[position].lastName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        questionSpinner.setSelection(questionsList.indexOfFirst { it.id == choice?.questionId })
        userSpinner.setSelection(usersList.indexOfFirst { it.id == choice?.userId })

        editBtn.setOnClickListener{
            if (userChoiceField.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("question_id", choice?.questionId.toString())
                    .addFormDataPart("user_id", choice?.userChoice.toString())
                    .addFormDataPart("user_choice", userChoiceField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Choice/${choiceId}").put(body).build()

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
            val request = Request.Builder().url("http://192.168.3.60:5000/Choice/${choiceId}").delete().build()
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

        userChoiceField = view.findViewById(R.id.userChoice)
        userChoiceField.setText(choice?.userChoice)
        return view
    }

    fun backward(){
        val intent = ChoiceListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}