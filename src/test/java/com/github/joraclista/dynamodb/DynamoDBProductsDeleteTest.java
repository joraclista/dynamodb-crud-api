package com.github.joraclista.dynamodb;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.github.joraclista.dynamodb.impl.ProductsTableCRUDApi;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
@RunWith(JUnitPlatform.class)
public class DynamoDBProductsDeleteTest {

    private static ProductsTableCRUDApi productsTableCRUDApi;

    @BeforeAll
    static void setup() {
        AmazonDynamoDB amazonClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
        productsTableCRUDApi = new ProductsTableCRUDApi(amazonClient);
    }

    @ParameterizedTest(name = "Delete Item Property Test: For item id = \"{0}\" : \"{1}\" should be null")
    @CsvSource({
            "552, title, Taffy swing dress",
            "553, merchantId, jadore-you"
    })
    @Test
    public void testDeleteItemProperty(String id, String propertyName, Object propertyValue) {
        productsTableCRUDApi.setItemProperty(id, propertyName, propertyValue);

        productsTableCRUDApi.deleteItemProperty(id, propertyName);
        Map<String, Object> item = productsTableCRUDApi.getItem(id);
        assertNull(item.get(propertyName));
    }

    @ParameterizedTest(name = "Delete Properties Test: For item id = \"{0}\" : \"{1}\" should be null")
    @CsvSource({
            "555, title, Mabel Wide Faux Leather Belt, price, 19.9",
            "556, title, Mabel Wide Faux Leather Belt, price, 150.00"
    })
    @Test
    public void testDeleteItemProperties(String id, String propertyName1, Object propertyValue1, String propertyName2, Object propertyValue2) {
        productsTableCRUDApi.setItemProperties(id, of(propertyName1, propertyValue1, propertyName2, propertyValue2));

        productsTableCRUDApi.deleteItemProperties(id, ImmutableList.of(propertyName1, propertyName2));
        Map<String, Object> item = productsTableCRUDApi.getItem(id);
        assertNull(item.get(propertyName1));
        assertNull(item.get(propertyName2));
    }

    @ParameterizedTest(name = "Delete Item Test: For item id = \"{0}\" : \"{1}\" should throw exception on attempt to get item by id")
    @CsvSource({
            "771, title, Mabel Wide Faux Leather Belt",
            "772, title, Taffy swing dress"
    })
    @Test
    public void testDeleteItem(String id, String propertyName1, Object propertyValue1) {
        productsTableCRUDApi.setItemProperties(id, of(propertyName1, propertyValue1));

        boolean deleteItemResult = productsTableCRUDApi.deleteItem(id);
        assertTrue(deleteItemResult);

        Throwable exception = assertThrows(RuntimeException.class, () -> productsTableCRUDApi.getItem(id));
        assertEquals(exception.getMessage(), "Product can't be found");
    }
}
