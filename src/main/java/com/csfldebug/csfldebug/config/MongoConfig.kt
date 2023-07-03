package com.csfldebug.csfldebug.config

import com.mongodb.AutoEncryptionSettings
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.vault.ClientEncryption
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.PropertyValueConverterFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.convert.encryption.MongoEncryptionConverter
import org.springframework.data.mongodb.core.encryption.EncryptionKey
import org.springframework.data.mongodb.core.encryption.EncryptionKeyResolver
import org.springframework.data.mongodb.core.encryption.MongoClientEncryption

@Configuration
class MongoConfig(private val clientEncryption: ClientEncryption, private val masterKeyProvider: KeyProvider, private val appContext: ApplicationContext): AbstractMongoClientConfiguration(){
    companion object {
        val vaultCollectionName = "__keyVault"
        val dbName = "test"
        val keyVaultNamespace = "$dbName.$vaultCollectionName"
        val connectionString = ConnectionString("mongodb://user:password@localhost:27017/?authSource=admin&retryWrites=true&w=majority")
    }

    @Bean
    override fun mongoClient(): MongoClient {
        val autoEncryptionSettings = AutoEncryptionSettings.builder()
            .keyVaultNamespace(keyVaultNamespace)
            .kmsProviders(masterKeyProvider.kmsProviders())
            .bypassAutoEncryption(true)
            .build()

        return MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .autoEncryptionSettings(autoEncryptionSettings)
                .build()
        )
    }

   @Bean
   fun encryptingConverter(): MongoEncryptionConverter{
       val encryption = MongoClientEncryption.just(clientEncryption)
       val keyResolver = EncryptionKeyResolver.annotated{ _ -> EncryptionKey.keyAltName("demo-data-key")}
       return MongoEncryptionConverter(encryption, keyResolver)
   }

    override fun configureConverters(converterConfigurationAdapter: MongoCustomConversions.MongoConverterConfigurationAdapter) {
        converterConfigurationAdapter.registerPropertyValueConverterFactory(PropertyValueConverterFactory.beanFactoryAware(appContext))
    }

    override fun getDatabaseName(): String {
        return "test"
    }
}