package com.mataycode.recruitment.dto;

import java.util.List;

public record CreateOrderRequest(
        List<Long> productsIds
) {
}
