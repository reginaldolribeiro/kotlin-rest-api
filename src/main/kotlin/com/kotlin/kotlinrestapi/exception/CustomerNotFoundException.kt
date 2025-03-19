package com.kotlin.kotlinrestapi.exception

class CustomerNotFoundException(message: String? = null) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}