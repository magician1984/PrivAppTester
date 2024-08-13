package com.android.privapptester.data

import com.android.privapptester.core.IData

data class Result(
    override val time: Long,
    override val normal: Boolean,
    override val content: String
) : IData {
    override val type: Int
        get() = IData.TYPE_RESULT

    override fun toString(): String =
        "Result(time=$time, normal=$normal, content='$content', type=$type)"
}