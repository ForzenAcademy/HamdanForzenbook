package com.hamdan.forzenbook.java.core;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;
import com.hamdan.forzenbook.ui.core.R;

public class GeneralUtilityFunctions {

    public static boolean validateEmail(String email) {
        final String regex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";
        final Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    @NonNull
    public static String stringDate(int month, int day, int year, Context context) {
        final String[] date = context.getString(R.string.create_account_date, month, day, year).split("-");
        StringBuilder returnString = new StringBuilder("");
        for (String element : date) {
            returnString.append(leftPad(element)).append("-");
        }
        return returnString.substring(0, returnString.length() - 1);
    }

    @NonNull
    private static String leftPad(String text) {
        if (text.length() < 2) {
            return "0" + text;
        } else return text;
    }

}
