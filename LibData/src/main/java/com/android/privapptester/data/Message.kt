package com.android.privapptester.data

import com.android.privapptester.core.IData

data class Message(
    override val time: Long,
    override val normal: Boolean,
    override val content: String
) : IData {
    override val type: Int
        get() = IData.TYPE_MESSAGE

    override fun toString(): String =
        "Message(time=$time, normal=$normal, content='$content', type=$type)"
}