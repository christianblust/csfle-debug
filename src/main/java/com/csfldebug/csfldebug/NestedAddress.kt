package com.csfldebug.csfldebug

import org.springframework.data.mongodb.core.mapping.Document

data class NestedAddress(private val zip: String, private val city: String)
