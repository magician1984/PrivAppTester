package com.android.privapptester.usecase

import com.android.privapptester.core.IData
import com.android.privapptester.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException

class OpenFileTestUseCase : IOpenFileTestUseCase {
    private data class Params(val file: File, val handle: (File) -> Unit)

    private val fileTravelFunc = DeepRecursiveFunction<Params, Unit> { it ->
        val file: File = it.file
        val handle: (File) -> Unit = it.handle
        if (file.isDirectory) {
            val files = file.listFiles()

            files?.forEach { child ->
                callRecursive(Params(child, handle))
            }
        } else {
            handle(file)
        }
    }

    override fun invoke(input: String): Flow<IData> = channelFlow {
        val root: File = File(input)

        if (!root.exists())
            throw FileNotFoundException("root folder not exists")

        travelFiles(root) {
            val filename = it.name
            val result: Boolean = try {
                val inputStream = it.inputStream()
                inputStream.available()
                true
            } catch (e: Exception) {
                false
            }
            runBlocking {
                send(Result(System.currentTimeMillis(), result, filename))
            }
        }
    }

    private fun travelFiles(file: File, handle: (File) -> Unit) {
        fileTravelFunc.invoke(Params(file, handle))
    }
}