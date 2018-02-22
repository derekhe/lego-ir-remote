package com.obd.infrared.detection;

import android.os.Build;

public class DeviceDetector {

    public static boolean isSamsung() {
        return isDevice("SAMSUNG");
    }

    public static boolean isLg() {
        return isDevice("LG") || isDevice("LGE");
    }

    public static boolean isHtc() {
        return isDevice("HTC");
    }

    public static boolean isLe() {
        return isDevice("LE") || isDevice("LEECO") || isDevice("LETV") || isDevice("COOLPAD") || isDevice("LETVITWO");
    }

    private static boolean isDevice(String manufactureName) {
        return Build.MANUFACTURER.equalsIgnoreCase(manufactureName);
    }
}
