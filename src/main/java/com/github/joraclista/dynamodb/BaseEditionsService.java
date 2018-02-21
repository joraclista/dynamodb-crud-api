package com.github.joraclista.dynamodb;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public abstract class BaseEditionsService {

    protected DynamoDbLowLevelService dynamoDbLowLevelService;

    public BaseEditionsService(String tableName, LowLevelDao dao, Supplier<? extends RuntimeException> notFoundExceptionSupplier) {
        dynamoDbLowLevelService = DynamoDbLowLevelService.builder()
                .tableName(tableName)
                .notFoundExceptionSupplier(notFoundExceptionSupplier)
                .dao(dao)
                .build();
    }

    public Map<String, Object> setItemProperty(String id, String fieldName, Object value) {
        return dynamoDbLowLevelService.setItemProperty(getKey(id), fieldName, value);
    }

    public Map<String, Object> setItemProperties(String id, Map<String, Object> propertiesMap) {
        return dynamoDbLowLevelService.setItemProperties(getKey(id), propertiesMap);
    }

    public Map<String, Object> deleteItemProperty(String id, String fieldName) {
        return dynamoDbLowLevelService.deleteItemProperty(getKey(id), fieldName);
    }

    public Map<String, Object> deleteItemProperties(String id, Collection<String> fieldNames) {
        return dynamoDbLowLevelService.deleteItemProperties(getKey(id), fieldNames);
    }

    public Map<String, Object> getItem(String id) {
        return dynamoDbLowLevelService.getItem(getKey(id));
    }


    protected abstract Key getKey(String id);
}
