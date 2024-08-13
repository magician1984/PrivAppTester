package com.android.privapptester.core

import android.database.Observable
import java.io.Closeable
import kotlin.jvm.Throws
import kotlin.reflect.KClass

interface IController : Closeable {
    interface Module : Closeable

    @Throws(ModuleNotCreateException::class)
    fun <T : Module> getModule(clazz: KClass<out T>): T

    class ModuleNotCreateException(clazz: KClass<out Module>) :
        Exception("Module [${clazz.simpleName}] not create")

    interface IControlEventRepository : Module {
        data class ControlEvent(val time: Long, val isSucceed: Boolean, val content: String)

        fun pushEvent(event: ControlEvent)
        fun getEventList() : List<ControlEvent>
        fun subscribeEvent(handler: (ControlEvent) -> Unit)
    }

    interface IFileController : Module {
        data class FileInfo(val name: String, val readable: Boolean)

        fun listAllFiles(path: String, recursive: Boolean): List<FileInfo>
    }
}