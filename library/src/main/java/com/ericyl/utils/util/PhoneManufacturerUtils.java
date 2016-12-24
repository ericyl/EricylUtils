package com.ericyl.utils.util;


public class PhoneManufacturerUtils {
    public static final String KEY = "Key";
    public static final String VALUE = "Value";

    public static final String DEFAULT_VALUE = "UNKNOWN";
    public static final String MEI_ZU_VALUE = "MEIZU";
    public static final String LGE_VALUE = "LGE";
    public static final String HUAWEI_VALUE = "HUAWEI";
    public static final int DEFAULT_KEY = 0x0;
    public static final int MEI_ZU_KEY = 0x1;
    public static final int LGE_KEY = 0x2;
    public static final int HUAWEI_KEY = 0x3;


    public static int getKey() {
        String manufacturer = OSInfoUtils.getManufacturer().toUpperCase();
        int result = DEFAULT_KEY;
        if (manufacturer.equals(MEI_ZU_VALUE))
            result = MEI_ZU_KEY;
        if (manufacturer.equals(LGE_VALUE))
            result = LGE_KEY;
        if (manufacturer.equals(HUAWEI_VALUE))
            result = HUAWEI_KEY;
        return result;
    }

    public static String getValue(int key) {
        String result;
        switch (key) {
            case MEI_ZU_KEY:
                result = MEI_ZU_VALUE;
                break;
            case LGE_KEY:
                result = LGE_VALUE;
                break;
            case HUAWEI_KEY:
                result = HUAWEI_VALUE;
                break;
            default:
                result = DEFAULT_VALUE;
        }
        return result;
    }


}
