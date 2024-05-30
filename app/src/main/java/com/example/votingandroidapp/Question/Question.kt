package com.example.votingandroidapp.Question

class Question {
    var id: Int
    var voteId: Int
    var content: String
    var voteDate: String
    var title: String

    constructor(id: Int, voteId: Int, content: String, voteDate: String, title: String){
        this.id = id
        this.voteId = voteId
        this.content = content
        this.voteDate = voteDate
        this.title = title
    }
}