package com.github.joraclista.dynamodb;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
@Builder
public class DynamoDbLowLevelService {

    private LowLevelDao dao;
    private String tableName;
    private Supplier<? extends RuntimeException> notFoundExceptionSupplier;
    
    private DynamoDbLowLevelService(LowLevelDao dao, String tableName, Supplier<? extends RuntimeException> notFoundExceptionSupplier) {
        if (dao == null || tableName == null || tableName.trim().isEmpty()) throw new IllegalArgumentException("Dao and tableName shouldn't be null");
        this.dao = dao;
        this.tableName = tableName;
        this.notFoundExceptionSupplier = notFoundExceptionSupplier == null 
                ? new DefaultNotFoundExceptionSupplier() 
                : notFoundExceptionSupplier;
    }
    
    public Map<String, Object> setItemProperty(Key key, String fieldName, Object value) {
        Map<String, Object> itemMap = dao.setItemProperty(tableName,
                key,
                Property.builder().name(fieldName).value(value).build());
        if (itemMap == null || itemMap.isEmpty()) {
            throw notFoundExceptionSupplier.get();
        }
        return itemMap;
    }

    public Map<String, Object> setItemProperties(Key key, Map<String, Object> propertiesMap) {
        Map<String, Object> itemMap = dao.setItemProperties(tableName, key, propertiesMap
                .entrySet()
                .stream()
                .map(e -> Property.builder().name(e.getKey()).value(e.getValue()).build())
                .collect(Collectors.toList()));
        if (itemMap == null || itemMap.isEmpty()) {
            throw notFoundExceptionSupplier.get();
        }
        return itemMap;
    }

    /**
     * Delete single key-value pair from item with @param key where @param fieldName is passed in args
     * @param key - affected item
     * @param fieldName - property to delete
     * @return key-value pairs that were left after deletion of specified properties
     */

    public Map<String, Object> deleteItemProperty(Key key, String fieldName) {
        Map<String, Object> itemMap = dao.deleteItemProperty(tableName, key, Property.builder()
                .name(fieldName)
                .build());
        if (itemMap == null || itemMap.isEmpty()) {
            throw notFoundExceptionSupplier.get();
        }
        return itemMap;
    }

    /**
     * Delete all key-value pairs from item with @param key where @param fieldNames is passed in args
     * @param key - affected item
     * @param fieldNames - properties to delete
     * @return key-value pairs that were left after deletion of specified properties
     */
    public Map<String, Object> deleteItemProperties(Key key, Collection<String> fieldNames) {
        Map<String, Object> itemMap = dao.deleteItemProperties(tableName, key, fieldNames.stream()
                .map(fieldName -> Property.builder().name(fieldName).build())
                .collect(Collectors.toList()));
        if (itemMap == null || itemMap.isEmpty()) {
            throw notFoundExceptionSupplier.get();
        }
        return itemMap;
    }

    /**
     * Delete item by key
     * @param key
     * @return true if item has been successfully deleted, false - otherwise
     */
    public boolean deleteItem(Key key) {
        Map<String, Object> itemMap = dao.deleteItem(tableName, key);
        return itemMap == null || itemMap.isEmpty();
    }

    public Map<String, Object> getItem(Key key) {
        Item item = dao.getItemByKey(tableName, key);
        if (item == null) {
            throw notFoundExceptionSupplier.get();
        }
        return item.asMap();
    }

    public List<Map<String, Object>> getAllItems() {
        try {
            return dao.getAllItems(tableName).stream()
                    .filter(item -> item != null)
                    .map(Item::asMap)
                    .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            log.warn("getAllItems: table name {} not found", tableName);
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
