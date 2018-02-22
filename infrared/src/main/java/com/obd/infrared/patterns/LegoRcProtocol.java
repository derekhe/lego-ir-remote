package com.obd.infrared.patterns;

/**
 * Created by Vidar on 31.01.2015.
 */

public class LegoRcProtocol {
    // Modes
    public static final int COMBO_DIRECT_MODE = 0x1;
    public static final int COMBO_PWM_MODE = 0x4;
    public static final int SINGLE_PIN_CONTINUOUS = 0x2;
    public static final int SINGLE_PIN_TIMEOUT = 0x3;
    public static final int SINGLE_OUTPUT = 0x4;
    //speeds
    public static final int RED_FLT = 0x0;
    public static final int RED_FWD = 0x1;
    public static final int RED_REV = 0x2;
    public static final int RED_BRK = 0x3;
    public static final int BLUE_FLT = 0x0;
    public static final int BLUE_FWD = 0x4;
    public static final int BLUE_REV = 0x8;
    public static final int BLUE_BRK = 0xC;
    //pwm speeds
    public static final int FLOAT = 0x0;
    public static final int FWD_1 = 0x1;
    public static final int FWD_2 = 0x2;
    public static final int FWD_3 = 0x3;
    public static final int FWD_4 = 0x4;
    public static final int FWD_5 = 0x5;
    public static final int FWD_6 = 0x6;
    public static final int FWD_7 = 0x7;
    public static final int BRK_FLOAT = 0x8;
    public static final int REV_7 = 0x9;
    public static final int REV_6 = 0xA;
    public static final int REV_5 = 0xB;
    public static final int REV_4 = 0xC;
    public static final int REV_3 = 0xD;
    public static final int REV_2 = 0xE;
    public static final int REV_1 = 0xF;
    //channels
    public static final int CH1 = 0x0;
    public static final int CH2 = 0x1;
    public static final int CH3 = 0x2;
    public static final int CH4 = 0x3;
    //toggle
    public static final int[] TOGGLE = new int[]{0, 0, 0, 0};

    public static int getChannelCode(int channel) {
        if (channel == 1) return LegoRcProtocol.CH1;
        if (channel == 2) return LegoRcProtocol.CH2;
        if (channel == 3) return LegoRcProtocol.CH3;
        if (channel == 4) return LegoRcProtocol.CH4;
        throw new IllegalArgumentException("Channel must be between 1 and 4");
    }

    public static int getForwardSpeedCode(int speed) {
        if (speed == 1) return LegoRcProtocol.FWD_1;
        if (speed == 2) return LegoRcProtocol.FWD_2;
        if (speed == 3) return LegoRcProtocol.FWD_3;
        if (speed == 4) return LegoRcProtocol.FWD_4;
        if (speed == 5) return LegoRcProtocol.FWD_5;
        if (speed == 6) return LegoRcProtocol.FWD_6;
        if (speed == 7) return LegoRcProtocol.FWD_7;
        return LegoRcProtocol.FLOAT;
    }

    public static int getReverseSpeed(int speed) {
        if (speed == 1) return REV_1;
        if (speed == 2) return REV_2;
        if (speed == 3) return REV_3;
        if (speed == 4) return REV_4;
        if (speed == 5) return REV_5;
        if (speed == 6) return REV_6;
        if (speed == 7) return REV_7;
        return FLOAT;
    }

    public static int getPwmCode(int channel, int blueSpeed, int redSpeed) {
        int nib1 = COMBO_PWM_MODE | channel;
        int nib2 = blueSpeed;
        int nib3 = redSpeed;
        return createCode(nib1, nib2, nib3);
    }

    public static int[] generatePattern(int code){
        boolean [] bits = new boolean[16];
        for (int i = 15; i >= 0; i--) {
            bits[15 - i] = (code & (1 << i)) != 0;
        }
        int[] pattern = new int[36];

        for (int i = 0; i < 16; i++)
        {
            pattern[2 + (i*2)] = 6;
            if (bits[i]) {
                pattern[3 + (i*2)] = 21;
            } else {
                pattern[3 + (i*2)] = 10;
            }
        }
        pattern[0] = 6;
        pattern[1] = 39;
        pattern[34] = 6;
        pattern[35] = 39;
        return pattern;
    }

    private static int createLrc(int nib1, int nib2, int nib3) {
        return 0xf ^ nib1 ^ nib2 ^nib3;
    }

    private static int createCode(int nib1, int nib2, int nib3) {
        return ((nib1 << 12) | (nib2 << 8) | (nib3 << 4) | (createLrc(nib1, nib2, nib3)));
    }

    public static int[] FullSpeed(){
        int nib1 = CH1;
        int nib2 = COMBO_DIRECT_MODE;
        int nib3 = BLUE_FLT | RED_FWD;
        return generatePattern(createCode(nib1, nib2, nib3));
    }

    public static int[] Reverse(){
        int nib1 = CH1;
        int nib2 = COMBO_DIRECT_MODE;
        int nib3 = BLUE_FLT | RED_REV;
        return generatePattern(createCode(nib1, nib2, nib3));
    }
}
