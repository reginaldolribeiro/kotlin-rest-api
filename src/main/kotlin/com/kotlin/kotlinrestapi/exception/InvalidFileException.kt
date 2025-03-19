package com.kotlin.kotlinrestapi.exception

class InvalidFileException(message: String? = null) : RuntimeException(message) {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}