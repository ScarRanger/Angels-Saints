package com.rhinepereira.saints.data.model

sealed class UiError {
    object Network : UiError()
    object NotFound : UiError()
    data class Unknown(val message: String?) : UiError()
}
