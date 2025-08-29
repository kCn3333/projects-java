package com.kcn.orders_api.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Authority implements GrantedAuthority {

    private String authority;

}
