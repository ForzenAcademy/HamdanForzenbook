package com.hamdan.forzenbook.java.core.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hamdan.forzenbook.java.core.GeneralUtilityFunctions;

import java.util.Calendar;

public class DialogUtils {

    public static AlertDialog standardAlertDialog(Context context, String title, String body, String buttonText, Runnable onDismiss) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(title);
        dialog.setMessage(body);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonText, (dialogInterface, i) -> dialog.dismiss());
        dialog.setOnDismissListener(listener -> onDismiss.run());
        dialog.show();
        return dialog;
    }

    public static void datePickerDialog(Context context, String birthDate, DateDialogOnTextChange onTextChange, Runnable onDateSubmission, Runnable onDismiss) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedYear = currentYear;
        int selectedMonth = currentMonth;
        int selectedDay = currentDay;
        if (!birthDate.isEmpty()) {
            String[] split = birthDate.split("-");
            if(split.length==3){
                selectedYear = Integer.parseInt(split[2]);
                selectedMonth = Integer.parseInt(split[0]) - 1;
                selectedDay = Integer.parseInt(split[1]);
            }
        }
        DatePickerDialog.OnDateSetListener dateSetListener;
        dateSetListener = (view, year, month, day) -> {
            String birthDay = GeneralUtilityFunctions.stringDate(month + 1, day, year, context);
            onTextChange.operation(birthDay);
            onDateSubmission.run();
        };
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                com.hamdan.forzenbook.ui.core.R.style.MySpinnerDatePickerStyle,
                dateSetListener,
                selectedYear,
                selectedMonth,
                selectedDay
        );
        dialog.setOnDismissListener(dialogInterface -> {
            onDismiss.run();
        });
        dialog.show();
    }


}
