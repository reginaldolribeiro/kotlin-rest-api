package com.kotlin.kotlinrestapi.controller

import com.kotlin.kotlinrestapi.model.Customer
import com.kotlin.kotlinrestapi.model.UploadResponse
import com.kotlin.kotlinrestapi.service.CustomerService
import com.kotlin.kotlinrestapi.service.StorageService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("api")
class CustomerController(
    private val customerService: CustomerService,
    private val storageService: StorageService
) {

    @GetMapping
    fun findAll() = ResponseEntity.ok(this.customerService.findAll())

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long) =
        ResponseEntity.ok(this.customerService.findById(id))

    @PostMapping
    fun save(@Valid @RequestBody customer: Customer) =
        ResponseEntity.ok(this.customerService.save(customer))

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody customer: Customer) =
        ResponseEntity.ok(this.customerService.update(id, customer))

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = this.customerService.delete(id)

    @PostMapping("upload-file")
    fun uploadFile(@RequestPart file: MultipartFile): ResponseEntity<UploadResponse> {
        val key = storageService.putRequestFile(file)
        return ResponseEntity.ok(UploadResponse(key))
    }

    @DeleteMapping("upload-file/{key}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteUploadedFile(@PathVariable key: String) =
        this.storageService.delete(key)

    @GetMapping("upload-file/{key}")
    fun getUploadedFile(@PathVariable key: String, response: HttpServletResponse): ResponseEntity<ByteArray> {
        val file = this.storageService.download(key)
        val fileExtension = getFileExtension(key)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$key.$fileExtension\"")
            .body(file)
    }

    fun getFileExtension(filename: String): String {
        val indexFileExtension = filename.split('.').lastIndex
        return filename.split('.')[indexFileExtension]
    }
}