package com.github.joraclista.dynamodb;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Map;

import static com.github.joraclista.dynamodb.ProductItemProperty.MERHCNAT_ID;
import static com.github.joraclista.dynamodb.ProductItemProperty.TITLE;
import static com.google.common.collect.ImmutableMap.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
@RunWith(JUnitPlatform.class)
public class DynamoDBEntityCRUDTest {

    private static ProductsEditionsService productsEditionsService;

    @BeforeAll
    static void setup() {
        AmazonDynamoDB amazonClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
        productsEditionsService = new ProductsEditionsService(new LowLevelDao(new DynamoDB(amazonClient)));
    }

    @DisplayName("All Items Display Test")
    @Disabled
    @Test
    void testAllItemsDisplay() {
        productsEditionsService.getAllItems().forEach(item -> log.info("item: id={}, title={}", item.getId(), item.getTitle()));
        log.info("Success");
    }

    @DisplayName("Remove Item Test")
    @Disabled
    @Test
    void testRemoveItem() {
        Map<String, Object> item = productsEditionsService.deleteItem("123");
        assertEquals(item.get(MERHCNAT_ID.getProperty()), "amazon");
        assertEquals(item.get(TITLE.getProperty()), "New Product");
    }

    @DisplayName("Set Item Property Test")
    @Test
    void testSetItemProperty() {
        productsEditionsService.setItemProperty("123", TITLE.getProperty(), "New Product");
        Map<String, Object> item = productsEditionsService.getItem("123");
        assertEquals(item.get(MERHCNAT_ID.getProperty()), "amazon");
        assertEquals(item.get(TITLE.getProperty()), "New Product");
    }

    @DisplayName("Set Item Properties Test")
    @Test
    void testSetItemProperties() {
        productsEditionsService.setItemProperties("123", of("title", "New Product","merchantId", "amazon"));
        Map<String, Object> item = productsEditionsService.getItem("123");
        assertEquals(item.get(MERHCNAT_ID.getProperty()), "amazon");
        assertEquals(item.get("title"), "New Product");
    }

    @DisplayName("Get Item Test")
    @Test
    void testGetItem() {
        Map<String, Object> item = productsEditionsService.getItem("123");
        assertEquals(item.get(MERHCNAT_ID.getProperty()), "amazon");
        assertEquals(item.get("title"), "New Product");
    }

    @DisplayName("Update Item Test")
    @Disabled
    @Test
    void testUpdateItem() {
        productsEditionsService.setItemProperty("123", "price", 100);
        productsEditionsService.setItemProperty("123", "valid", "yes");
        Map<String, Object> item = productsEditionsService.getItem("123");
        assertEquals(item.get("price"), BigDecimal.valueOf(100));
        assertEquals(item.get("valid"), "yes");
    }

    @DisplayName("Update Item Test 2")
    @Disabled
    @Test
    void testUpdateItem2() {
        productsEditionsService.setItemProperties("123", of("active", true, "merchantId", "ebay"));
        Map<String, Object> item = productsEditionsService.getItem("123");
        assertEquals(item.get("active"), true);
        assertEquals(item.get(MERHCNAT_ID.getProperty()), "ebay");
    }
}
