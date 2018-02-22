package com.github.joraclista.dynamodb;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Alisa
 * version 1.0.
 */
@Slf4j
public class ProductsEditionsService extends BaseEditionsService {

    public static final String TABLE = "Products";
    public static final String EVENT_ID = "id";


    public ProductsEditionsService(LowLevelDao dao) {
       super(TABLE, dao, () -> new RuntimeException("Product can't be found"));
    }

    public List<ProductMap> getAllItems() {
        return dynamoDbLowLevelService.getAllItems()
                .stream()
                .map(ProductMap::new)
                .collect(Collectors.toList());
    }


    public Collection<ProductMap> getFilteredItems(String market, Predicate<ProductMap> predicate) {
        return dynamoDbLowLevelService.getAllItems().stream()
                .filter(item -> item != null)
                .map(ProductMap::new)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Override
    protected Key getKey(String id) {
        return Key.builder().hashKeyName(EVENT_ID).hashKeyValue(id).build();
    }
}
