package com.example.servicenovigrad;

import android.os.Build;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    public static boolean valStringInput (String input){
        if(input==null) return false;
        if(input.equals("")) return false;
        input=input.trim();
        return true;
    }

    public static boolean valUsername (String username){
        if(!valStringInput(username)) return false;
        for (int i = 0; i < username.length(); i++) {
            // checks whether the character is a whitespace character
            if (Character.isWhitespace(username.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static boolean valPassword (String password){
        if(!valStringInput(password)) return false;
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    public static boolean valSpinner(Spinner spinner){
        if (spinner.getSelectedItem()==null){
            return false;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validateField(String input, String type){
        boolean result=true;
        switch(type){
            case "DATE":
                try {
                    // ResolverStyle.STRICT for 30, 31 days checking, and also leap year.
                    LocalDate.parse(input,
                            DateTimeFormatter.ofPattern("uuuu-M-d")
                                    .withResolverStyle(ResolverStyle.STRICT)
                    );

                } catch (Exception e) {
                    result = false;
                }
                break;
            case "ADDRESS":
                result=input.matches(
                        "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" );
                break;
        }
        return result;
    }


}
