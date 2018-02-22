package com.github.joraclista.dynamodb;

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
public abstract class AbstractDynamoDBTableCRUDService {

    protected DynamoDBLowLevelCRUDService dbLowLevelCRUDService;

    public AbstractDynamoDBTableCRUDService(String tableName, LowLevelDao dao, Supplier<? extends RuntimeException> notFoundExceptionSupplier) {
        dbLowLevelCRUDService = DynamoDBLowLevelCRUDService.builder()
                .tableName(tableName)
                .notFoundExceptionSupplier(notFoundExceptionSupplier)
                .dao(dao)
                .build();
    }

    public Map<String, Object> setItemProperty(String id, String fieldName, Object value) {
        return dbLowLevelCRUDService.setItemProperty(getKey(id), fieldName, value);
    }

    public Map<String, Object> setItemProperties(String id, Map<String, Object> propertiesMap) {
        return dbLowLevelCRUDService.setItemProperties(getKey(id), propertiesMap);
    }

    public Map<String, Object> deleteItemProperty(String id, String fieldName) {
        return dbLowLevelCRUDService.deleteItemProperty(getKey(id), fieldName);
    }

    public Map<String, Object> deleteItemProperties(String id, Collection<String> fieldNames) {
        return dbLowLevelCRUDService.deleteItemProperties(getKey(id), fieldNames);
    }

    public boolean deleteItem(String id) {
        return dbLowLevelCRUDService.deleteItem(getKey(id));
    }

    public Map<String, Object> getItem(String id) {
        return dbLowLevelCRUDService.getItem(getKey(id));
    }

    public List<Map<String, Object>> getAllItems() {
        return dbLowLevelCRUDService.getAllItems();
    }

    protected abstract Key getKey(String id);
}
