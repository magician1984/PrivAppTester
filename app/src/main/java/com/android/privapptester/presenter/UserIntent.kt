package com.android.privapptester.presenter

sealed class UserIntent {
    data object OpenFileTest : UserIntent()
}