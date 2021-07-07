package com.nostyling.wcms.utils.object;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @author shiliang
 * @Classname ObjectUtils
 * @Date 2021/7/6 10:30
 * @Description ObjectUtils
 */
public class ObjectUtils {
    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return
     * @throws Exception
     */
    public static Object forceGetFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        return forceGetFieldValue(obj, field);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param field 属性名称所在的对象
     * @return
     * @throws Exception
     */
    public static Object forceGetFieldValue(Object obj, Field field) throws Exception {
        Object object = null;
        boolean accessible = field.canAccess(obj);
        if (!accessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            object = field.get(obj);
            // 还原private,protected属性的访问性质
            field.setAccessible(accessible);
            return object;
        }
        object = field.get(obj);
        return object;
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param obj       属性名称所在的对象
     * @param fieldName 属性名称
     * @return
     * @throws Exception
     */
    public static void forceSetFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        forceSetFieldValue(obj, field, value);
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param field 属性名称所在的对象
     * @return
     * @throws Exception
     */
    public static void forceSetFieldValue(Object obj, Field field, Object value) throws Exception {

        boolean accessible = field.canAccess(obj);
        Class<?> fieldType = field.getType();
        if (!accessible) {
            // 如果是private,protected修饰的属性，需要修改为可以访问的
            field.setAccessible(true);
            forceSetFieldValue(obj, field, value, fieldType);
            // 还原private,protected属性的访问性质
            field.setAccessible(accessible);
            return;
        }
        forceSetFieldValue(obj, field, value, fieldType);
        return;
    }

    /**
     * 获取指定对象的指定属性值（去除private,protected的限制）
     *
     * @param field 属性名称所在的对象
     * @return
     * @throws Exception
     */
    public static void forceSetFieldValue(Object obj, Field field, Object value, Class<?> fieldType) throws Exception {

        if (null != value) {
            if (fieldType.isInstance(value)) {
                field.set(obj, value);
            } else {
                value = value.toString();
                Constructor<?> constructor = fieldType.getConstructor(value.getClass());
                field.set(obj, constructor.newInstance(value));
            }
        }

    }


    /**
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyObject(Object source, Object target) throws Exception {
        copyObject(source, target, true);
    }

    /**
     * @param source
     * @param target
     * @param isCopyNotNull 是否保留目标对象原值 true 是保留
     * @throws Exception
     */
    public static void copyObject(Object source, Object target, boolean isCopyNotNull) throws Exception {
        // class.getFields()只能获取公开的属性
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {

            Object targetValue = forceGetFieldValue(target, field);
            if (isCopyNotNull && null != targetValue) {
                continue;
            } else {
                Object sourceValue = forceGetFieldValue(source, field);
                forceSetFieldValue(target, field, sourceValue);
            }

        }

    }

}
