package com.github.joraclista.dynamodb;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.github.joraclista.dynamodb.api.model.Key;
import com.github.joraclista.dynamodb.api.model.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Alisa
 * version 1.0.
 */
public class LowLevelDao {

    private DynamoDB dynamoDB;

    public LowLevelDao(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public Table getTable(String tableName) {
        validateArgs(tableName);
        return dynamoDB.getTable(tableName);
    }

    public Item getItemByKey(String tableName, Key key) {
        return getTable(tableName).getItem(new GetItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withConsistentRead(true));
    }

    public List<Item> getAllItems(String tableName) {
        ItemCollection<ScanOutcome> items = getTable(tableName).scan(new ScanSpec());
        List<Item> result = new LinkedList<>();
        for(Item item : items) {
            result.add(item);
        }
        return result;
    }

    public Item getItemByKey(Table table, Key key) {
        return table.getItem(new GetItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withConsistentRead(true));
    }

    public Object getItemProperty(Item item, String propertyName) {
        validateArgs(item, propertyName);
        return item.get(propertyName);
    }

    private PrimaryKey getPrimaryKey(Key key) {
        validateArgs(key.getHashKeyName(), key.getHashKeyValue());
        if (key.getRangeKeyName() == null || key.getRangeKeyName().trim().isEmpty()) {
            return new PrimaryKey(key.getHashKeyName(), key.getHashKeyValue());
        }
        validateArgs(key.getRangeKeyValue());
        return new PrimaryKey(key.getHashKeyName(), key.getHashKeyValue(), key.getRangeKeyName(), key.getRangeKeyValue());
    }

    public Map<String, Object> setItemProperty(String tableName, Key key, Property property) {
        Table table = getTable(tableName);
        validateArgs(property.getName());
        return getResult(table.updateItem(new UpdateItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.ALL_NEW)
                .withAttributeUpdate(new AttributeUpdate(property.getName()).put(property.getValue()))));
    }

    public Map<String, Object> setItemProperties(String tableName, Key key, List<Property> properties) {
        Table table = getTable(tableName);
        properties.forEach(property -> validateArgs(property.getName()));

        return getResult(table.updateItem(new UpdateItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.ALL_NEW)
                .withAttributeUpdate(properties.stream()
                        .map(property -> new AttributeUpdate(property.getName()).put(property.getValue()))
                        .collect(Collectors.toList()))));
    }

    public Map<String, Object> addItemProperty(String tableName, Key key, Property property) {
        Table table = getTable(tableName);
        validateArgs(property.getName());
        return getResult(table.updateItem(new UpdateItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.ALL_NEW)
                .withAttributeUpdate(new AttributeUpdate(property.getName()).addElements(property.getValue()))));
    }

    public Map<String, Object> deleteItemProperty(String tableName, Key key, Property property) {
        Table table = getTable(tableName);
        validateArgs(property.getName());
        return getResult(table.updateItem(new UpdateItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.ALL_NEW)
                .withAttributeUpdate(new AttributeUpdate(property.getName()).delete())));
    }

    public Map<String, Object> deleteItemProperties(String tableName, Key key, List<Property> properties) {
        Table table = getTable(tableName);
        properties.forEach(property -> validateArgs(property.getName()));

        return getResult(table.updateItem(new UpdateItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.ALL_NEW)
                .withAttributeUpdate(properties.stream()
                        .map(property -> new AttributeUpdate(property.getName()).delete())
                        .collect(Collectors.toList()))));
    }

    public Map<String, Object> deleteItem(String tableName, Key key) {
        Table table = getTable(tableName);
        return getResult(table.deleteItem(new DeleteItemSpec()
                .withPrimaryKey(getPrimaryKey(key))
                .withReturnValues(ReturnValue.NONE)));
    }

    private void validateArgs(Object... args) {
        if (args == null || args.length == 0) throw new IllegalArgumentException("No arguments provided");
        for(Object arg : args) {
            if (arg == null || (arg instanceof String && ((String)arg).trim().isEmpty()))
                throw new IllegalArgumentException("Argument [" + arg + "] should not be null or empty");
        }
    }

    private Map<String, Object> getResult(UpdateItemOutcome outcome) {
        return InternalUtils.toSimpleMapValue(outcome.getUpdateItemResult().getAttributes());
    }

    private Map<String, Object> getResult(DeleteItemOutcome outcome) {
        return InternalUtils.toSimpleMapValue(outcome.getDeleteItemResult().getAttributes());
    }
}
