package dev.nautchkafe.postgres;

import io.vavr.control.Try;
import io.vavr.collection.List;

interface PgQueryExecutor {

    <T> Try<List<T>> executeQuery(final String sql, final List<Object> params, final ResultSetMapper<T> mapper);

    Try<Void> executeUpdate(final String sql, final List<Object> params);

    Try<Void> executeBatch(final String sql, final List<List<Object>> batchParams);
}