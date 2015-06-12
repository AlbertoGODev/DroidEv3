package com.guerritastudio.albertogarcia.droidev3.app;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import com.guerritastudio.albertogarcia.droidev3.R;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class Utils {

    public static InputFilter[] getFilterIP() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        return filters;
    }
    public static String getColorSt(Context context,int colorSample) {

        switch (colorSample) {
            case -1:
                return context.getResources().getString(R.string.color_s_none);
            case 0:
                return context.getResources().getString(R.string.color_s_red);
            case 1:
                return context.getResources().getString(R.string.color_s_green);
            case 2:
                return context.getResources().getString(R.string.color_s_blue);
            case 3:
                return context.getResources().getString(R.string.color_s_yellow);
            case 6:
                return context.getResources().getString(R.string.color_s_white);
            case 7:
                return context.getResources().getString(R.string.color_s_black);
            case 13:
                return context.getResources().getString(R.string.color_s_brown);
            default:
                return "null";
        }
    }
}
