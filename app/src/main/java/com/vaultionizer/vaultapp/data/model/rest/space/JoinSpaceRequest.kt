package com.vaultionizer.vaultapp.data.model.rest.space

data class JoinSpaceRequest(
    val spaceID: Long,
    val authKey: String
)