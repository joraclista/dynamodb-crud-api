package com.github.joraclista.dynamodb.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.github.joraclista.dynamodb.LowLevelDao;
import com.github.joraclista.dynamodb.api.model.Key;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public abstract class AbstractDynamoDBTableCRUDApi {

    protected DynamoDBLowLevelCRUDApi crudApi;
    @Getter
    private Function<String, Key> keyFunction;

    public AbstractDynamoDBTableCRUDApi(String tableName, Function<String, Key> keyFunction, AmazonDynamoDB amazonDynamoDBClient, Supplier<? extends RuntimeException> notFoundExceptionSupplier) {
        crudApi = DynamoDBLowLevelCRUDApi.builder()
                .tableName(tableName)
                .notFoundExceptionSupplier(notFoundExceptionSupplier)
                .dao(new LowLevelDao(new DynamoDB(amazonDynamoDBClient)))
                .build();
        this.keyFunction = keyFunction;
    }

    public AbstractDynamoDBTableCRUDApi(String tableName, Function<String, Key> keyFunction, AmazonDynamoDB amazonDynamoDBClient) {
        this(tableName, keyFunction, amazonDynamoDBClient, null);
    }

    public Map<String, Object> setItemProperty(String id, String fieldName, Object value) {
        return crudApi.setItemProperty(keyFunction.apply(id), fieldName, value);
    }

    public Map<String, Object> setItemProperties(String id, Map<String, Object> propertiesMap) {
        return crudApi.setItemProperties(keyFunction.apply(id), propertiesMap);
    }

    public Map<String, Object> deleteItemProperty(String id, String fieldName) {
        return crudApi.deleteItemProperty(keyFunction.apply(id), fieldName);
    }

    public Map<String, Object> deleteItemProperties(String id, Collection<String> fieldNames) {
        return crudApi.deleteItemProperties(keyFunction.apply(id), fieldNames);
    }

    public boolean deleteItem(String id) {
        return crudApi.deleteItem(keyFunction.apply(id));
    }

    public Map<String, Object> getItem(String id) {
        return crudApi.getItem(keyFunction.apply(id));
    }

    public List<Map<String, Object>> getAllItems() {
        return crudApi.getAllItems();
    }

}
