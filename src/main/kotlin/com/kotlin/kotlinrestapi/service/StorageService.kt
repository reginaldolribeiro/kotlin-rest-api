package com.kotlin.kotlinrestapi.service

import com.kotlin.kotlinrestapi.exception.InvalidFileException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.exception.SdkClientException
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.NoSuchKeyException
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream
import java.time.OffsetDateTime

@Service
class StorageService {

    @Value("\${aws.accessKeyId}")
    private val accessKeyId: String? = null

    @Value("\${aws.secretAccessKey}")
    private val secretAccessKey: String? = null

    @Value("\${aws.bucket}")
    private val bucket: String? = null

    private val region: Region = Region.US_EAST_1
    private val s3Client = S3Client.builder().region(region)
        .credentialsProvider { AwsBasicCredentials.create(accessKeyId, secretAccessKey) }
        .build()

    fun putRequestFile(file: MultipartFile): String {
        if(!isValidFile(file)) throw InvalidFileException("Invalid file!")

        println("Uploading file ${file.originalFilename}...")

        val (filename, fileExtension) = file.originalFilename!!.split(".")
        val key = "$filename-${OffsetDateTime.now()}.$fileExtension"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()

        val response = s3Client.putObject(
            putObjectRequest,
            RequestBody.fromBytes(file.bytes)
        )

        println("""
            Upload complete. Response from AWS: $response    
            Closing the connection to Amazon S3
        """.trimIndent())

        s3Client.close()

        println("""
            Connection closed
            Exiting...
        """.trimIndent())

        return key
    }

    fun delete(key: String) {
        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        try {
            s3Client.deleteObject(deleteRequest)
        } catch (e: SdkClientException){
            e.printStackTrace()
        }
    }

    fun isValidFile(multipartFile: MultipartFile) = multipartFile.bytes.isNotEmpty()

    fun getUploadedFile(key: String): InputStream? {
        return try {
            val request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build()
            s3Client.getObject(request)
        } catch (e: NoSuchKeyException) {
            null
        }
    }

    fun download(key: String): ByteArray {
        val file = getUploadedFile(key) ?: throw InvalidFileException("File not found!")
        return file.readBytes()
    }
}