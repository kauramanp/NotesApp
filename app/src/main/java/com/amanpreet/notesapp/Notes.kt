package com.amanpreet.notesapp

import com.google.firebase.Timestamp

data class Notes(
    var id: String?= null,
    var title: String?= null,
    var description: String?= null,
    var timestamp: Timestamp?= null,
    var priority: Int?= 0,
    var userId: String?= null,
)
