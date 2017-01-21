package com.spinstreet.paparazzi;

public class Session {

    public static String username = "";
    public static String jwt = "";

    public static String url(String part) {
        return "https://spin.wander.host/api/1/" + part;
    }
}
