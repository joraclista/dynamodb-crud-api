package com.github.joraclista.dynamodb.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.github.joraclista.dynamodb.LowLevelDao;
import com.github.joraclista.dynamodb.api.model.Key;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public abstract class AbstractDynamoDBTableCRUDApi {

    protected DynamoDBLowLevelCRUDApi crudApi;

    public AbstractDynamoDBTableCRUDApi(String tableName, AmazonDynamoDB amazonDynamoDBClient, Supplier<? extends RuntimeException> notFoundExceptionSupplier) {
        crudApi = DynamoDBLowLevelCRUDApi.builder()
                .tableName(tableName)
                .notFoundExceptionSupplier(notFoundExceptionSupplier)
                .dao(new LowLevelDao(new DynamoDB(amazonDynamoDBClient)))
                .build();
    }

    public Map<String, Object> setItemProperty(String id, String fieldName, Object value) {
        return crudApi.setItemProperty(getKey(id), fieldName, value);
    }

    public Map<String, Object> setItemProperties(String id, Map<String, Object> propertiesMap) {
        return crudApi.setItemProperties(getKey(id), propertiesMap);
    }

    public Map<String, Object> deleteItemProperty(String id, String fieldName) {
        return crudApi.deleteItemProperty(getKey(id), fieldName);
    }

    public Map<String, Object> deleteItemProperties(String id, Collection<String> fieldNames) {
        return crudApi.deleteItemProperties(getKey(id), fieldNames);
    }

    public boolean deleteItem(String id) {
        return crudApi.deleteItem(getKey(id));
    }

    public Map<String, Object> getItem(String id) {
        return crudApi.getItem(getKey(id));
    }

    public List<Map<String, Object>> getAllItems() {
        return crudApi.getAllItems();
    }

    protected abstract Key getKey(String id);
}
