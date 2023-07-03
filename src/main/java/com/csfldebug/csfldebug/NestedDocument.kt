package com.csfldebug.csfldebug

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.EncryptionAlgorithms
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.ExplicitEncrypted
import java.util.*

@Document
data class NestedDocument(
    @ExplicitEncrypted(algorithm = EncryptionAlgorithms.AEAD_AES_256_CBC_HMAC_SHA_512_Random, keyAltName = "demo-data-key") private val nestedAddress: List<NestedAddress>) {
    @Id private val id: String = UUID.randomUUID().toString()
}