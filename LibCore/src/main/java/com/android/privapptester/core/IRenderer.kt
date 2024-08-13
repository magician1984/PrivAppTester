package com.android.privapptester.core

import androidx.compose.runtime.Composable

interface IRenderer {
    fun draw(content : @Composable ()->Unit)
}