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

        fun generateMessage(content: String, succeed: Boolean) =
            Message(System.currentTimeMillis(), TYPE_MESSAGE, content, succeed)

        fun generateResult(content: String, succeed: Boolean) =
            Message(System.currentTimeMillis(), TYPE_RESULT, content, succeed)
    }

    override fun toString(): String =
        "Message(time=$time, type=$type, content='$content', succeed=$succeed)"
}
