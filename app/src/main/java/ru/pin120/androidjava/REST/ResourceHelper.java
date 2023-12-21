package ru.pin120.androidjava.REST;

import android.content.Context;

public class ResourceHelper {
    public static int getResourceIdByName(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }
}
