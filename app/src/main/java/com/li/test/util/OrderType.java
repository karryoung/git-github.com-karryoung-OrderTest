package com.li.test.util;

import java.util.LinkedHashMap;

/**
 * 类型
 **/
public class OrderType {
    public static final int TYPE1 = 1;
    public static final int TYPE2 = 2;
    public static final int TYPE3 = 3;
    public static final int TYPE4 = 4;
    public static final int TYPE5 = 5;
    public static final int TYPE6 = 6;
    public static final int TYPE7 = 7;
    public static final int TYPE8 = 8;
    public static final int TYPE9 = 9;
    public static final int TYPE10 = 10;

    private static LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<String, Integer>(){
        {
            put("kd", -1);
            put("dds", 1);
            put("ds", 2);
            put("dds", 3);
            put("fsf", 4);
            put("gw", 5);
            put("gew", 6);
            put("sfas", 8);
            put("dsfs", 9);
            put("sdgsg", 10);
            put("sertew", 7);
        }
    };

    public static String[] array_type = {"ffd", "fds", "gs", "ds", "fds", "df", "sd", "ds",
    "dfsgfd", "sds"};//状态的数据源
}
