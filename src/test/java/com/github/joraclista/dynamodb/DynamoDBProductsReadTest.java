package com.github.joraclista.dynamodb;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.joraclista.dynamodb.impl.ProductsTableCRUDApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
@RunWith(JUnitPlatform.class)
public class DynamoDBProductsReadTest {

    private static ProductsTableCRUDApi productsTableCRUDApi;

    @BeforeAll
    static void setup() {
        AmazonDynamoDB amazonClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
        productsTableCRUDApi = new ProductsTableCRUDApi(amazonClient);
    }

    @DisplayName("Read All Items & Display Test")
    @Test
    void testAllItemsDisplay() {
        productsTableCRUDApi.getAllItems().forEach(item -> log.info("item: [id:title]=[{}:{}]", item.get("id"), item.get("title")));
    }

    @DisplayName("Read Item Test")
    @ParameterizedTest(name = "Read Item Property Test: For item id = \"{0}\" : \"{1}\" should be \"{2}\"")
    @CsvSource({
            "991, title, Taffy swing dress",
            "991, merchantId, jadore-you",
            "992, title, Perfume Bottle Purse",
            "992, merchantId, aftershock-london",
            "991, status, PUBLIC",
            "992, status, DISABLED"
    })
    void testGetItem(String id, String propertyName, Object propertyValue) {
        productsTableCRUDApi.setItemProperty(id, propertyName, propertyValue);

        Map<String, Object> item = productsTableCRUDApi.getItem(id);
        assertEquals(item.get(propertyName), propertyValue);
    }


}
