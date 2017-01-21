package com.spinstreet.paparazzi;

/**
 * Created by merlin on 2017/01/21.
 */

public class Session {

    public static String username = "";
    public static String jwt = "";

    public static String url(String part) {
        return "http://paparazzi.io/" + part;
    }
}
