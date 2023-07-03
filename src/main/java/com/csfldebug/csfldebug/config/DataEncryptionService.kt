package com.csfldebug.csfldebug.config

import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.vault.DataKeyOptions
import com.mongodb.client.vault.ClientEncryption
import org.bson.BsonBinary
import org.springframework.stereotype.Service

@Service
class DataEncryptionService(private val mongoClient: MongoClient,
                            private val clientEncryption: ClientEncryption,
) {
    companion object {
        private val keyAltNames = mutableListOf("demo-data-key")
    }

    fun createDataEncryptionKey(): BsonBinary {
        return clientEncryption.createDataKey("local", DataKeyOptions().keyAltNames(keyAltNames))
    }

    fun encryptionKeyExists(): Boolean {
        val collection = mongoClient.getDatabase(MongoConfig.dbName).getCollection(MongoConfig.vaultCollectionName)
        val doc = collection.find(Filters.`in`("keyAltNames", keyAltNames)).first()
        return doc != null
    }

    fun createKeyAlternateNameIndex() {
        val vaultCollection = mongoClient.getDatabase(MongoConfig.dbName).getCollection(MongoConfig.vaultCollectionName)
        vaultCollection.createIndex(
            Indexes.ascending("keyAltNames"),
            IndexOptions().unique(true).partialFilterExpression(Filters.exists("keyAltNames"))
        )
    }
}