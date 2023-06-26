package com.hyz.springbootinit.model.enums;

import com.hyz.springbootinit.common.ErrorCode;
import com.hyz.springbootinit.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hegd
 * @date 2023/6/20 11:14
 */
@AllArgsConstructor
@Getter
public enum UserRoleEnum {
    USER("用户", 0),
    ADMIN("管理员", 1),
    BAN("被封号", 2);

    private final String text;

    private final Integer value;

    /**
     * 获得枚举类
     *
     * @param value 价值
     * @return {@link UserRoleEnum}
     */
    public static UserRoleEnum getByValue(Integer value) {
        if (value == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        return null;
    }

    public static List<Integer> getValueList() {
        return Arrays.stream(UserRoleEnum.values()).map(UserRoleEnum::getValue).collect(Collectors.toList());
    }
}
