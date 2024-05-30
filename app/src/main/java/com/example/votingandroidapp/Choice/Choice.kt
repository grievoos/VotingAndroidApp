package com.example.votingandroidapp.Choice

class Choice {
    var id: Int
    var questionId: Int
    var userId: Int
    var userChoice: String
    var content: String
    var fullname: String

    constructor(id: Int, questionId: Int, userId: Int, userChoice: String, content: String, fullname: String){
        this.id = id
        this.questionId = questionId
        this.userId = userId
        this.userChoice = userChoice
        this.content = content
        this.fullname = fullname
    }
}