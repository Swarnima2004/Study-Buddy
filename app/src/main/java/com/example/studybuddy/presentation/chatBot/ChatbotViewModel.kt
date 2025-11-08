package com.example.studybuddy.presentation.chatBot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studybuddy.util.Constants

import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@HiltViewModel
class ChatbotViewModel @Inject constructor() : ViewModel() {



    val ApiKey = Constants.API_KEY
    private val client = OkHttpClient()
    private val _response = MutableStateFlow("Ask something...")
    val response: StateFlow<String> = _response

    fun sendMessage(question : String){
        viewModelScope.launch {

            try {
                val responseText = withContext(Dispatchers.IO) {
                    val url = URL("https://api.openai.com/v1/chat/completions")
                    val connection = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "POST"
                        setRequestProperty("Content-Type", "application/json")
                        setRequestProperty("Authorization", "Bearer $ApiKey")
                        doOutput = true
                    }

                    val jsonRequest = JSONObject().apply {
                        put("model", "gpt-4o-mini") // safer model for normal keys
                        put("messages", JSONArray().apply {
                            put(JSONObject().apply {
                                put("role", "user")
                                put("content", question)
                            })
                        })
                    }

                    // Send JSON
                    OutputStreamWriter(connection.outputStream).use {
                        it.write(jsonRequest.toString())
                    }

                    val responseCode = connection.responseCode
                    val inputStream = if (responseCode in 200..299) {
                        connection.inputStream
                    } else {
                        connection.errorStream
                    }

                    val result = BufferedReader(inputStream.reader()).use { it.readText() }
                    connection.disconnect()
                    result
                }

                // Parse JSON safely
                val json = JSONObject(responseText)

                if (json.has("choices")) {
                    val content = json
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")

                    _response.value = content.trim()
                } else if (json.has("error")) {
                    val errorMessage = json.getJSONObject("error").optString("message", "Unknown API error")
                    _response.value = "API Error: $errorMessage"
                } else {
                    _response.value = "Unexpected response format: $responseText"
                }

            } catch (e: Exception) {
                Log.e("ChatbotViewModel", "Error: ${e.localizedMessage}", e)
                _response.value = "Error: ${e.localizedMessage}"
            }

    }
}}