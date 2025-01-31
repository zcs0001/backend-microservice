package com.tree.backendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户状态枚举
 */
public enum UserStatusEnum {

    NORMAL("正常", "0"),
    WRITE_OFF("注销", "1"),
    BAN("封号", "2");

    private final String text;

    private final String value;

    UserStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserStatusEnum anEnum : UserStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
    public static UserStatusEnum getEnumByText(String text) {
        for (UserStatusEnum value : UserStatusEnum.values()) {
            if (value.getText().equals(text)) {
                return value;
            }
        }
        return null;
    }
    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
