package com.irfankhoirul.popularmovie.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;


public class DisplayMetricUtils {

    public static int convertDpToPixel(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = (float) dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

}
