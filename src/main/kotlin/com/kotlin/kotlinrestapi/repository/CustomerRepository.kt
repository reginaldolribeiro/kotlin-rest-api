package com.kotlin.kotlinrestapi.repository

import com.kotlin.kotlinrestapi.model.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long>{
}