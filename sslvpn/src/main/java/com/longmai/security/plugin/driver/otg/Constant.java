package com.longmai.security.plugin.driver.otg;

import java.util.HashMap;
import java.util.Map;

public final class Constant {
    public static final Map<Integer, int[]> VIDs = new HashMap();

    static {
        VIDs.put(Integer.valueOf(59476), new int[2]);
        Map map = VIDs;
        Integer valueOf = Integer.valueOf(1372);
        int[] obj = new int[2];
        obj[0] = 3;
        map.put(valueOf, obj);
    }
}
