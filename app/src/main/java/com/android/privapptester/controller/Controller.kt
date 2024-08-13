package com.android.privapptester.controller

import com.android.privapptester.controller.module.ControlEventRepository
import com.android.privapptester.controller.module.FileController
import com.android.privapptester.core.IController
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class Controller : IController {
    private val modules: MutableMap<KClass<out IController.Module>, IController.Module> =
        mutableMapOf()

    init {
        modules[IController.IControlEventRepository::class] = ControlEventRepository()
        modules[IController.IFileController::class] = FileController()
    }

    @Throws(IController.ModuleNotCreateException::class)
    override fun <T : IController.Module> getModule(clazz: KClass<out T>): T =
        clazz.safeCast(modules[clazz]) ?: throw IController.ModuleNotCreateException(clazz)

    override fun close() = modules.forEach { it.value.close() }
}