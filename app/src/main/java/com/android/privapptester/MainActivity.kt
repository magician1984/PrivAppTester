package com.android.privapptester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.android.privapptester.pattern.Model
import com.android.privapptester.pattern.View

class MainActivity : ComponentActivity() {
    private val model: Model = Model()

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
}