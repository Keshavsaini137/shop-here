package com.shop_here.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String raw = "admin123";
        String encoded = encoder.encode(raw);
        System.out.println(encoded);
    }
}
