package com.kcn.orders_api.dto.response;

import com.kcn.orders_api.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {

    private long id;
    private String fullName;
    private String email;
    private List<String> authorities;

}
