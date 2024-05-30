package com.example.votingandroidapp.Users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class UsersCreateFragment: Fragment() {
    companion object{
        fun newInstance() = UsersCreateFragment()
    }

    private val httpClient = OkHttpClient()
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var phoneField: EditText
    private lateinit var statusField: EditText
    private lateinit var createBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.users_create_fragment, container, false)
        createBtn = view.findViewById(R.id.createBtn)
        backBtn = view.findViewById(R.id.backBtn)
        firstNameField = view.findViewById(R.id.firstName)
        lastNameField = view.findViewById(R.id.lastName)
        emailField = view.findViewById(R.id.email)
        phoneField = view.findViewById(R.id.phone)
        statusField = view.findViewById(R.id.status)

        createBtn.setOnClickListener{
            if (firstNameField.text.toString().isEmpty() && lastNameField.text.toString().isEmpty()
                && emailField.text.toString().isEmpty() && phoneField.text.toString().isEmpty()
                && statusField.text.toString().isEmpty()){
                Toast.makeText(context, "Заполните все поля!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("first_name", firstNameField.text.toString())
                    .addFormDataPart("last_name", lastNameField.text.toString())
                    .addFormDataPart("email", emailField.text.toString())
                    .addFormDataPart("phone", phoneField.text.toString())
                    .addFormDataPart("status", statusField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.60:5000/Users").post(body).build()

                httpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}

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
        val intent = UsersListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}