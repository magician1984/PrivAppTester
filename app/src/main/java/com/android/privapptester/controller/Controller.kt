package com.android.privapptester.controller

import com.android.privapptester.controller.module.ControlEventRepository
import com.android.privapptester.controller.module.FileController
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class Controller : com.android.privapptester.core.IController {
    private val modules: MutableMap<KClass<out com.android.privapptester.core.IController.Module>, com.android.privapptester.core.IController.Module> =
        mutableMapOf()

    init {
        modules[com.android.privapptester.core.IController.IControlEventRepository::class] = ControlEventRepository()
        modules[com.android.privapptester.core.IController.IFileController::class] = FileController()
    }

    @Throws(com.android.privapptester.core.IController.ModuleNotCreateException::class)
    override fun <T : com.android.privapptester.core.IController.Module> getModule(clazz: KClass<out T>): T =
        clazz.safeCast(modules[clazz]) ?: throw com.android.privapptester.core.IController.ModuleNotCreateException(clazz)

    override fun close() = modules.forEach { it.value.close() }
}