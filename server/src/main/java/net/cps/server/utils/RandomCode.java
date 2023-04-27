package net.cps.server.utils;

import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Random;

public class RandomCode {
    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Integer RANDOM_STRING_DEFAULT_LENGTH = 6;
    
    public static @NotNull String generate () {
        StringBuilder randomStr = new StringBuilder();
        Random rand = new SecureRandom();
        
        for (int i = 0 ; i < RANDOM_STRING_DEFAULT_LENGTH ; i++) {
            int index = rand.nextInt(CHAR_LIST.length());
            char ch = CHAR_LIST.charAt(index);
            randomStr.append(ch);
        }
        
        return randomStr.toString();
    }
    
    public static @NotNull String generate (Integer length) {
        StringBuilder randomStr = new StringBuilder();
        Random rand = new SecureRandom();
        
        for (int i = 0 ; i < length ; i++) {
            int index = rand.nextInt(CHAR_LIST.length());
            char ch = CHAR_LIST.charAt(index);
            randomStr.append(ch);
        }
        
        return randomStr.toString();
    }
}
