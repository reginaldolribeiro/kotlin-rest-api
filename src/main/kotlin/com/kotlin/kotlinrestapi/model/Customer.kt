package com.kotlin.kotlinrestapi.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
data class Customer(
    @Id
    @GeneratedValue
    val id: Long,
    @Column(name = "name")
    @NotBlank(message = "Name is required")
    @NotNull
    @NotEmpty
    val name: String,
    @Column(unique = true)
    val email: String
)