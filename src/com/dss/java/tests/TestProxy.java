package com.dss.java.tests;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * User: DSS
 * Date: 2018/9/9
 * Time: 13:15
 * Tag: Test Proxy
 */
public class TestProxy {

    public static void main(String[] args) {
//        testDynamicProxy();
        testAOPProxy();
    }

    private static void testAOPProxy() {
        SuperMan superMan = new SuperMan(); // 被代理类
        Human human = (Human) DynamicProxyFactory.getProxyInstance(superMan); // 代理类
        // 用来验证AOP，面向切面编程，在代码块中动态的插入方法
        /**
         * 这里是第一个方法
         * SuperMan flying....
         * 这里是第二个方法
         */
        human.fly();
        System.out.println();
        /**
         * 这里是第一个方法
         * I'm Stronger
         * 这里是第二个方法
         */
        human.show();

        System.out.println();
        // 只需要声明一个被代理类，然后通过包装类，动态的去获取代理类对象，然后用代理类对象去执行方法
        NikeClothFactory nikeClothFactory = new NikeClothFactory();
        ClothFactory clothFactory = (ClothFactory) DynamicProxyFactory.getProxyInstance(nikeClothFactory);
        clothFactory.productCloth();
    }

    @Test
    public void testDynamicProxy() {
        // 被代理类的对象
        RealSubject realSubject = new RealSubject();
        // 实现InvocationHandler接口的对象
        MyInvocationHandler handler = new MyInvocationHandler();
        // 调用blind()方法，动态的返回实现了Subject接口的代理类对象
        Subject subject = (Subject) handler.blind(realSubject);
        // 这里会调用到handler中的invoke()
        int result = subject.action(1, 2);
        System.out.println("result = " + result);
    }

    // 静态代理
    @Test
    public  void testStaticProxy() {
        // 创建被代理类对象
        NikeClothFactory nikeClothFactory = new NikeClothFactory();
        // 创建代理类对象
        ProxyFactory proxyFactory = new ProxyFactory(nikeClothFactory);
        // 通过代理类对象调用重写的方法
        proxyFactory.productCloth();
    }

}

// 接口
interface ClothFactory{
    void productCloth();
}

/**
 *  被代理类
 */
class NikeClothFactory implements ClothFactory {
    @Override
    public void productCloth() {
        System.out.println("Nike工厂开始生产...");
    }
}

/**
 *   代理类
 */
class ProxyFactory implements ClothFactory {
    private NikeClothFactory mNikeClothFactory;

    public ProxyFactory(NikeClothFactory nikeClothFactory) {
        mNikeClothFactory = nikeClothFactory;
    }

    @Override
    public void productCloth() {
        mNikeClothFactory.productCloth();
    }
}

/**
 *  接口
 */
interface Subject{
    int action(int a, int b);
}

/**
 *  被代理类
 */
class RealSubject implements Subject {

    @Override
    public int action(int a, int b) {
        System.out.println("这里是被代理类:" + getClass().getName());
        return a + b;
    }
}

/**
 *  动态实现类
 */
class MyInvocationHandler implements InvocationHandler {
    private Object mObject;

    /**
     *  给被代理的对象实例化，并
     *  返回一个代理类对象
     * @param o
     * @return
     */
    public Object blind(Object o){
        mObject = o;
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy = " + proxy.getClass().getName());
        Object invoke = method.invoke(mObject, args);
        return invoke;
    }
}

/**
 *  AOP，面向切面编程
 */

/**
 *  接口
 */
interface Human {
    void fly();
    void show();
}

/**
 *  被代理类
 */
class SuperMan implements Human{

    @Override
    public void fly() {
        System.out.println("SuperMan flying....");
    }

    @Override
    public void show() {
        System.out.println("I'm Stronger");
    }
}

/**
 *  AOP，固定的方法块
 */
class HumanUtils{
    public void method1() {
        System.out.println("这里是第一个方法");
    }

    public void method2(){
        System.out.println("这里是第二个方法");
    }
}

/**
 *  动态代理
 */
class MyInvocation implements InvocationHandler {
    private final HumanUtils mHumanUtils;
    private Object mObject;

    public MyInvocation(){
        mHumanUtils = new HumanUtils();
    }
    /**
     * 设置被代理类对象
     * @param object
     */
    public void setObject(Object object) {
        mObject = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 这里调用HumanUtils中的method1和method2，就是用来验证在一个固定的方法快中，动态的插入一个方法
        // method1 和 method2 就是固定的方法块
        mHumanUtils.method1();
        Object invoke = method.invoke(mObject, args);
        mHumanUtils.method2();
        return invoke;
    }
}

/**
 *  动态的获取代理类
 */
class DynamicProxyFactory {
    static MyInvocation mInvocation = new MyInvocation();
    public static Object getProxyInstance(Object object) {
        mInvocation.setObject(object);
        Object instance = Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), mInvocation);
        return instance;
    }
}

class TestInvocation implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
