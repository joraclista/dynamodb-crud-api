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
public class Property {
    private String name;
    private Object value;
}
