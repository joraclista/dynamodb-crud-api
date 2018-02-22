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

    public ProductsTableCRUDApi(AmazonDynamoDB amazonDynamoDBClient) {
       super("ShopProducts", id -> Key.builder().hashKeyName("id").hashKeyValue(id).build(), amazonDynamoDBClient, () -> new RuntimeException("Product can't be found"));
    }

}
