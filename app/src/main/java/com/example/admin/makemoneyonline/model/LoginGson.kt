package com.example.admin.makemoneyonline.model

class LoginGson {

    data class LoginGson(
            val status: Boolean,
            val success: SuccessBean
    )

    data class SuccessBean(
            val code: Int,
            val token: String,
            val id: Int,
            val username: String,
            val name: String,
            val email: String,
            val avatar: String
    )
}