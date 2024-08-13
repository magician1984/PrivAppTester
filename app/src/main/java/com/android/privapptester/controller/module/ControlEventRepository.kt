package com.android.privapptester.controller.module

import com.android.privapptester.core.IController.IControlEventRepository


class ControlEventRepository : IControlEventRepository {
    companion object {
        const val TAG = "ControlEventRepository"
    }

    private val events: MutableList<IControlEventRepository.ControlEvent> = mutableListOf()

    private var observer: ((IControlEventRepository.ControlEvent) -> Unit)? = null

    override fun pushEvent(event: IControlEventRepository.ControlEvent) {
        events.add(event)
        observer?.invoke(event)
    }

    override fun getEventList(): List<IControlEventRepository.ControlEvent> = events

    override fun subscribeEvent(
        handler: (IControlEventRepository.ControlEvent) -> Unit
    ) {
        this.observer = handler
    }


    override fun close() {
        events.clear()
    }
}