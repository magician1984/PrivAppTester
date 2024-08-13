package com.android.privapptester.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

interface IPresenter {
    @Composable
    fun showUI()

    interface IAction

    interface IState

    interface IEffect

    interface IModel<A : IAction, S : IState, E : IEffect>{
        val state: State<S>

        val effect: State<E>

        fun actionDispatcher(action: A)
    }

    interface IView<A : IAction, S : IState, E : IEffect>{
        val content : @Composable () -> Unit

        fun attach(state:State<S>, effect:State<E>, actionDispatcher: (A) -> Unit)
    }
}