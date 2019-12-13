package com.ds.sapling.buttermanager;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * create by cral
 * create at 2019/12/13
 **/
public class ButterKnifeD {
    private static final Map<String, Constructor<? extends UnBinder>> constructorMap = new HashMap<>();

    public static UnBinder bind(Activity activity) {
        UnBinder result = null;
        String packageName = "com.ds.sapling.butterknife.";
        String className = packageName + activity.getClass().getSimpleName() + "_viewbinding";

        Constructor<? extends UnBinder> constructor = getConstructor(activity, className);

        if (constructor == null){
            return result;
        }

        try {
            result = constructor.newInstance(activity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Constructor<? extends UnBinder> getConstructor(Activity activity, String className) {
        Constructor<? extends UnBinder> constructor = constructorMap.get(className);
        try {
            Class cls = Class.forName(className);
            if (cls != null) {
                constructor = cls.getConstructor(activity.getClass());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return constructor;
    }
}
