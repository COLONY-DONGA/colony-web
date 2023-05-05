package colony.webproj.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_MEMBER("일반사용자"), ROLE_ADMIN("관리자"), ROLE_MANAGER("매니저"), ROLE_GUEST("게스트");

    private String description;

    Role(String description) {
        this.description = description;
    }
}
