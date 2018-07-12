package com;

public class Tools {
    public static boolean isDigit(String s){
        try {
            Long.parseLong(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
