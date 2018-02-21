package com.github.joraclista.dynamodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Alisa
 * version 1.0.
 */
@Builder
@AllArgsConstructor
@Data
public class Key {
    private String hashKeyName;
    private Object hashKeyValue;
    private String rangeKeyName;
    private Object rangeKeyValue;
}
