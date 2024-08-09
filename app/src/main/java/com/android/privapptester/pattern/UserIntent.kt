package com.android.privapptester.pattern

sealed class UserIntent {
    data object OpenFileTest : UserIntent()
}