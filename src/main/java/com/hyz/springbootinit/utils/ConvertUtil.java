package com.hyz.springbootinit.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hegd
 * @date 2023/6/19 17:23
 */
public class ConvertUtil {
    public static Map<String, String> convertObjectToMap(Object object) {
        Map<String, String> resultMap = new HashMap<>();
        // 获取对象的所有字段
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 设置字段可访问，以便读取值
            field.setAccessible(true);
            try {
                // 获取字段的值并转换为字符串
                Object value = field.get(object);
                String stringValue = (value != null) ? value.toString() : "";
                // 将字段名称和值添加到结果Map中
                resultMap.put(field.getName(), stringValue);
            } catch (IllegalAccessException e) {
                // 处理访问异常
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}
