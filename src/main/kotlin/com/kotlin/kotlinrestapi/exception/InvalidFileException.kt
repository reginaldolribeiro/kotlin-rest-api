package com.kotlin.kotlinrestapi.exception

class InvalidFileException(message: String? = null) : Exception(message) {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}