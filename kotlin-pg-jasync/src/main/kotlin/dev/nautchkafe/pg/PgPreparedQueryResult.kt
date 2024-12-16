package dev.nautchkafe.pg

import com.github.jasync.sql.db.QueryResult

interface PgPreparedQueryResult {

    suspend fun applyQuery(sqlStatement: SQLStatement): QueryResult
    suspend fun prepareQuery(sqlStatement: SQLStatement, sqlValues: SQLValueResult): QueryResult
}