package com.android.privapptester

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.privapptester.controller.Controller
import com.android.privapptester.controller.module.ControlEventRepository
import com.android.privapptester.core.IController
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class ControllerTest {

    interface MockModule : IController.Module

    fun interface SubscribeInterface{
        fun onEvent(event: IController.IControlEventRepository.ControlEvent)
    }

    @Test(expected = IController.ModuleNotCreateException::class)
    fun controllerInitTest() {
        val controller = Controller()

        val eventModule : IController.Module = controller.getModule(IController.IControlEventRepository::class)

        assert(eventModule is IController.IControlEventRepository)

        val fileModule : IController.Module = controller.getModule(IController.IFileController::class)

        assert(fileModule is IController.IFileController)

        controller.getModule(MockModule::class)
    }

    @Test
    fun controlEventRepositoryTest() {
        val module  = ControlEventRepository()

        val handler = mock<SubscribeInterface>()

        val event = IController.IControlEventRepository.ControlEvent(System.currentTimeMillis(), true, "Test")

        repeat(10){
            module.pushEvent(event)
        }

        assert(module.getEventList().size == 10)

        module.subscribeEvent(handler::onEvent)

        module.pushEvent(event)

        verify(handler).onEvent(event)
    }
}