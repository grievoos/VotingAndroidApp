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
import org.json.JSONObject
import java.io.IOException

class UsersEditFragment: Fragment() {
    companion object{
        fun newInstance(userId: Int?) =
            UsersEditFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("usersKey", userId)
                }
            }
    }
    private val httpClient = OkHttpClient()
    private var userId = 0
    private var user: Users? = null
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var phoneField: EditText
    private lateinit var statusField: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = (requireActivity().intent.getSerializableExtra("usersKey") as Int?)!!
        val request = Request.Builder().url("http://192.168.0.104:5000/Users/${userId}").build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                user = Users(jsonItem.getInt("id"), jsonItem.getString("first_name"),
                    jsonItem.getString("last_name"), jsonItem.getString("email"),
                    jsonItem.getString("phone"), jsonItem.getString("status"),)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.users_edit_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)

        editBtn.setOnClickListener{
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
                val request = Request.Builder().url("http://192.168.3.60:5000/Users/${userId}").put(body).build()

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
            val request = Request.Builder().url("http://192.168.3.60:5000/Users/${userId}").delete().build()
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

        firstNameField = view.findViewById(R.id.firstName)
        firstNameField.setText(user?.firstName)
        lastNameField = view.findViewById(R.id.lastName)
        lastNameField.setText(user?.lastName)
        emailField = view.findViewById(R.id.email)
        emailField.setText(user?.email)
        phoneField = view.findViewById(R.id.phone)
        phoneField.setText(user?.phone)
        statusField = view.findViewById(R.id.status)
        statusField.setText(user?.status)
        return view
    }

    fun backward(){
        val intent = UsersListActivity.newIntent(context)
        context?.startActivity(intent)
    }
}