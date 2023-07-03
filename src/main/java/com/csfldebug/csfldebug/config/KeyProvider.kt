package com.csfldebug.csfldebug.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom

@Configuration
class KeyProvider {

    private val path = "mk.txt"

    @Bean
    fun kmsProviders(): Map<String, Map<String, Any>> {
        return mapOf(
            "local" to mapOf<String, Any>(
                "key" to readOrCreateMasterKey()
            )
        )
    }

    private fun readOrCreateMasterKey(): ByteArray {
        val file = File(path)
        if (!file.exists()) {
            createLocalMasterKey()
        }
        return readLocalMasterKey()
    }

    private fun readLocalMasterKey(): ByteArray {
        val localMasterKeyRead = ByteArray(96)
        FileInputStream(path).use {
                fis ->
            if (fis.read(localMasterKeyRead) < 96)
                throw Exception("Expected to read 96 bytes from file")
        }
        return localMasterKeyRead
    }

    private fun createLocalMasterKey() {
        val localMasterKeyWrite = ByteArray(96)
        SecureRandom().nextBytes(localMasterKeyWrite)
        FileOutputStream(path).use { stream -> stream.write(localMasterKeyWrite) }
    }
}