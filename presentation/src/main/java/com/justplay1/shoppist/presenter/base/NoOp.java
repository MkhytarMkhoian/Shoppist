package com.justplay1.shoppist.presenter.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Dynamically proxy to generate a new object instance for a given class by using reflections
 */
final class NoOp {

    private static final InvocationHandler DEFAULT_VALUE = new DefaultValueInvocationHandler();

    private NoOp() {
        // no instances
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(Class<T> interfaceClass) {
        return (T) newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                DEFAULT_VALUE);
    }

    private static class DefaultValueInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return Defaults.defaultValue(method.getReturnType());
        }
    }
}