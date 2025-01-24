package org.example.expert.domain.user.enums;

import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;


    /**
     * 시큐리티에서 인가를 ROLE_ADMIN or ROLE_USER로 반환해주기 때문에 여기서 ROLE_을 제거해주는 로직을 추가했습니다.
     */
    public static UserRole of(String role) {
        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }
        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalStateException("잘못된 enum 값입니다");
    }
}
