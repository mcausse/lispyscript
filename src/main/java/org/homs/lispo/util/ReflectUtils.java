package org.homs.lispo.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectUtils {

    final static Map<Class<?>, Class<?>> m = new LinkedHashMap<>();

    static {
        m.put(boolean.class, Boolean.class);
        m.put(char.class, Character.class);
        m.put(short.class, Short.class);
        m.put(int.class, Integer.class);
        m.put(long.class, Long.class);
        m.put(float.class, Float.class);
        m.put(double.class, Double.class);
    }

    private static boolean isAssignableFrom(Class<?> c1, Class<?> c2) {
        if (m.containsKey(c1)) {
            c1 = m.get(c1);
        }
        if (m.containsKey(c2)) {
            c2 = m.get(c2);
        }
        return c1.isAssignableFrom(c2);
    }

    public static Object callMethod(Object target, String methodName, final Object[] args) throws RuntimeException {

        try {
            Class<?> targetClass = target.getClass();
            for (Method method : targetClass.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                    boolean typesMatches = true;
                    for (int i = 0; i < method.getParameterCount(); i++) {
                        if (args[i] != null
                                && !isAssignableFrom(method.getParameters()[i].getType(), args[i].getClass())) {
                            typesMatches = false;
                            break;
                        }
                    }
                    if (typesMatches) {
                        if (!method.canAccess(target)) {
                            method.setAccessible(true);
                        }
                        return method.invoke(target, args);
                    }
                }
            }
            throw new RuntimeException(
                    "no method found for: " + targetClass.getName() + "#" + methodName + Arrays.toString(args));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callStaticMethod(String beanClassName, String methodName, Object[] args) {
        try {
            Class<?> beanClass = Class.forName(beanClassName);
            for (Method method : beanClass.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                    boolean typesMatches = true;
                    for (int i = 0; i < method.getParameterCount(); i++) {
                        if (args[i] != null
                                && !isAssignableFrom(method.getParameters()[i].getType(), args[i].getClass())) {
                            typesMatches = false;
                            break;
                        }
                    }
                    if (typesMatches) {
                        return method.invoke(null, args);
                    }
                }
            }
            throw new RuntimeException(
                    "no method found for: " + beanClassName + "#" + methodName + Arrays.toString(args));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getStaticField(String className, String fieldName) {
        try {
            Class<?> beanClass = Class.forName(className);
            return beanClass.getField(fieldName).get(null);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(final Class<?> beanClass, final Object[] args) throws RuntimeException {

        try {
            for (Constructor<?> ctor : beanClass.getConstructors()) {
                if (ctor.getParameterCount() == args.length) {
                    boolean typesMatches = true;
                    for (int i = 0; i < ctor.getParameterCount(); i++) {
                        if (args[i] != null
                                && !isAssignableFrom(ctor.getParameters()[i].getType(), args[i].getClass())) {
                            typesMatches = false;
                            break;
                        }
                    }
                    if (typesMatches) {
                        return ctor.newInstance(args);
                    }
                }
            }
            throw new RuntimeException(
                    "no constructor found for: " + beanClass.getName() + "#" + Arrays.toString(args));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object newInstance(final String beanClassName, final Object[] args) throws RuntimeException {

        try {
            Class<?> beanClass = Class.forName(beanClassName);
            return newInstance(beanClass, args);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public static Object get(final Object targetBean, final String field) {
//        PropertyAccessor propertyAccessor;
//        try {
//            Method m = targetBean.getClass().getMethod(getGetterName(field));
//            propertyAccessor = new PropertyAccessor(m);
//        } catch (final Exception e) {
//            try {
//                Method m = targetBean.getClass().getMethod(getIsGetterName(field));
//                propertyAccessor = new PropertyAccessor(m);
//            } catch (final Exception e1) {
//                try {
//                    Method m = targetBean.getClass().getMethod(field);
//                    propertyAccessor = new PropertyAccessor(m);
//                } catch (final Exception e2) {
//                    try {
//                        Field f = targetBean.getClass().getField(field);
//                        propertyAccessor = new PropertyAccessor(f);
//                    } catch (final Exception e3) {
//                        throw new RuntimeException("not found accessor for: " + targetBean + "#" + field, e3);
//                    }
//                }
//            }
//        }
//        return propertyAccessor.get(targetBean);
//    }
//
//    static String getGetterName(final String propName) {
//        if (Character.isUpperCase(propName.charAt(0)) || propName.length() > 1
//                && Character.isUpperCase(propName.charAt(1))) {
//            return "get" + propName;
//        }
//        return "get" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
//    }
//
//    static String getIsGetterName(final String propName) {
//        if (Character.isUpperCase(propName.charAt(0)) || propName.length() > 1
//                && Character.isUpperCase(propName.charAt(1))) {
//            return "is" + propName;
//        }
//        return "is" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
//    }


}

