package dev.nautchkafe.postgres;

import io.vavr.control.Try;

public interface PgTransaction {

    Try<Void> beginTransaction();

    Try<Void> commitTransaction();

    Try<Void> rollbackTransaction();
}