package com.example.campus.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus.util.Response
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class geminiViewModel: ViewModel() {

    private val genAI by lazy{
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey="AIzaSyCxIbvTIzsOdW2BuSZSEub6mm6KZD4tDXw"
        )
    }

    private val _response = MutableLiveData<Response<String>>()
    val response: LiveData<Response<String>> get() = _response
    fun sendMessage(message: String) = viewModelScope.launch {
        _response.value = Response.Loading
        try {
            val responseText = genAI.startChat().sendMessage(message).text

            _response.postValue(Response.Success(responseText!!))
        } catch (e: Exception) {
            _response.postValue(Response.Error(e.message ?: "An unknown error occurred"))
        }
    }
}