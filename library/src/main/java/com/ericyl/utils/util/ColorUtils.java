package com.ericyl.utils.util;

import android.support.annotation.ColorInt;

import java.util.Random;


public class ColorUtils {

    public static final int COLOR_TRANSPARENT = 0x00000000;
    public static final int COLOR_LIGHT_RED_BRIGHT = 0xffff5252;
    public static final int COLOR_RED_BRIGHT = 0xffff1744;
    public static final int COLOR_DARK_RED_BRIGHT = 0xffd50000;

    public static final int COLOR_LIGHT_RED = 0xffe57373;
    public static final int COLOR_RED = 0xfff44336;
    public static final int COLOR_DARK_RED = 0xffd32f2f;

    public static final int COLOR_LIGHT_PINK_BRIGHT = 0xffff4081;
    public static final int COLOR_PINK_BRIGHT = 0xfff50057;
    public static final int COLOR_DARK_PINK_BRIGHT = 0xffc51162;

    public static final int COLOR_LIGHT_PINK = 0xfff06292;
    public static final int COLOR_PINK = 0xffe91e63;
    public static final int COLOR_DARK_PINK = 0xffc2185b;

    public static final int COLOR_LIGHT_PURPLE_BRIGHT = 0xffe040fb;
    public static final int COLOR_PURPLE_BRIGHT = 0xffd500f9;
    public static final int COLOR_DARK_PURPLE_BRIGHT = 0xffaa00ff;

    public static final int COLOR_LIGHT_PURPLE = 0xffba68c8;
    public static final int COLOR_PURPLE = 0xff9c27b0;
    public static final int COLOR_DARK_PURPLE = 0xff7b1fa2;

    public static final int COLOR_LIGHT_DEEP_PURPLE_BRIGHT = 0xff7c4dff;
    public static final int COLOR_DEEP_PURPLE_BRIGHT = 0xff651fff;
    public static final int COLOR_DARK_DEEP_PURPLE_BRIGHT = 0xff6200ea;

    public static final int COLOR_LIGHT_DEEP_PURPLE = 0xff9575cd;
    public static final int COLOR_DEEP_PURPLE = 0xff673ab7;
    public static final int COLOR_DARK_DEEP_PURPLE = 0xff512da8;

    public static final int COLOR_LIGHT_INDIGO_BRIGHT = 0xff536dfe;
    public static final int COLOR_INDIGO_BRIGHT = 0xff3d5afe;
    public static final int COLOR_DARK_INDIGO_BRIGHT = 0xff304ffe;

    public static final int COLOR_LIGHT_INDIGO = 0xff7986cb;
    public static final int COLOR_INDIGO = 0xff3f51b5;
    public static final int COLOR_DARK_INDIGO = 0xff303f9f;

    public static final int COLOR_LIGHT_BLUE_BRIGHT = 0xff448aff;
    public static final int COLOR_BLUE_BRIGHT = 0xff2979ff;
    public static final int COLOR_DARK_BLUE_BRIGHT = 0xff2962ff;

    public static final int COLOR_LIGHT_BLUE = 0xff64b5f6;
    public static final int COLOR_BLUE = 0xff2196f3;
    public static final int COLOR_DARK_BLUE = 0xff1976d2;

    public static final int COLOR_LIGHT_BLUE_L_BRIGHT = 0xff40c4ff;
    public static final int COLOR_BLUE_L_BRIGHT = 0xff00b0ff;
    public static final int COLOR_DARK_BLUE_L_BRIGHT = 0xff0091ea;

    public static final int COLOR_LIGHT_BLUE_L = 0xff4fc3f7;
    public static final int COLOR_BLUE_L = 0xff03a9f4;
    public static final int COLOR_DARK_BLUE_L = 0xff0288d1;

    public static final int COLOR_LIGHT_CYAN_BRIGHT = 0xff18ffff;
    public static final int COLOR_CYAN_BRIGHT = 0xff00e5ff;
    public static final int COLOR_DARK_CYAN_BRIGHT = 0xff00b8d4;

    public static final int COLOR_LIGHT_CYAN = 0xff4dd0e1;
    public static final int COLOR_CYAN = 0xff00bcd4;
    public static final int COLOR_DARK_CYAN = 0xff0097a7;

    public static final int COLOR_LIGHT_TEAL_BRIGHT = 0xff64ffda;
    public static final int COLOR_TEAL_BRIGHT = 0xff1de9b6;
    public static final int COLOR_DARK_TEAL_BRIGHT = 0xff00bfa5;

    public static final int COLOR_LIGHT_TEAL = 0xff4db6ac;
    public static final int COLOR_TEAL = 0xff009688;
    public static final int COLOR_DARK_TEAL = 0xff00796b;

