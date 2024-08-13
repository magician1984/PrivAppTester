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

    interface MockModule : com.android.privapptester.core.IController.Module

    fun interface SubscribeInterface{
        fun onEvent(event: com.android.privapptester.core.IController.IControlEventRepository.ControlEvent)
    }

    @Test(expected = com.android.privapptester.core.IController.ModuleNotCreateException::class)
    fun controllerInitTest() {
        val controller = Controller()

        val eventModule : com.android.privapptester.core.IController.Module = controller.getModule(
            com.android.privapptester.core.IController.IControlEventRepository::class)

        assert(eventModule is com.android.privapptester.core.IController.IControlEventRepository)

        val fileModule : com.android.privapptester.core.IController.Module = controller.getModule(
            com.android.privapptester.core.IController.IFileController::class)

        assert(fileModule is com.android.privapptester.core.IController.IFileController)

        controller.getModule(MockModule::class)
    }

    @Test
    fun controlEventRepositoryTest() {
        val module  = ControlEventRepository()

        val handler = mock<SubscribeInterface>()

        val event = com.android.privapptester.core.IController.IControlEventRepository.ControlEvent(System.currentTimeMillis(), true, "Test")

        repeat(10){
            module.pushEvent(event)
        }

        assert(module.getEventList().size == 10)

        module.subscribeEvent(handler::onEvent)

        module.pushEvent(event)

        verify(handler).onEvent(event)
    }
}