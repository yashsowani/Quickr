package com.example.Quickr.Service;

public interface BaseConversionService {
    String ALLOWED_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    char[] allowedCharacters = ALLOWED_STRING.toCharArray();
    int base = allowedCharacters.length;

    String encode(long input);

    long decode(String input);
}
