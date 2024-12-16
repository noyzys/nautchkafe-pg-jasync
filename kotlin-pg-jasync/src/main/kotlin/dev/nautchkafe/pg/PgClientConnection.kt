package dev.nautchkafe.pg

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

typealias SQLStatement = String
typealias SQLValueResult = List<Any?>?
typealias PGConnectionPool = ConnectionPool<PostgreSQLConnection>

@ExperimentalTime
class PgClientConnection(
    private val connectionProperties: PgConnectionProperties
) : PgStoreSqlConnection, PgPreparedQueryResult {

    private val pgConnectionPool: PGConnectionPool
        get() = createConnection()

    private fun createConnection() = PostgreSQLConnectionBuilder.createConnectionPool("pgurl") {
        connectionProperties.apply {
            host = hostname
            port
            database
            username
            password
        }

        connectionValidationInterval = DurationUnit.SECONDS.toMillis(1)
        maxIdleTime = DurationUnit.MINUTES.toMillis(1)
        maxActiveConnections = 10
        maxPendingQueries = 10_000
        maxActiveConnections = 100
    }

    override suspend fun connect() {
        pgConnectionPool.connect()
    }

    override suspend fun disconnect() {
        pgConnectionPool.disconnect()
    }

    private fun acquire(): SuspendingConnection = pgConnectionPool.asSuspending

    private suspend fun useSqlStatement(sqlStatement: SQLStatement, sqlValues: SQLValueResult): QueryResult = sqlValues
        ?.let<SQLValueResult, QueryResult> { acquire().sendPreparedStatement(sqlStatement, sqlValues) }
        ?: acquire().sendQuery(sqlStatement)

    override suspend fun applyQuery(sqlStatement: SQLStatement): QueryResult =
        useSqlStatement(sqlStatement, sqlValues = null)

    override suspend fun prepareQuery(sqlStatement: SQLStatement, sqlValues: SQLValueResult): QueryResult =
        useSqlStatement(sqlStatement, sqlValues)
}
