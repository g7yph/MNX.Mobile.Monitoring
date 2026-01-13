package com.minux.monitoring.feature.auth.impl.retrofit

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class FlowResultCallAdapterFactory private constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) return null
        check(returnType is ParameterizedType) {
            "Flow return type must be parameterized as Flow<T> or Flow<out T>"
        }

        val resultType = getParameterUpperBound(0, returnType)
        if (getRawType(resultType) != Result::class.java) return null
        check(resultType is ParameterizedType) {
            "Result must be parameterized as Result<T> or Result<out T>"
        }

        val responseType = getParameterUpperBound(0, resultType)
        return FlowResultCallAdapter<Any>(responseType = responseType)
    }

    companion object {
        @JvmStatic
        fun create() = FlowResultCallAdapterFactory()
    }
}