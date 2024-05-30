package com.example.votingandroidapp.Vote

class Vote {
    var id: Int
    var title: String
    var startDate: String
    var finishDate: String
    var status: String

    constructor(id: Int, title: String, startDate: String, finishDate: String, status: String){
        this.id = id
        this.title = title
        this.startDate = startDate
        this.finishDate = finishDate
        this.status = status
    }
}