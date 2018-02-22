package com.github.joraclista.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Alisa
 * version 1.0.
 */
@AllArgsConstructor
@Getter
public enum ProductItemProperty {
    TITLE("title"),
    MERHCNAT_ID("merhcantId"),
    PRICE("price"),
    ACTIVE("active"),
    STATUS("status"),
    CURRENCY("currency"),
    STOCK("stock");

    private final String property;
}
