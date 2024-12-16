package dev.nautchkafe.pg

sealed interface PgStoreConnection {

    suspend fun connect()
    suspend fun disconnect()
}

interface PgStoreSqlConnection : PgStoreConnection

data class PgConnectionProperties(
    val hostname: String,
    val port: Int = 5432,
    val database: String,
    val username: String,
    val password: String
)
