/**
 * 
 */
package com.dss.java.tests;

import com.dss.java.bean.Cat;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author DSS
 *
 */
public class TestReflection {
	
	public static void main(String[] args) {
		//test();
		test1();
	}

	private static void test1() {
		try {
			Class<?> aClass = Class.forName("com.dss.java.bean.Cat");
			Object o = aClass.newInstance();
			System.out.println("o = " + o);
			ClassLoader classLoader = Class.forName("java.lang.String").getClassLoader();
			System.out.println("classLoader = " + classLoader);
			Properties properties = new Properties();
			String name = properties.getProperty("name", "dss");
			// 在静态方法里不能使用this
			ClassLoader classLoader1 = TestReflection.class.getClassLoader();
			// 尝试获取自己的泛型的类型，没成功
			Type type = aClass.getGenericSuperclass();
			String typeName = type.getTypeName();
			// 这里还是父类的泛型
			Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
			System.out.println("Arrays.toString(actualTypeArguments) = " + Arrays.toString(actualTypeArguments));
			System.out.println("typeName = " + typeName);

			// 获得指定方法
			Method setName = aClass.getMethod("setName", String.class);
			setName.invoke(o, "Jerry");
			System.out.println("o = " + o);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private static void test() {
		Class<Cat> catClass = Cat.class;
		Class<Class> classClass = Class.class;
		try {

			//Class aClass = classClass.newInstance();
			//System.out.println("aClass = " + aClass);
			Cat cat = catClass.newInstance();
			System.out.println("cat = " + cat);

			//Field name = catClass.getField("name"); // 只能获得声明为public的属性
			// 获取声明为private的属性
			Field name = catClass.getDeclaredField("name");
			name.setAccessible(true);
			name.set(cat, "Tom");
			System.out.println("cat = " + cat);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

	}
}
