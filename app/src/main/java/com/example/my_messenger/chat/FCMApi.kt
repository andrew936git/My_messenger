package com.example.my_messenger.chat


import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer YOUR_SERVER_KEY"
    )
    @POST("https://fcm.googleapis.com/v1/projects/YOUR_PROJECT_ID/messages:send")
    suspend fun sendNotification(@Body body: FCMMessage): Response<ResponseBody>
}

data class FCMMessage(
    val message: MessageData
)

data class MessageData(
    val notification: NotificationData,
    val token: String
)

data class NotificationData(
    val title: String,
    val body: String
)

