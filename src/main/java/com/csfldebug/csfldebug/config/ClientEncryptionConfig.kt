package com.csfldebug.csfldebug.config

import com.mongodb.ClientEncryptionSettings
import com.mongodb.MongoClientSettings
import com.mongodb.client.vault.ClientEncryption
import com.mongodb.client.vault.ClientEncryptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClientEncryptionConfig(private val masterKeyProvider: KeyProvider) {

    @Bean
    fun encryptionClient(): ClientEncryption {
        val clientEncryptionSettings = ClientEncryptionSettings.builder()
            .keyVaultMongoClientSettings(
                MongoClientSettings.builder()
                    .applyConnectionString(MongoConfig.connectionString)
                    .build()
            )
            .keyVaultNamespace(MongoConfig.keyVaultNamespace)
            .kmsProviders(masterKeyProvider.kmsProviders())
            .build()
        return ClientEncryptions.create(clientEncryptionSettings)
    }
}