package com.example.xnb.util;

import cn.hutool.core.util.ObjectUtil;

public class MyTest {
    public static void main(String[] args) {
//        List<String> uuids = Arrays.asList("121212123");
//        String uuid;
//        do {
//            int i = new Random().nextInt(9999999);
//            while (i < 1000001) {
//                i = new Random().nextInt(9999999);
//            }
//            uuid = i + "";
//        } while (uuids.contains(uuid));
//        System.out.println(uuid);
        //获取秒数
        //long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0));
        //try {
        //    Thread.sleep(3000L);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //long endSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0));
        //long absSeconds = Math.abs(nowSecond - endSecond);
        //System.out.println(absSeconds + "||");
        if (ObjectUtil.isNotEmpty(Integer.valueOf(0))){
            if (Integer.valueOf(0).equals(0)){
                System.out.println(ObjectUtil.isNotEmpty(Integer.parseInt("0")));
            }
        }
    }
}
