package com.messages.abdallah.mymessages.models

import androidx.room.Embedded

data class MsgModelWithTitle(
    @Embedded
    var msgModel: MsgsModel? = null,
    val typeTitle: String = ""

)
