package com.android.privapptester.core

interface IData {
    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_RESULT = 1
    }

    val time: Long
    val type: Int
    val normal: Boolean
    val content: String

    override fun toString(): String
}