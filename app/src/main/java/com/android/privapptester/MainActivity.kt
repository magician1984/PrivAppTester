package com.android.privapptester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.android.privapptester.core.IRenderer
import com.android.privapptester.presenter.Model
import com.android.privapptester.presenter.View
import com.android.privapptester.usecase.OpenFileTestUseCase

class MainActivity : ComponentActivity(), IRenderer {
    private val model: Model = Model(OpenFileTestUseCase())

    private val view: View = View(
        onBackPressedDispatcher = onBackPressedDispatcher,
        messages = model.messages,
        userIntentHandler = model::handleUserIntent,
        renderer = this
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        view.show()
    }

    override fun draw(content: @Composable () -> Unit) = setContent(null, content)
}