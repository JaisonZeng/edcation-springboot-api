package top.alexjtech.educationapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    STUDENT(1, "学生"),
    TEACHER(2, "老师"),
    ADMIN(3, "管理员");

    private final Integer code;
    private final String description;

    public static UserType getByCode(Integer code) {
        for (UserType userType : UserType.values()) {
            if (userType.code.equals(code)) {
                return userType;
            }
        }
        return null;
    }
}
