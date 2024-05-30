package com.example.votingandroidapp.Users

class Users {
    var id: Int
    var firstName: String
    var lastName: String
    var email: String
    var phone: String
    var status: String

    constructor(id: Int, firstName: String, lastName: String, email: String, phone: String, status: String){
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phone = phone
        this.status = status
    }
}