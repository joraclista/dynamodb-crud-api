package com.github.joraclista.dynamodb;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.joraclista.dynamodb.impl.ProductsTableCRUDApi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
@RunWith(JUnitPlatform.class)
public class DynamoDBProductsUpdateTest {

    private static ProductsTableCRUDApi productsTableCRUDApi;

    @BeforeAll
    static void setup() {
        AmazonDynamoDB amazonClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
        productsTableCRUDApi = new ProductsTableCRUDApi(amazonClient);
    }

    @ParameterizedTest(name = "Update Item Property Test: For item id = \"{0}\" : \"{1}\" should be \"{2}\"")
    @CsvSource({
            "123, title, Taffy swing dress",
            "123, merchantId, jadore-you",
            "124, title, Perfume Bottle Purse",
            "124, merchantId, aftershock-london",
            "125, title, Dalis Skater Dress",
            "125, status, PUBLIC",
            "126, title, Natali poncho cape",
            "126, status, DISABLED",
            "127, title, Danica Oversized Blouse",
            "128, title, Zaliki Shoulder Bag",
            "129, title, Brynn Floral Print Dress"
    })
    @Test
    public void testSetItemProperty(String id, String propertyName, Object propertyValue) {
        productsTableCRUDApi.setItemProperty(id, propertyName, propertyValue);
        Map<String, Object> item = productsTableCRUDApi.getItem(id);
        assertEquals(item.get(propertyName), propertyValue);
    }

    @ParameterizedTest(name = "Update Item Properties Test: For item id = \"{0}\" : \"{1}\" should be \"{2}\"")
    @CsvSource({
            "223, title, Mabel Wide Faux Leather Belt, merchantId, jadore-you",
            "224, title, Mabel Wide Faux Leather Belt, merchantId, aftershock-london",
            "225, title, Mabel Wide Faux Leather Belt, status, PUBLIC",
            "226, title, Clara Glitter Oversized Sweater, status, DISABLED"
    })
    @Test
    public void testSetItemProperties(String id, String propertyName1, Object propertyValue1, String propertyName2, Object propertyValue2) {
        productsTableCRUDApi.setItemProperties(id, of(propertyName1, propertyValue1, propertyName2, propertyValue2));
        Map<String, Object> item = productsTableCRUDApi.getItem(id);
        assertEquals(item.get(propertyName1), propertyValue1);
        assertEquals(item.get(propertyName2), propertyValue2);
    }
}
