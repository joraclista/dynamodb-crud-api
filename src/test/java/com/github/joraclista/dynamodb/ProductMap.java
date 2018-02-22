package com.github.joraclista.dynamodb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alisa
 * version 1.0.
 */
public class ProductMap extends HashMap<String, Object> {

    public static final String ID = "id";
    public static final String MERCHANT_ID = "merchantId";
    public static final String TITLE = "title";

    public ProductMap(Map<String, Object> map) {
        putAll(map);
    }

    public String getId() {
        return get(ID) + "";
    }

    public String getMerchantId() {
        return get(MERCHANT_ID) + "";
    }

    public String getTitle() {
        return get(TITLE) + "";
    }

}
