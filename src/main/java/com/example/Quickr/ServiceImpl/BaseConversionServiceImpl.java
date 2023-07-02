package com.example.Quickr.ServiceImpl;

import com.example.Quickr.Service.BaseConversionService;
import org.springframework.stereotype.Service;

@Service
public class BaseConversionServiceImpl implements BaseConversionService {

    @Override
    public String encode(long input){
        StringBuilder encodedString = new StringBuilder();

        if(input == 0) {
            return String.valueOf(allowedCharacters[0]);
        }

        while (input > 0) {
            encodedString.append(allowedCharacters[(int) (input % base)]);
            input = input / base;
        }

        return encodedString.reverse().toString();
    }

    @Override
    public long decode(String input) {
        char[] characters = input.toCharArray();
        int length = characters.length;

        int decoded = 0;

        //counter is used to avoid reversing input string
        int counter = 1;
        for (int i = 0; i < length; i++) {
            decoded += ALLOWED_STRING.indexOf(characters[i]) * Math.pow(base, length - counter);
            counter++;
        }
        return decoded;
    }
}
