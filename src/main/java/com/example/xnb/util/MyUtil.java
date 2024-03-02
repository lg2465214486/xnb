package com.example.xnb.util;

import java.util.List;
import java.util.Random;

public class MyUtil {

    public static String getUUID(List<String> uuids) {
        String uuid;
        do {
            int i = new Random().nextInt(9999999);
            while (i < 1000001) {
                i = new Random().nextInt(9999999);
            }
            uuid = i + "";
        }while (uuids.contains(uuid));
        return uuid;
    }
}
