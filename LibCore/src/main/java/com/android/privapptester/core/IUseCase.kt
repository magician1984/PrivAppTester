package com.android.privapptester.core

import kotlinx.coroutines.flow.Flow

interface IUseCase<I, O> {
    operator fun invoke(input: I): Flow<O>

    interface IUseCaseNoInput<O> : IUseCase<Unit, O> {
        operator fun invoke(): Flow<O> = invoke(Unit)
    }

    interface IUseCaseNoIO : IUseCaseNoInput<Unit>
}