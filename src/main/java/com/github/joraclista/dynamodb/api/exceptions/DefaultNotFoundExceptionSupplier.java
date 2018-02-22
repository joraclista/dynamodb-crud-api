package com.github.joraclista.dynamodb.api.exceptions;

import java.util.function.Supplier;

/**
 * Created by Alisa
 * version 1.0.
 */
public class DefaultNotFoundExceptionSupplier implements Supplier<RuntimeException> {

    @Override
    public RuntimeException get() {
        return new RuntimeException("Entity not found");
    }
}
