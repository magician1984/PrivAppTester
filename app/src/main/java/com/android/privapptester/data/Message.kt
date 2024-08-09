package com.android.privapptester.data

data class Message(
    val time: Long,
    val type: Int,
    val content: String,
    val succeed: Boolean = true
) {
    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_RESULT = 1
    }

    override fun toString(): String =
        "Message(time=$time, type=$type, content='$content', succeed=$succeed)"
}
