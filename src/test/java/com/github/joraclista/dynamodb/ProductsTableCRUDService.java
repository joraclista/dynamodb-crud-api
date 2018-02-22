package com.github.joraclista.dynamodb;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public class ProductsTableCRUDService extends AbstractDynamoDBTableCRUDService {

    public static final String TABLE = "ShopProducts";
    public static final String EVENT_ID = "id";


    public ProductsTableCRUDService(LowLevelDao dao) {
       super(TABLE, dao, () -> new RuntimeException("Product can't be found"));
    }

    @Override
    protected Key getKey(String id) {
        return Key.builder().hashKeyName(EVENT_ID).hashKeyValue(id).build();
    }
}
