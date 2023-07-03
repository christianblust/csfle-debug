package com.csfldebug.csfldebug

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepo: MongoRepository<Address, String>
