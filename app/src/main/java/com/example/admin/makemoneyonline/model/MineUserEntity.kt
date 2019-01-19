package com.example.admin.makemoneyonline.model

class MineUserEntity {

    data class MineUserInfo(
            val status: Boolean,
            val success: SuccessBean
    )

    data class SuccessBean(
            val code: Int,
            val staff: ArrayList<StaffBean> = arrayListOf<StaffBean>()
    )

    data class StaffBean(
            val id: Int,
            val name: String
    )
}