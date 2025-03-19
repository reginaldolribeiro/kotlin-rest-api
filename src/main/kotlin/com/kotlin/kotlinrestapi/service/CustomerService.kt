package com.kotlin.kotlinrestapi.service

import com.kotlin.kotlinrestapi.exception.CustomerNotFoundException
import com.kotlin.kotlinrestapi.exception.RequiredFieldsException
import com.kotlin.kotlinrestapi.model.Customer
import com.kotlin.kotlinrestapi.repository.CustomerRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    fun findById(id: Long): Customer {
        return this.customerRepository
            .findById(id)
            .orElseThrow { CustomerNotFoundException("Customer not found!") }
    }

    fun findAll(): List<Customer> {
        return customerRepository.findAll()
    }

    fun save(customer: Customer): Customer {
        return try{
            customerRepository.save(customer)
        }catch (e: DataIntegrityViolationException){
            throw DataIntegrityViolationException("Email already exists!")
        }
    }

    fun update(id: Long, customer: Customer): Customer {
        if(customer.name.isBlank() || customer.email.isBlank()) throw RequiredFieldsException("Required fields missing!")

        val customerToSave = findById(id)
            .copy(name = customer.name, email = customer.email)
        return customerRepository.save(customerToSave)
    }

    fun delete(id: Long): Unit {
        customerRepository.delete(findById(id))
    }
}