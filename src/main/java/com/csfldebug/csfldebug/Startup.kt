package com.csfldebug.csfldebug

import com.csfldebug.csfldebug.config.DataEncryptionService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class Startup(private val dataEncryptionService: DataEncryptionService, private val addressRepo: AddressRepo, private val nestedAddressRepo: NestedAddressRepo) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {

        createDEKifNotExists()

        // save address with @ExplicitEncryption on simple fields (String)
        addressRepo.save(Address("zip", "city"))
        // works
        val allAddresses = addressRepo.findAll()

        //save document with a nested list of addresses which is @ExplicitEncrypted
        nestedAddressRepo.save(NestedDocument(listOf(NestedAddress("zip", "city"))))

        /**
         * Fails with
         * org.springframework.data.mapping.model.MappingInstantiationException: Failed to instantiate com.csfldebug.csfldebug.NestedDocument using constructor fun `<init>`(com.csfldebug.csfldebug.NestedAddress): com.csfldebug.csfldebug.NestedDocument with arguments [Document{{zip=zip, city=city, _class=com.csfldebug.csfldebug.NestedAddress}}]
         * if its just a simple List<Address>
         *
         * Fails with
         * java.lang.ClassCastException: class org.bson.Document cannot be cast to class org.bson.BsonValue (org.bson.Document and org.bson.BsonValue are in unnamed module of loader 'app')
         * when its a list of addresses
         */

        val allNested = nestedAddressRepo.findAll()

    }

    private fun createDEKifNotExists() {
        if (!dataEncryptionService.encryptionKeyExists()) {
            dataEncryptionService.createDataEncryptionKey()
            dataEncryptionService.createKeyAlternateNameIndex()
        }
    }

    private fun clearCollections(){
        nestedAddressRepo.deleteAll()
        addressRepo.deleteAll()

    }
}