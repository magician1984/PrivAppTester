package com.android.privapptester.pattern

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.privapptester.data.Message
import com.android.privapptester.usecase.IOpenFileTestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class Model(private val openFileTestUseCase: IOpenFileTestUseCase) : CoroutineScope {
    companion object {
        private const val ROOT_FOLDER = "/data/logs"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val _messages: SnapshotStateList<Message> = SnapshotStateList()

    val messages: List<Message>
        get() = _messages

    fun handleUserIntent(intent: UserIntent) {
        launch {
            when (intent) {
                is UserIntent.OpenFileTest -> openFileTestUseCase.invoke(ROOT_FOLDER).collect{
                    update(it)
                }
            }
        }
    }

    private fun update(message: Message) {
        _messages.add(message)
    }
}