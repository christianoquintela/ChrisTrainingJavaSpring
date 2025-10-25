package com.example.training.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ProfileENUM {

    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private Integer code;
    private String description;

    public static ProfileENUM toEnum(Integer code) {

        if (Objects.isNull(code)) return null;
        for (ProfileENUM x : ProfileENUM.values()) {
            if (code.equals(x.getCode())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
