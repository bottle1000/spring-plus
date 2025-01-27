package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    /**
     * CustomUserDetail 맞추려해서 지워줬습니다.
     */
    private final String nickname;
    private final String email;

    public UserResponse(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
