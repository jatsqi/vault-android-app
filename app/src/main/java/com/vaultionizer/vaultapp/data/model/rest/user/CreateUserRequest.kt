package com.vaultionizer.vaultapp.data.model.rest.user

data class CreateUserRequest(
    val key: String,
    val refFile: String,
    val username: String
)