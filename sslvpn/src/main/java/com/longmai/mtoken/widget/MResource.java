package com.longmai.mtoken.widget;

import android.content.Context;

public class MResource {
    public static int getIdByName(Context context, String className, String name) {
        try {
            Class[] classes = Class.forName(context.getPackageName() + ".R").getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null) {
                return desireClass.getField(name).getInt(desireClass);
            }
            return 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return 0;
        } catch (SecurityException e3) {
            e3.printStackTrace();
            return 0;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return 0;
        } catch (NoSuchFieldException e5) {
            e5.printStackTrace();
            return 0;
        }
    }

    public static int[] getIdsByName(Context context, String className, String name) {
        try {
            Class[] classes = Class.forName(context.getPackageName() + ".R").getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass == null || desireClass.getField(name).get(desireClass) == null || !desireClass.getField(name).get(desireClass).getClass().isArray()) {
                return null;
            }
            return (int[]) desireClass.getField(name).get(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        } catch (SecurityException e3) {
            e3.printStackTrace();
            return null;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return null;
        } catch (NoSuchFieldException e5) {
            e5.printStackTrace();
            return null;
        }
    }
}
