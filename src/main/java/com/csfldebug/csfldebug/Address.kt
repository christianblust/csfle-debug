package com.csfldebug.csfldebug

import org.springframework.data.mongodb.core.EncryptionAlgorithms
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.ExplicitEncrypted

@Document
data class Address(
    @ExplicitEncrypted(algorithm = EncryptionAlgorithms.AEAD_AES_256_CBC_HMAC_SHA_512_Random, keyAltName = "demo-data-key") private val zip: String,
    @ExplicitEncrypted(algorithm = EncryptionAlgorithms.AEAD_AES_256_CBC_HMAC_SHA_512_Random, keyAltName = "demo-data-key") private val city: String)
