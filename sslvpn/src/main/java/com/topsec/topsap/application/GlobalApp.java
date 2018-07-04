package com.topsec.topsap.application;

import java.util.ArrayList;

public class GlobalApp {
    public static ArrayList<String> alist;
    public static boolean isDelete = false;
    public static boolean isEnglish = false;
    public static boolean isNULL = false;
    public static ArrayList<String> returnAlist;

    public static ArrayList<String> getReturnAlist() {
        return returnAlist;
    }

    public static void setReturnAlist(ArrayList<String> returnAlist) {
        returnAlist = returnAlist;
    }

    public static ArrayList<String> getAlist() {
        return alist;
    }

    public static void setAlist(ArrayList<String> li) {
        alist = li;
    }
}
