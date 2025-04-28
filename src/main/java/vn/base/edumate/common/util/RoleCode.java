package vn.base.edumate.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleCode {
    SYSTEM_ADMIN("Quản trị hệ thống"),
    USER("Người dùng");

    private final String roleName;
}
