package com.dss.java.tests.databases.utils;

import java.lang.reflect.*;

/**
 * FileName: ReflectionUtils
 * Author: Chris
 * Date: 2018/10/25 15:54
 * Description: Reflection Utils
 */
public class ReflectionUtils {

    /**
     * 通过反射设置变量的值
     *
     * @param obj
     * @param name
     * @param value
     */
    public static void setFieldValue(Object obj, String name, Object value) {
        Field field = getDeclaredField(obj, name);

        if (field == null) {
            throw new IllegalArgumentException("There is not [" + name + "] in " + obj);
        }
        // 设置访问权限
        checkAndSetAccessible(field);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void checkAndSetAccessible(Field field) {
        // 私有属性，设置访问权限
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射获得变量
     *
     * @param obj
     * @param name
     * @return
     */
    private static Field getDeclaredField(Object obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            return field;
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
            String name_new = "m" + name.substring(0, 1).toUpperCase() + name.substring(1);
            try {
                Field field = obj.getClass().getDeclaredField(name_new);
                return field;
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Class<T> getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型
     * 如: public EmployeeDao extends BaseDao<Employee, String>
     *
     * @param clazz
     * @return
     */
    private static Class getSuperClassGenericType(Class clazz, int index) {
        Type superType = clazz.getGenericSuperclass();
        if (!(superType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] types = ((ParameterizedType) superType).getActualTypeArguments();
        if (index >= types.length || index < 0) {
            return Object.class;
        }
        if (!(types[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) types[index];
    }

    public void test() {
        Class clazz = null;
        Method method = null;
        String name = null;
        Object args = null;
        try {
            Object obj = clazz.newInstance();
            method = clazz.getDeclaredMethod(name, Object.class);
            method.invoke(obj, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