    public static final int COLOR_LIGHT_GREEN_BRIGHT = 0xff69f0ae;
    public static final int COLOR_GREEN_BRIGHT = 0xff00e676;
    public static final int COLOR_DARK_GREEN_BRIGHT = 0xff00c853;

    public static final int COLOR_LIGHT_GREEN = 0xff81c784;
    public static final int COLOR_GREEN = 0xff4caf50;
    public static final int COLOR_DARK_GREEN = 0xff388e3c;

    public static final int COLOR_LIGHT_GREEN_L_BRIGHT = 0xffb2ff59;
    public static final int COLOR_GREEN_L_BRIGHT = 0xff76ff03;
    public static final int COLOR_DARK_GREEN_L_BRIGHT = 0xff64dd17;

    public static final int COLOR_LIGHT_GREEN_L = 0xffaed581;
    public static final int COLOR_GREEN_L = 0xff8bc34a;
    public static final int COLOR_DARK_GREEN_L = 0xff689f38;

    public static final int COLOR_LIGHT_LIME_BRIGHT = 0xffeeff41;
    public static final int COLOR_LIME_BRIGHT = 0xffc6ff00;
    public static final int COLOR_DARK_LIME_BRIGHT = 0xffaeea00;

    public static final int COLOR_LIGHT_LIME = 0xffdce775;
    public static final int COLOR_LIME = 0xffcddc39;
    public static final int COLOR_DARK_LIME = 0xffafb42b;

    public static final int COLOR_LIGHT_YELLOW_BRIGHT = 0xffffff00;
    public static final int COLOR_YELLOW_BRIGHT = 0xffffea00;
    public static final int COLOR_DARK_YELLOW_BRIGHT = 0xffffd600;

    public static final int COLOR_LIGHT_YELLOW = 0xfffff176;
    public static final int COLOR_YELLOW = 0xffffeb3b;
    public static final int COLOR_DARK_YELLOW = 0xfffbc02d;

    public static final int COLOR_LIGHT_AMBER_BRIGHT = 0xffffd740;
    public static final int COLOR_AMBER_BRIGHT = 0xffffc400;
    public static final int COLOR_DARK_AMBER_BRIGHT = 0xffffab00;

    public static final int COLOR_LIGHT_AMBER = 0xffffd54f;
    public static final int COLOR_AMBER = 0xffffc107;
    public static final int COLOR_DARK_AMBER = 0xffffa000;

    public static final int COLOR_LIGHT_ORANGE_BRIGHT = 0xffffab40;
    public static final int COLOR_ORANGE_BRIGHT = 0xffff9100;
    public static final int COLOR_DARK_ORANGE_BRIGHT = 0xffff6d00;

    public static final int COLOR_LIGHT_ORANGE = 0xffffb74d;
    public static final int COLOR_ORANGE = 0xffff9800;
    public static final int COLOR_DARK_ORANGE = 0xfff57c00;

    public static final int COLOR_LIGHT_DEEP_ORANGE_BRIGHT = 0xffff6e40;
    public static final int COLOR_DEEP_ORANGE_BRIGHT = 0xffff3d00;
    public static final int COLOR_DARK_DEEP_ORANGE_BRIGHT = 0xffdd2c00;

    public static final int COLOR_LIGHT_DEEP_ORANGE = 0xffff8a65;
    public static final int COLOR_DEEP_ORANGE = 0xffff5722;
    public static final int COLOR_DARK_DEEP_ORANGE = 0xffe64a19;

    public static final int COLOR_LIGHT_BROWN = 0xffa1887f;
    public static final int COLOR_BROWN = 0xff795548;
    public static final int COLOR_DARK_BROWN = 0xff5d4037;

    public static final int COLOR_LIGHT_GREY = 0xffe0e0e0;
    public static final int COLOR_GREY = 0xff9e9e9e;
    public static final int COLOR_DARK_GREY = 0xff616161;

    public static final int COLOR_LIGHT_BLUE_GREY = 0xff90a4ae;
    public static final int COLOR_BLUE_GREY = 0xff607d8b;
    public static final int COLOR_DARK_BLUE_GREY = 0xff455a64;

    public static final int COLOR_LIGHT_GRAY = 0xffefefef;
    public static final int COLOR_GRAY = 0xffcecece;
    public static final int COLOR_DARK_GRAY = 0xffc8c8c8;

    public static final int COLOR_WHITE = 0xffffffff;
    public static final int COLOR_BLACK = 0xff000000;

    private static final int[] COLORS = new int[]{
            COLOR_BLUE, COLOR_ORANGE, COLOR_GREEN, COLOR_RED, COLOR_GREY,
            COLOR_PURPLE, COLOR_PINK, COLOR_INDIGO, COLOR_TEAL, COLOR_BROWN,
            COLOR_AMBER, COLOR_YELLOW, COLOR_CYAN, COLOR_PINK, COLOR_LIME
    };

    @ColorInt
    public static int getRandomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

}
