package com.android.privapptester.pattern

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.privapptester.data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class Model : CoroutineScope {
    companion object {
        private const val ROOT_FOLDER = "/data/logs"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    private val _messages: SnapshotStateList<Message> = SnapshotStateList()

    val messages:List<Message>
        get() = _messages

    fun handleUserIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.OpenFileTest -> startTest()
        }
    }

    private fun startTest() {
        launch {
            message("Start open file test")

            // Try to open files
            val root = File(ROOT_FOLDER)

            if (!root.exists())
                message("Root folder not exists").also { return@launch }

            travelFiles(root) {
                try {
                    val inputStream = it.inputStream()
                    inputStream.available()
                    result(it.name, true)
                } catch (e: Exception) {
                    result(it.name, false)
                }
            }
        }
    }

    private fun travelFiles(file: File, handle: (File) -> Unit) {
        if (file.isDirectory) {
            val files = file.listFiles()

            files?.forEach {
                travelFiles(it, handle)
            }
        } else {
            handle(file)
        }
    }

    private fun result(message: String, succeed: Boolean) =
        update(Message(System.currentTimeMillis(), Message.TYPE_RESULT, message, succeed))

    private fun message(message: String) =
        update(Message(System.currentTimeMillis(), Message.TYPE_MESSAGE, message))

    private fun update(message: Message) {
        _messages.add(message)
    }
}