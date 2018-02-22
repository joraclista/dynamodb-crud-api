package com.github.joraclista.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.github.joraclista.dynamodb.api.AbstractDynamoDBTableCRUDApi;
import com.github.joraclista.dynamodb.api.model.Key;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public class ProductsTableCRUDApi extends AbstractDynamoDBTableCRUDApi {

    public static final String TABLE = "ShopProducts";
    public static final String EVENT_ID = "id";


    public ProductsTableCRUDApi(AmazonDynamoDB amazonDynamoDBClient) {
       super(TABLE, amazonDynamoDBClient, () -> new RuntimeException("Product can't be found"));
    }

    @Override
    protected Key getKey(String id) {
        return Key.builder().hashKeyName(EVENT_ID).hashKeyValue(id).build();
    }
}
