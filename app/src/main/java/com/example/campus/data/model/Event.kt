package com.example.campus.data.model

import java.util.Date

data class Event(
    val id:String,
    val username:String,
    val title:String,
    val price:String,
    val image:String,
    val description:String,
    val date:Date,
    val location:String
)
