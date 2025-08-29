package com.kcn.orders_api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private String name;
    private String description;
    private Integer quantity;
    private Double price;
}
