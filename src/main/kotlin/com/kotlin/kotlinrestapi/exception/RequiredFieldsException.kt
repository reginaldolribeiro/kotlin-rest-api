package com.kotlin.kotlinrestapi.exception

class RequiredFieldsException(message: String? = null) : Exception(message) {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}