package com.dipen.bookstore.utility;

import java.util.UUID;

public class RandomTokenGenerator {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
